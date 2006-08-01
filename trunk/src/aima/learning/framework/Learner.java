/*
 * Created on Apr 14, 2005
 *
 */
package aima.learning.framework;


public interface Learner {
	void train(DataSet ds);
	String predict(Example e);
	int[] test(DataSet ds);
}
