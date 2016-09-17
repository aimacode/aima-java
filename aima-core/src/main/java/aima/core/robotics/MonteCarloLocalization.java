package aima.core.robotics;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.probability.bayes.approx.ParticleFiltering;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.RandVar;
import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclPose;
import aima.core.robotics.datatypes.IMclRangeReading;
import aima.core.robotics.datatypes.IMclVector;
import aima.core.util.Randomizer;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 982.<br>
 * <br>
 * 
 * <pre>
 * function MONTE-CARLO-LOCALIZATION(a, z, N, P(X'|X, v, w), P(z|z*), m) returns a set of samples for the next time step
 * inputs: a, robot velocities v and w
 *         z, range scan z<sub>1</sub>,..., z<sub>M</sub>
 *         P(X'|X,v,w), motion model
 *         P(z|z*), range sensor noise model
 *         m, 2D map of the environment
 * persistent: S, a vector of samples of size N
 * local variables: W, a vector of weights of size N
 *                  S', a temporary vector of particles of size N
 * </pre>
 * 
 * Figure 25.9 A Monte-Carlo-Localization algorithm using a range-scan sensor model with independent noise.
 * The Monte-Carlo-Localization is an extension of a {@link ParticleFiltering} as stated on page 982.
 * This is true for the functionality but this implementation can not extend the implementation of the ParticleFiltering
 * as both implementations only contain the actual algorithm as a single method.
 * <br/><br/>
 * The update cycle of the algorithm is executed by the method {@code localize} for the given set of samples, move and vector of range readings.
 * Before calling this method, a set of samples can be generated through the method {@code generateCloud}, which represents the initialization phase of the pseudocode, for the given size N.
 * This removes the need of specifying the size N on every call of {@code localize} as this information is already contained in the set itself.
 * The method {@code localize} is divided into these two parts implemented each by a single method:
 * <ol>
 * <li>{@code applyMove} represents the first line of the update cycle. It moves all samples according to the move / motion model.</li>
 * <li>{@code weightSamples} represents the second to second last line of the update cycle. A vector of weights is created by this method for the given range scans by comparing every range scan to a ray cast with the correspondent sample through the range sensor noise model.</li>
 * </ol>
 * The WEIGHTED-SAMPLE-WITH-REPLACEMENT is implemented by the method {@code extendedWeightedSampleWithReplacement}. This implementation contains the addition of a cutoff value. All particles having a weight below this cutoff are ignored.
 * <br/><br/>
 * It is possible to reduce the steps needed for the localization by tweaking the  sample count and the parameter {@code cutOff}.
 * <br/><br/>
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> a pose implementing {@link IMclPose}.
 * @param <V> an n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment. This vector describes the angle between two rays in the environment.
 * @param <M> a movement (or sequence of movements) of the robot implementing {@link IMclMove}. 
 * @param <R> a range measurement implementing {@link IMclRangeReading}.
 */
public final class MonteCarloLocalization<P extends IMclPose<P,V,M>, V extends IMclVector, M extends IMclMove<M>, R extends IMclRangeReading<R,V>> {
	
	private static final String SAMPLE_INDEXES_NAME = "SAMPLE_INDEXES";
	
	private final IMclMap<P,V,M,R> map;
	private final Randomizer randomizer;
	
	private RandVar sampleIndexes;
	private double weightCutOff;
	
	/**
	 * @param map an instance of a class implementing {@link IMclMap}.
	 * @param randomizer a {@link Randomizer} that is used for re-sampling.
	 */
	public MonteCarloLocalization(IMclMap<P,V,M,R> map, Randomizer randomizer) {
		this.map = map;
		this.randomizer = randomizer;
	}
	
	/**
	 * Sets the minimum weight of the particles.
	 * @param cutOff the minimum weight below which the corresponding particle gets removed during the {@code resample()} step. Set to zero when in doubt.
	 */
	public void setWeightCutOff(double cutOff) {
		this.weightCutOff = cutOff;
	}
	
	/**
	 * Applies a move to the samples, creating a new {@link Set}.
	 * @param samples the samples the move will be applied to.
	 * @param move the move to be applied to the samples.
	 * @return a new set of size N containing the moved samples.
	 */
	protected Set<P> applyMove(Set<P> samples, M move) {
		Set<P> newSamples = new LinkedHashSet<P>();
		for(P sample: samples) {
			newSamples.add(sample.applyMovement(move.generateNoise()));
		}
		return newSamples;
	}
	
	/**
	 * Weights the samples by a given vector of range scans.
	 * @param samples the samples to be weighted.
	 * @param rangeReadings the vector containing all range scans.
	 * @return a vector of weights of size N.
	 */
	protected double[] weightSamples(Set<P> samples, R[] rangeReadings) {
		Iterator<P> samplesIterator = samples.iterator();
		double[] w = new double[samples.size()];
		for(int j=0;j<samples.size();j++) {
			P sample = samplesIterator.next();
			if(map.isPoseValid(sample)) {
				w[j] = 1.0d;
				for(int i=0;i<rangeReadings.length;i++) {
					w[j] = w[j] * rangeReadings[i].calculateWeight(map.rayCast(sample.addAngle(rangeReadings[i].getAngle())));
				}
			} else {
				w[j] = 0.0d;
			}
		}
		return w;
	}
	
	/**
	 * Taken {@code weightedSampleWithReplacement} out of {@link ParticleFiltering} and extended by a minimum weight.
	 * @param samples the samples to be re-sampled.
	 * @param w the probability distribution on the samples.
	 * @return the new set of samples.
	 */
	@SuppressWarnings("unchecked")
	protected Set<P> extendedWeightedSampleWithReplacement(Set<P> samples, double[] w) {
		int i = 0;
		for(;i<samples.size();i++) {
			if(w[i] > weightCutOff) break;
		}
		if(i >= samples.size()) return generateCloud(samples.size()); /*If all particleCloud are below weightCutOff, generate a new set of samples, as we are lost.*/
		/*WEIGHTED-SAMPLE-WITH-REPLACEMENT:*/
		double[] normalizedW = Util.normalize(w);
		Set<P> newSamples = new LinkedHashSet<P>();
		Object[] array = samples.toArray(new Object[0]);
		for(i=0; i < samples.size(); i++) {
			final int selectedSample = (Integer) ProbUtil.sample(randomizer.nextDouble(),sampleIndexes,normalizedW);
			newSamples.add(((P) array[selectedSample]).clone());
		}
		return newSamples;
	}
	
	/**
	 * This method is the initialization phase of the algorithm. It has to be called to generate a set of samples of count N.
	 * @param N the count of samples.
	 * @return a set containing N samples.
	 */
	public Set<P> generateCloud(int N) {
		Set<P>samples = new LinkedHashSet<P>();
		Integer[] indexes = new Integer[N];
		for(int i=0;i<N;i++) {
			samples.add(map.randomPose());
			indexes[i] = i;
		}
		sampleIndexes = new RandVar(SAMPLE_INDEXES_NAME, new FiniteIntegerDomain(indexes));
		return samples;
	}
	
	/**
	 * Executes the update cycle of the Monte-Carlo-Localization for the given parameters.
	 * @param samples the sample cloud.
	 * @param move the move to be applied to the cloud.
	 * @param rangeReadings the range scan that has been performed after the move has ended.
	 * @return a new Set containing updated samples. {@code null} is returned if {@code samples} is {@code null}.
	 */
	public Set<P> localize(Set<P> samples, M move, R[] rangeReadings) {
		if(samples == null) return null;/*initialization phase = call generateCloud*/
		Set<P> newSamples = applyMove(samples, move);/*motion model*/
		double[] w = weightSamples(newSamples, rangeReadings);/*range sensor noise model*/
		newSamples = extendedWeightedSampleWithReplacement(newSamples, w);
		return newSamples;
	}
}
