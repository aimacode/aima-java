package aima.core.search.basic.local;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import aima.core.util.ExecutionController;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page
 * ??.<br>
 * <br>
 * 
 * <pre>
 * function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
 *   inputs: population, a set of individuals
 *           FITNESS-FN, a function that measures the fitness of an individual
 *           
 *   repeat
 *     new_population &lt;- empty set
 *     repeat
 *       x &lt;- RANDOM-SELECTION(population, FITNESS-FN)
 *       y &lt;- RANDOM-SELECTION(population, FITNESS-FN)
 *       child &lt;- REPRODUCE(x, y)
 *       if (small random probability) then child &lt;- MUTATE(child)
 *       add child to new_population
 *     until SIZE(new_population) = SIZE(population)
 *     population &lt;- new_population
 *   until some individual is fit enough, or enough time has elapsed
 *   return the best individual in population, according to FITNESS-FN
 * --------------------------------------------------------------------------------
 * function REPRODUCE(x, y) returns an individual
 *   inputs: x, y, parent individuals
 *   
 *   n &lt;- LENGTH(x); c &lt;- random number from 1 to n
 *   return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
 * </pre>
 * 
 * Figure ?? A genetic algorithm. The algorithm is the same as the one
 * diagrammed in Figure 4.6, with one variation: in this more popular version,
 * each mating of two parents produces only one offspring, not two.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Mike Stampone
 * 
 * @param <A>
 *            the type of the alphabet used in the representation of the
 *            individuals in the population (this is to provide flexibility in
 *            terms of how a problem can be encoded).
 */
public class GeneticAlgorithm<A> {
	/**
	 * function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
	 * 
	 * @param population
	 *            a set of individuals
	 * @param fitnessFn
	 *            a function that measures the fitness of an individual
	 * @return the best individual in the specified population, according to the
	 *         specified FITNESS-FN.
	 */
	public Individual<A> geneticAlgorithm(Set<Individual<A>> population, ToDoubleFunction<Individual<A>> fitnessFN) {
		Individual<A> bestIndividual;
		// repeat
		do {
			// new_population <- empty set
			Set<Individual<A>> new_population = new HashSet<Individual<A>>(population.size());
			// repeat
			do {
				// x <- RANDOM-SELECTION(population, FITNESS-FN)
				Individual<A> x = randomSelection(population, fitnessFN);
				// y <- RANDOM-SELECTION(population, FITNESS-FN)
				Individual<A> y = randomSelection(population, fitnessFN);
				// child <- REPRODUCE(x, y)
				Individual<A> child = reproduce(x, y);
				// if (small random probability) then child <- MUTATE(child)
				if (isSmallRandomProbabilityOfMutation()) {
					child = mutate(child);
				}
				// add child to new_population
				new_population.add(child);
			} // until SIZE(new_population) = SIZE(population)
			while (new_population.size() != population.size());
			population = new_population;
		} // until some individual is fit enough, or enough time has elapsed
		while ((bestIndividual = selectBestIndividualIfReady(population, fitnessFN)) == null);
		// return the best individual in population, according to FITNESS-FN
		return bestIndividual;
	}

	// function REPRODUCE(x, y) returns an individual
	public Individual<A> reproduce(Individual<A> x, Individual<A> y) {
		// n <- LENGTH(x);
		int n = x.length();
		// c <- random number from 1 to n
		int c = random.nextInt(n);
		// return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
		return new Individual<A>(x.substring(0, c), y.substring(c, n));
	}

	//
	// Supporting Code
	private List<A> alphabet;
	private double mutationProbability;
	private Random random;
	private Predicate<Individual<A>> fitEnoughPredicate;
	private ExecutionController executionController;

	public GeneticAlgorithm(List<A> alphabet, double mutationProbability, Random random,
			Predicate<Individual<A>> fitEnoughPredicate, ExecutionController executionController) {
		this.alphabet = alphabet;
		this.mutationProbability = mutationProbability;
		this.random = random;
		this.fitEnoughPredicate = fitEnoughPredicate;
		this.executionController = executionController;
	}

	public Individual<A> randomSelection(Set<Individual<A>> population, ToDoubleFunction<Individual<A>> fitnessFN) {
		Individual<A> selection = null;

		// Determine all of the fitness values
		double[] fValues = new double[population.size()];
		Iterator<Individual<A>> popIt = population.iterator();
		for (int i = 0; popIt.hasNext(); i++) {
			fValues[i] = fitnessFN.applyAsDouble(popIt.next());
		}
		// Normalize the fitness values
		fValues = Util.normalize(fValues);

		// Now select the individual
		double prob = random.nextDouble();
		double totalSoFar = 0.0;
		popIt = population.iterator();
		for (int i = 0; popIt.hasNext(); i++) {
			selection = popIt.next();
			// Determine if this is the individual we really want to select
			totalSoFar += fValues[i];
			if (prob <= totalSoFar) {
				break; // selected
			}
		}

		return selection;
	}

	public boolean isSmallRandomProbabilityOfMutation() {
		return random.nextDouble() <= mutationProbability;
	}

	public Individual<A> mutate(Individual<A> child) {
		// We will mutate the child in place.
		child.getRepresentation().set(random.nextInt(child.length()), alphabet.get(random.nextInt(alphabet.size())));
		return child;
	}

	public Individual<A> selectBestIndividualIfReady(Set<Individual<A>> population,
			ToDoubleFunction<Individual<A>> fitnessFN) {
		Individual<A> best = null;

		// until some individual is fit enough, or enough time has elapsed
		if (!executionController.isExecuting() || population.stream().anyMatch(fitEnoughPredicate)) {
			double bestValue = 0;
			Iterator<Individual<A>> popIt = population.iterator();
			while (popIt.hasNext()) {
				Individual<A> current = popIt.next();
				double currentValue = fitnessFN.applyAsDouble(current);
				if (best == null || currentValue > bestValue) {
					best = current;
					bestValue = currentValue;
				} else if (currentValue == bestValue) {
					// Randomly break ties
					if (random.nextBoolean()) {
						best = current;
					}
				}
			}
		}

		return best;
	}
}
