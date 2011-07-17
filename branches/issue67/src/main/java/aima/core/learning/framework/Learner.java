package aima.core.learning.framework;

/**
 * @author Ravi Mohan
 * 
 */
public interface Learner {
	void train(DataSet ds);

	/**
	 * Returns the outcome predicted for the specified example
	 * 
	 * @param e
	 *            an example
	 * 
	 * @return the outcome predicted for the specified example
	 */
	String predict(Example e);

	/**
	 * Returns the accuracy of the hypothesis on the specified set of examples
	 * 
	 * @param ds
	 *            the test data set.
	 * 
	 * @return the accuracy of the hypothesis on the specified set of examples
	 */
	int[] test(DataSet ds);
}
