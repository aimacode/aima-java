/*
 * Created on Aug 3, 2005
 *
 */
package aima.learning.neural;

/**
 * @author Ravi Mohan
 * 
 */

public interface ActivationFunction {
	double activation(double parameter);

	double deriv(double parameter);
}
