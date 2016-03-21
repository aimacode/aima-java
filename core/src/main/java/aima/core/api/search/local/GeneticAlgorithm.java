package aima.core.api.search.local;

import aima.core.api.search.local.Individual;

import java.util.Set;

/**
 * @author Ciaran O'Reilly
 * @author Michael Crosscombe
 */
public interface GeneticAlgorithm {

    // function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
    Individual<A> geneticAlgorithm(Set<Individual<A>> population,
                                   FitnessFunction<A> fitnessFn);

    Individual<A> randomSelection(Set<Individual<A>> population,
                                          FitnessFunction<A> fitnessFn);

    // function REPRODUCE(x, y) returns an individual
    Individual<A> reproduce(Individual<A> x, Individual<A> y);

    Individual<A> mutate(Individual<A> child);

    Individual<A> bestIndividual(Set<Individual<A>> population,
                                 FitnessFunction<A> fitnessFn);

    int randomOffset(int length);

    void validatePopulation(Set<Individual<A>> population);
}
