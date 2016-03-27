package aima.core.search.local;

import aima.core.api.search.local.FitnessFunction;
import aima.core.api.search.local.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 * @author Michael Crosscombe
 */
public abstract class BasicGeneticAlgorithm<A> implements GeneticAlgorithm<A> {

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
                    child = mutate(child, this.finiteAlphabet);
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

    public void setTimeLimit(long timeLimitMilliseconds) {
        this.timeLimitMilliseconds = timeLimitMilliseconds;
    }

    public void setMaxFitness(long maxFitness) { this.maxFitness = maxFitness; }
}
