package aima.test.core.unit.search.local;

import aima.core.api.search.local.FitnessFunction;
import aima.core.api.search.local.GeneticAlgorithm;

import aima.core.search.local.Individual;
import aima.core.search.local.BasicGeneticAlgorithm;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * A test class for the purpose of testing the FitnessFunction interface.
 *
 * @author Michael Crosscombe
 */
public class BasicFitnessFunctionTest implements FitnessFunction<Integer> {

    // Maximal fitness value: goal state
    private double goalFitnessValue;
    // Board size for the n-queen problem
    private int boardDimensions[];

    /**
     * Initial action should be to calculate maximal fitness value
     * of a given population.
     * In the N Queen example, this is the number of non-attacking
     * pairs with a maximal fitness value of (n * (n-1)) / 2 for n
     * queens.
     */
    public BasicFitnessFunctionTest(Individual<Integer> individual) {}

    public double getValue(Individual<Integer> individual) {

        // Fitness value for individual
        double fitnessValue = 0.0;

        for (individual : )

        return fitnessValue;
    }

    public Individual<Integer> generateRandomIndividual(Set<Integer> finiteAlphabet) {

    }

    public void  {
        this.boardDimensions = dimensions;
    }
}
