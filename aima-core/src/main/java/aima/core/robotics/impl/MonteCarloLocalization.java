package aima.core.robotics.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.probability.bayes.approx.ParticleFiltering;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.RandVar;
import aima.core.robotics.IMcl;
import aima.core.robotics.IMclMap;
import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclPose;
import aima.core.robotics.datatypes.IMclRangeReading;
import aima.core.robotics.datatypes.IMclVector;
import aima.core.util.Randomizer;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page TODO.<br>
 * <br>
 * 
 * As stated on page TODO a Monte-Carlo-Localization is an extension of a {@link ParticleFiltering}.
 * This is true for the functionality but this implementation can not extend the implementation of the ParticleFiltering
 * as both implementations only contain the actual algorithm as a single method. (Thus it is divided into 4 additonal methods in this class.)
 * 
 * 
 * The interface {@link IMcl} that defines the functionality for the Monte-Carlo-Localization is implemented with this class.
 * It is possible to reduce the steps needed for the localization by tweaking the parameters {@code initialParticleCount}, {@code weightCutOff} and {@code maxDistance}.
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> a pose implementing {@link IMclPose}.
 * @param <V> an n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment. This vector describes the angle between two rays in the environment.
 * @param <M> a movement (or sequence of movements) of the robot implementing {@link IMclMove}. 
 * @param <R> a range measurement implementing {@link IMclRangeReading}.
 */
public final class MonteCarloLocalization<P extends IMclPose<P,V,M>, V extends IMclVector, M extends IMclMove<M>, R extends IMclRangeReading<R,V>> implements IMcl<P,V,M,R> {
	
	private static final String SAMPLE_INDEXES_NAME = "SAMPLE_INDEXES";
	
	private final IMclMap<P,V,M,R> map;
	private final Randomizer randomizer;
	
	private RandVar sampleIndexes;
	private double weightCutOff;
	
	/**
	 * TODO
	 * @param map an instance of a class implementing {@link IMclMap}.
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
	//TODO
	protected Set<P> applyMove(Set<P> samples, M move) {
		Set<P> newSamples = new LinkedHashSet<P>();
		for(P sample: samples) {
			newSamples.add(sample.applyMovement(move));
		}
		return newSamples;
	}
	//TODO
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
	 * TODO
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
			newSamples.add(( (P) array[selectedSample]).clone());
		}
		return newSamples;
	}
	
	@Override
	public Set<P> generateCloud(int sampleCount) {
		Set<P>samples = new LinkedHashSet<P>();
		Integer[] indexes = new Integer[sampleCount];
		for(int i=0;i<sampleCount;i++) {
			samples.add(map.randomPose());
			indexes[i] = i;
		}
		sampleIndexes = new RandVar(SAMPLE_INDEXES_NAME, new FiniteIntegerDomain(indexes));
		return samples;
	}
	
	@Override
	public Set<P> localize(Set<P> samples, M move, R[] rangeReadings) {
		if(samples == null) return null;/*initialization phase = call generateCloud*/
		Set<P> newSamples = applyMove(samples, move);/*motion model*/
		double[] w = weightSamples(newSamples, rangeReadings);/*range sensor noise model*/
		newSamples = extendedWeightedSampleWithReplacement(newSamples, w);
		return newSamples;
				
	}
}
