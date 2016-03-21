package aima.core.search.local;

import aima.core.api.search.local.FitnessFunction;
import aima.core.api.search.local.GeneticAlgorithm;

import java.util.LinkedHashSet;
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
public class BasicGeneticAlgorithm<A> implements GeneticAlgorithm<A> {

    // Genetic Algorithm-specific variables
    protected int individualLength;
    protected List<A> finiteAlphabet;
    // Mutation variables
    protected double mutationProbability;
    protected Random random;
    // Max fitness producible from fitness function
    long maxFitness;
    // Time limit for Genetic Algorithm
    long timeLimitMilliseconds;

    public BasicGeneticAlgorithm(int individualLength,
                                 Set<A> finiteAlphabet,
                                 double mutationProbability) {

        this(individualLength, finiteAlphabet,
             mutationProbability, new Random()
        );
    }

    public BasicGeneticAlgorithm(int individualLength,
                                 Set<A> finiteAlphabet,
                                 double mutationProbability,
                                 Random random) {

        this.individualLength = individualLength;
        this.finiteAlphabet = new ArrayList<A>(finiteAlphabet);
        this.mutationProbability = mutationProbability;
        this.random = random;

        assert (this.mutationProbability >= 0.0 && this.mutationProbability <= 1.0);
    }

    @Override
    public Individual<A> geneticAlgorithm(Set<Individual<A>> population,
                                          FitnessFunction<A> fitnessFn) {

        // Time variables to timeout GeneticAlgorithm
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        Individual<A> bestIndividual = null;

        // Create a local copy of the population to work with
        List<Individual<A>> localPopulation = new ArrayList<Individual<A>>(population);
        validatePopulation(localPopulation);

        // repeat
        do {
            // new_population &lt;- empty set
            List<Individual<A>> newPopulation = new ArrayList<Individual<A>>(localPopulation.size());
            // for i = 1 to SIZE(population) do
            for (int i = 0; i < localPopulation.size(); i++) {
                // x &lt;- RANDOM-SELECTION(population, FITNESS-FN)
                Individual<A> x = randomSelection(localPopulation, fitnessFn);
                // y &lt;- RANDOM-SELECTION(population, FITNESS-FN)
                Individual<A> y = randomSelection(localPopulation, fitnessFn);
                while (y == x) { // select new y until x and y are different
                    y = randomSelection(localPopulation, fitnessFn);
                }

                // child &lt;- REPRODUCE(x,y)
                Individual<A> child = reproduce(x,y);
                // if (small random probability) then child &lt;- MUTATE(child)
                if (random.nextInt(100) < mutationProbability) {
                    child = mutate(child);
                }

                // add child to new_population
                newPopulation.add(child);
            }
            // population &lt;-  new_population
            localPopulation.clear();
            localPopulation.addAll(newPopulation);

            bestIndividual = bestIndividual(localPopulation, fitnessFn);
            elapsedTime = System.currentTimeMillis() - startTime;
        }
        // until some individual is fit enough, or enough time has elapsed
        while (fitnessFn.getValue(bestIndividual) == maxFitness || elapsedTime < timeLimitMilliseconds);
        // set input population to new population
        population.clear();
        population.addAll(localPopulation);
        // return the best individual in population, according to FITNESS-FN
        return bestIndividual;
    }

    @Override
    // RANDOM-SELECTION(population, FITNESS-FN)
    public Individual<A> randomSelection(List<Individual<A>> population,
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

    @Override
    // function REPRODUCE(x, y) returns an individual
    public Individual<A> reproduce(Individual<A> x, Individual<A> y) {

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

    @Override
    public Individual<A> mutate(Individual<A> child) {

        int mutateOffset = randomOffset(individualLength);
        int alphaOffset = randomOffset(finiteAlphabet.size());

        List<A> mutatedRepresentation = new ArrayList<A>(child.representation());
        mutatedRepresentation.set(mutateOffset, finiteAlphabet.get(alphaOffset));

        return new Individual<A>(mutatedRepresentation);
    }

    @Override
    public Individual<A> bestIndividual(List<Individual<A>> population,
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

    @Override
    public int randomOffset(int length) { return random.nextInt(length); }

    @Override
    public void validatePopulation(List<Individual<A>> population) {

        // Require at least 1 individual in population in order
        // for algorithm to work
        if (population.size() < 1) {
            throw new IllegalArgumentException(
                    "The population must contain at least one individual."
            );
        }
        // String lengths are assumed to be of fixed size,
        // therefore ensure initial populations lengths correspond to this
        for (Individual<A> individual : population) {
            if (individual.length() != this.individualLength) {
                throw new IllegalArgumentException("Individual [" + individual
                        + "] in population is not the required length of "
                        + this.individualLength);
            }
        }
    }

    public void setTimeLimit(long timeLimitMilliseconds) {

        this.timeLimitMilliseconds = timeLimitMilliseconds;
    }

    public void setMaxFitness(long maxFitness) { this.maxFitness = maxFitness; }
}
