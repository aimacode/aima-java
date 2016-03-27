package aima.test.core.unit.search.local;

import aima.core.api.search.local.FitnessFunction;
import aima.core.api.search.local.GeneticAlgorithm;

import aima.core.search.local.Individual;
import aima.core.search.local.BasicGeneticAlgorithm;

import aima.test.core.unit.search.local.BasicFitnessFunctionTest;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * A test class for testing the GeneticAlgorithm and FitnessFunction classes.
 *
 * @author Michael Crosscombe
 */
public class BasicGeneticAlgorithmTest extends BasicGeneticAlgorithm<Integer>{

    // Genetic Algorithm-specific variables
    protected int individualLength;
    // Set finiteAlphabet to contain all possible indices in an 8x8 board.
    protected Set<Integer> finiteAlphabet = new HashSet<Integer>(
            Arrays.asList(1,2,3,4,5,6,7,8)
    );
    // Mutation variables
    protected double mutationProbability;
    protected Random random;
    protected int boardDimensions[] = {8,8};

    // Max fitness producible from fitness function
    long maxFitness;
    // Time limit for Genetic Algorithm
    long timeLimitMilliseconds;

    public BasicGeneticAlgorithmTest(int individualLength,
                                     Set<Integer> finiteAlphabet,
                                     double mutationProbability) {

        this(individualLength, finiteAlphabet, mutationProbability, new Random());
    }

    public BasicGeneticAlgorithmTest(int individualLength,
                                     Set<Integer> finiteAlphabet,
                                     double mutationProbability,
                                     Random random) {

        super(individualLength, finiteAlphabet, mutationProbability, random);

        // Set board dimensions for fitness function

    }
}
