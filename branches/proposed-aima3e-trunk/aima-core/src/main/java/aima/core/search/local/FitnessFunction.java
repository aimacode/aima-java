package aima.core.search.local;

/**
 * Interface representing a fitness function used by the GeneticAlgorithm
 * to determine the fitness of individuals within the population being
 * evolved.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface FitnessFunction {
	double getValue(String individual);
}
