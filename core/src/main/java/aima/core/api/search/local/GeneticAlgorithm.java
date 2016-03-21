package aima.core.api.search.local;

import aima.core.search.local.Individual;

import java.util.List;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 * @author Michael Crosscombe
 */
public interface GeneticAlgorithm<A> {

    // function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
    Individual<A> geneticAlgorithm(Set<Individual<A>> population,
                                   FitnessFunction<A> fitnessFn);

    Individual<A> randomSelection(List<Individual<A>> population,
                                  FitnessFunction<A> fitnessFn);

    // function REPRODUCE(x, y) returns an individual
    Individual<A> reproduce(Individual<A> x, Individual<A> y);

    Individual<A> mutate(Individual<A> child);

    Individual<A> bestIndividual(List<Individual<A>> population,
                                 FitnessFunction<A> fitnessFn);

    int randomOffset(int length);

    void validatePopulation(List<Individual<A>> population);
}
