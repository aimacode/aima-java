package aima.core.search.local;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import aima.core.search.framework.GoalTest;
import aima.core.search.framework.Metrics;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.8, page
 * 129.<br>
 * <br>
 * 
 * <pre>
 * function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
 *   inputs: population, a set of individuals
 *           FITNESS-FN, a function that measures the fitness of an individual
 *           
 *   repeat
 *     new_population &lt;- empty set
 *     for i = 1 to SIZE(population) do
 *       x &lt;- RANDOM-SELECTION(population, FITNESS-FN)
 *       y &lt;- RANDOM-SELECTION(population, FITNESS-FN)
 *       child &lt;- REPRODUCE(x, y)
 *       if (small random probability) then child &lt;- MUTATE(child)
 *       add child to new_population
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
 * Figure 4.8 A genetic algorithm. The algorithm is the same as the one
 * diagrammed in Figure 4.6, with one variation: in this more popular version,
 * each mating of two parents produces only one offspring, not two.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class GeneticAlgorithm {
	//
	protected Metrics metrics = new Metrics();
	protected static final String POPULATION_SIZE = "populationSize";
	protected static final String ITERATIONS = "iterations";

	//
	private final int individualLength;
	private final Character[] finiteAlphabet;
	private final double mutationProbability;
	private final Random random = new Random();

	public GeneticAlgorithm(int individualLength,
			Set<Character> finiteAlphabet, double mutationProbability) {
		this.individualLength = individualLength;
		this.finiteAlphabet = finiteAlphabet
				.toArray(new Character[finiteAlphabet.size()]);
		this.mutationProbability = mutationProbability;
		assert (this.mutationProbability >= 0.0 && this.mutationProbability <= 1.0);
	}

	/**
	 * Returns the best individual in the specified population, according to the
	 * specified FITNESS-FN and goal test.
	 * 
	 * @param population
	 *            a set of individuals
	 * @param fitnessFn
	 *            a function that measures the fitness of an individual
	 * @param goalTest
	 *            test determines whether a given individual is fit enough to
	 *            return.
	 * 
	 * @return the best individual in the specified population, according to the
	 *         specified FITNESS-FN and goal test.
	 */
	// function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
	// inputs: population, a set of individuals
	// FITNESS-FN, a function that measures the fitness of an individual
	public String geneticAlgorithm(Set<String> population,
			FitnessFunction fitnessFn, GoalTest goalTest) {
		String bestIndividual = null;

		validatePopulation(population);
		clearInstrumentation();
		setPopulationSize(population.size());

		// repeat
		int cnt = 0;
		do {
			bestIndividual = ga(population, fitnessFn);
			cnt++;
			// until some individual is fit enough, or enough time has elapsed
		} while (!goalTest.isGoalState(bestIndividual));
		setIterations(cnt);

		// return the best individual in population, according to FITNESS-FN
		return bestIndividual;
	}

	/**
	 * Returns the best individual in the specified population, according to the
	 * specified FITNESS-FN and number of iterations.
	 * 
	 * @param population
	 *            a set of individuals
	 * @param fitnessFn
	 *            a function that measures the fitness of an individual
	 * @param iterations
	 *            the number of iterations of the genetic algorithm
	 * 
	 * @return the best individual in the specified population, according to the
	 *         specified FITNESS-FN and number of iterations.
	 */
	// function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
	// inputs: population, a set of individuals
	// FITNESS-FN, a function that measures the fitness of an individual
	public String geneticAlgorithm(Set<String> population,
			FitnessFunction fitnessFn, int iterations) {
		String bestIndividual = null;

		validatePopulation(population);
		clearInstrumentation();
		setPopulationSize(population.size());

		// repeat
		// until some individual is fit enough, or enough time has elapsed
		for (int i = 0; i < iterations; i++) {
			bestIndividual = ga(population, fitnessFn);
		}
		setIterations(iterations);

		// return the best individual in population, according to FITNESS-FN
		return bestIndividual;
	}

	/**
	 * Sets the population size and number of iterations to zero.
	 */
	public void clearInstrumentation() {
		setPopulationSize(0);
		setIterations(0);
	}

	/**
	 * Returns all the metrics of the genetic algorithm.
	 * 
	 * @return all the metrics of the genetic algorithm.
	 */
	public Metrics getMetrics() {
		return metrics;
	}

	/**
	 * Returns the population size.
	 * 
	 * @return the population size.
	 */
	public int getPopulationSize() {
		return metrics.getInt(POPULATION_SIZE);
	}

	/**
	 * Sets the population size.
	 * 
	 * @param size
	 *            the population size.
	 */
	public void setPopulationSize(int size) {
		metrics.set(POPULATION_SIZE, size);
	}

	/**
	 * Returns the number of iterations of the genetic algorithm.
	 * 
	 * @return the number of iterations of the genetic algorithm.
	 */
	public int getIterations() {
		return metrics.getInt(ITERATIONS);
	}

	/**
	 * Sets the number of iterations of the genetic algorithm.
	 * 
	 * @param cnt
	 *            the number of iterations.
	 */
	public void setIterations(int cnt) {
		metrics.set(ITERATIONS, cnt);
	}

	//
	// PRIVATE METHODS
	//
	private void validatePopulation(Set<String> population) {
		// Require at least 1 individual in population in order
		// for algorithm to work
		if (population.size() < 1) {
			throw new IllegalArgumentException(
					"Must start with at least a population of size 1");
		}
		// String lengths are assumed to be of fixed size,
		// therefore ensure initial populations lengths correspond to this
		for (String individual : population) {
			if (individual.length() != this.individualLength) {
				throw new IllegalArgumentException("Individual [" + individual
						+ "] in population is not the required length of "
						+ this.individualLength);
			}
		}
	}

	private String ga(Set<String> population, FitnessFunction fitnessFn) {
		// new_population <- empty set
		Set<String> newPopulation = new HashSet<String>();

		// for i = 1 to SIZE(population) do
		for (int i = 0; i < population.size(); i++) {
			// x <- RANDOM-SELECTION(population, FITNESS-FN)
			String x = randomSelection(population, fitnessFn);
			// y <- RANDOM-SELECTION(population, FITNESS-FN)
			String y = randomSelection(population, fitnessFn);
			// child <- REPRODUCE(x, y)
			String child = reproduce(x, y);
			// if (small random probability) then child <- MUTATE(child)
			if (random.nextDouble() <= this.mutationProbability) {
				child = mutate(child);
			}
			// add child to new_population
			newPopulation.add(child);
		}
		// population <- new_population
		population.clear();
		population.addAll(newPopulation);

		return retrieveBestIndividual(population, fitnessFn);
	}

	private String randomSelection(Set<String> population,
			FitnessFunction fitnessFn) {
		String selected = null;

		// Determine all of the fitness values
		double[] fValues = new double[population.size()];
		String[] popArray = population.toArray(new String[population.size()]);
		for (int i = 0; i < popArray.length; i++) {
			fValues[i] = fitnessFn.getValue(popArray[i]);
		}

		// Normalize the fitness values
		fValues = Util.normalize(fValues);
		double prob = random.nextDouble();
		double totalSoFar = 0.0;
		for (int i = 0; i < fValues.length; i++) {
			// Are at last element so assign by default
			// in case there are rounding issues with the normalized values
			totalSoFar += fValues[i];
			if (prob <= totalSoFar) {
				selected = popArray[i];
				break;
			}
		}

		// selected may not have been assigned
		// if there was a rounding error in the
		// addition of the normalized values (i.e. did not total to 1.0)
		if (null == selected) {
			// Assign the last value
			selected = popArray[popArray.length - 1];
		}

		return selected;
	}

	// function REPRODUCE(x, y) returns an individual
	// inputs: x, y, parent individuals
	private String reproduce(String x, String y) {
		// n <- LENGTH(x);
		// Note: this is = this.individualLength
		// c <- random number from 1 to n
		int c = randomOffset(individualLength);
		// return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
		return x.substring(0, c) + y.substring(c);
	}

	private String mutate(String individual) {
		StringBuffer mutInd = new StringBuffer(individual);

		int posOffset = randomOffset(individualLength);
		int charOffset = randomOffset(finiteAlphabet.length);

		mutInd.setCharAt(posOffset, finiteAlphabet[charOffset]);

		return mutInd.toString();
	}

	private String retrieveBestIndividual(Set<String> population,
			FitnessFunction fitnessFn) {
		String bestIndividual = null;
		double bestSoFarFValue = Double.NEGATIVE_INFINITY;

		for (String individual : population) {
			double fValue = fitnessFn.getValue(individual);
			if (fValue > bestSoFarFValue) {
				bestIndividual = individual;
				bestSoFarFValue = fValue;
			}
		}

		return bestIndividual;
	}

	private int randomOffset(int length) {
		return random.nextInt(length);
	}
}