package aima.core.learning.neural;

import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public interface FunctionApproximator {

	/**
	 * Returns the output values for the specified input values
	 * 
	 * @param input
	 *            the input values
	 * 
	 * @return the output values for the specified input values
	 */
	Vector processInput(Vector input);

	/**
	 * Accept an error and change the parameters to accommodate it
	 * 
	 * @param error
	 *            an error vector
	 */
	void processError(Vector error);
}
