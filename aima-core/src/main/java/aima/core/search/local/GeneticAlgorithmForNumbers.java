package aima.core.search.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

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
	private List<ProgressTracer> progressTracers = new ArrayList<ProgressTracer>();

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

	/** Population printers can be used to display progress information. */
	public void addProgressTracer(ProgressTracer pTracer) {
		progressTracers.add(pTracer);
	}

	/** Convenience method. */
	public Individual<Double> createRandomIndividual() {
		List<Double> representation = new ArrayList<Double>(individualLength);
		for (int i = 0; i < individualLength; i++)
			representation.add(minimum + random.nextDouble() * (maximum - minimum));
		return new Individual<Double>(representation);
	}

	/**
	 * Starts the genetic algorithm and stops after a specified number of
	 * iterations.
	 */
	public Individual<Double> geneticAlgorithm(Collection<Individual<Double>> population,
			FitnessFunction<Double> fitnessFn, final int maxIterations) {
		GoalTest goalTest = new GoalTest() {
			@Override
			public boolean isGoalState(Object state) {
				return getIterations() >= maxIterations;
			}};
		return super.geneticAlgorithm(population, fitnessFn, goalTest, 0L);
	}

	/** Calls super implementation and additionally informs population printers. */
	@Override
	protected List<Individual<Double>> nextGeneration(List<Individual<Double>> population,
			FitnessFunction<Double> fitnessFn) {
		List<Individual<Double>> result = super.nextGeneration(population, fitnessFn);
		for (ProgressTracer printer : progressTracers)
			printer.traceProgress(getIterations(), population);
		return result;
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

	/**
	 * Interface for progress tracers.
	 * 
	 * @author Ruediger Lunde
	 */
	public static interface ProgressTracer {
		void traceProgress(int itCount, Collection<Individual<Double>> generation);
	}
}
