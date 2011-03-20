package aima.core.search.local;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 127.
 * 
 * Each state is rated by the objective function, or (in Genetic Algorithm terminology) the fitness function.
 * A fitness function should return higher values for better states.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface FitnessFunction {
	double getValue(String individual);
}
