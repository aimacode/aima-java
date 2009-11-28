package aima.core.learning.framework;

/**
 * @author Ravi Mohan
 * 
 */
public interface Learner {
	void train(DataSet ds);

	String predict(Example e);

	int[] test(DataSet ds);
}
