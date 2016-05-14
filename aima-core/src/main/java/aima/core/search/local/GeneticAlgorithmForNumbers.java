package aima.core.search.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Variant of the genetic algorithm which uses double numbers from a fixed
 * interval instead of symbols from a finite alphabet in the representations of
 * individuals. Reproduction uses values somewhere between the values of the
 * parents. Mutation adds some random offset. Progress tracer implementations
 * can be used to get informed about the running iterations. <br>
 * A typical use case for this genetic algorithm version is finding maximums in
 * a given mathematical (fitness) function.
 * 
 * @author Ruediger Lunde
 *
 */
public class GeneticAlgorithmForNumbers extends GeneticAlgorithm<Double> {

	private double minimum;
	private double maximum;

	/**
	 * Constructor.
	 * 
	 * @param individualLength
	 *            vector length used for the representations of individuals. Use
	 *            1 for analysis of functions f(x).
	 * @param min
	 *            minimal value to be used in vector elements.
	 * @param max
	 *            maximal value to be used in vector elements.
	 * @param mutationProbability
	 *            probability of mutations.
	 */
	public GeneticAlgorithmForNumbers(int individualLength, double min, double max, double mutationProbability) {
		super(individualLength, Collections.<Double> emptySet(), mutationProbability);
		minimum = min;
		maximum = max;
	}

	/** Convenience method. */
	public Individual<Double> createRandomIndividual() {
		List<Double> representation = new ArrayList<Double>(individualLength);
		for (int i = 0; i < individualLength; i++)
			representation.add(minimum + random.nextDouble() * (maximum - minimum));
		return new Individual<Double>(representation);
	}

	/**
	 * Produces for each number in the descendant's representation a random
	 * value between the corresponding values of its parents.
	 */
	@Override
	protected Individual<Double> reproduce(Individual<Double> x, Individual<Double> y) {
		List<Double> newRep = new ArrayList<Double>(x.length());
		double r = random.nextDouble();
		for (int i = 0; i < x.length(); i++)
			newRep.add(x.getRepresentation().get(i) * r + y.getRepresentation().get(i) * (1 - r));
		return new Individual<>(newRep);
	}

	/**
	 * Changes each component in the representation by random. The maximum
	 * change is +- (maximum - minimum) / 4, but smaller changes have a higher
	 * likelihood.
	 */
	@Override
	protected Individual<Double> mutate(Individual<Double> child) {
		List<Double> rep = child.getRepresentation();
		List<Double> newRep = new ArrayList<Double>();
		for (double num : rep) {
			double r = random.nextDouble() - 0.5;
			num += r * r * r * (maximum - minimum) / 2;
			if (num < minimum)
				num = minimum;
			else if (num > maximum)
				num = maximum;
			newRep.add(num);
		}
		return new Individual<>(newRep);
	}
}
