package aima.core.api.search.local;

import aima.core.search.local.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
 *   inputs:    population, a set of individuals
 *              FITNESS-FN, a function that measures the fitness of an individual
 *   repeat
 *      new_population &lt;- empty set
 *      for i = 1 to SIZE(population) do
 *          x &lt;- RANDOM-SELECTION(population, FITNESS-FN)
 *          y &lt;- RANDOM-SELECTION(population, FITNESS-FN)
 *          child &lt;- REPRODUCE(x,y)
 *          if (small random probability) then child &lt;- MUTATE(child)
 *          add child to new_population
 *      population &lt;-  new_population
 *   until some individual is fit enough, or enough time has elapsed
 *   return the best individual in population, according to FITNESS-FN
 * </pre>
 *
 * -------------------------------------------------------------------
 *
 * <pre>
 * function REPRODUCE(x, y) returns an individual
 *   inputs: x, y, parent individuals
 *
 *   n &lt;- LENGTH(x); c &lt;- random number from 1 to n
 *   return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
 * </pre>
 *
 * Figure ?? A genetic algorithm. The algorithm is the same as the one diagrammed in
 * Figure ??, with one variation: in this more popular version, each mating of two parents
 * produces only one offspring, not two.
 *
 * @author Ciaran O'Reilly
 * @author Michael Crosscombe
 */
public interface GeneticAlgorithm<A> {

    Random random = new Random();

    // function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
    Individual<A> geneticAlgorithm(Set<Individual<A>> population,
                                          FitnessFunction<A> fitnessFn);

    /**
     *
     * @param population
     *            the population of individuals to evolve.
     * @param fitnessFn
     *            the fitness function for the population; used to
     *            evaluate which individuals perform better than others
     *            for the specific application.
     * @return a random individual based on the weighted distribution
     *            generated via use of the fitness function (individuals
     *            of higher fitness receive a greater chance of selection).
     */
    default Individual<A> randomSelection(List<Individual<A>> population,
                                         FitnessFunction<A> fitnessFn) {

        Individual<A> selected = null;

        // Determine fitness values for the population
        double[] fitnessValues = new double[population.size()];
        double fitnessTotal = 0.0;
        for (int i = 0; i < population.size(); i++) {
            fitnessValues[i] = fitnessFn.getValue(population.get(i));
            fitnessTotal += fitnessValues[i];
        }

        // Normalise the fitness values
        for (int i = 0; i < population.size(); i++) {
            fitnessValues[i] = fitnessValues[i] / fitnessTotal;
        }

        // Pick the next Individual based on the probability distribution
        // over their fitness values
        double probability = random.nextDouble();
        double total = 0.0;
        for (int i = 0; i < fitnessValues.length; i++) {
            total += fitnessValues[i];
            if (probability <= total) {
                selected = population.get(i);
                break;
            }
        }

        // Pick the last individual in the population if the sum of the
        // normalised probability distribution is not equal to 1 due to
        // a rounding error.
        if (null == selected) {
            selected = population.get(population.size() - 1);
        }

        return selected;
    }

    // function REPRODUCE(x, y) returns an individual
    default Individual<A> reproduce(Individual<A> x, Individual<A> y) {

        // n &lt;- LENGTH(x)
        int n = x.length();
        // c &lt;- random number from 1 to n
        int c = randomOffset(n);

        List<A> childRepresentation = new ArrayList<A>();
        // APPEND(SUBSTRING(x, 1, c))
        childRepresentation.addAll(x.representation().subList(0,c));
        // APPEND(SUBSTRING(y, c+1, n))
        childRepresentation.addAll(y.representation().subList(0,c));

        return new Individual<A>(childRepresentation);

    }

    default Individual<A> mutate(Individual<A> child, List<A> finiteAlphabet) {

        int mutateOffset = randomOffset(child.length());
        int alphaOffset = randomOffset(finiteAlphabet.size());

        List<A> mutatedRepresentation = new ArrayList<A>(child.representation());
        mutatedRepresentation.set(mutateOffset, finiteAlphabet.get(alphaOffset));

        return new Individual<A>(mutatedRepresentation);
    }

    default Individual<A> bestIndividual(List<Individual<A>> population,
                                 FitnessFunction<A> fitnessFn) {

        Individual<A>  bestIndividual = null;
        double bestFitnessValue = Double.NEGATIVE_INFINITY;

        for (Individual<A> individual : population) {
            double fitnessValue = fitnessFn.getValue(individual);
            if (fitnessValue > bestFitnessValue) {
                bestIndividual = individual;
                bestFitnessValue = fitnessValue;
            }
        }

        return bestIndividual;
    }

    default void validatePopulation(List<Individual<A>> population) {

        // Require at least 1 individual in population in order
        // for algorithm to work
        if (population.size() < 1) {
            throw new IllegalArgumentException(
                    "The population must contain at least one individual."
            );
        }
        // String lengths are assumed to be of fixed size,
        // therefore ensure initial populations lengths correspond to this
        int individualLength = -1;
        for (Individual<A> individual : population) {
            // Set individualLength to be the length of the first individual,
            // and check that all others match.
            if (individualLength == -1) {
                individualLength = individual.length();
                continue;
            }
            if (individual.length() != individualLength) {
                throw new IllegalArgumentException(
                    "Individual ["
                    + individual
                    + "] in population is not the required length of "
                    + individualLength
                );
            }
        }
    }

    default int randomOffset(int length) { return random.nextInt(length); }
}
