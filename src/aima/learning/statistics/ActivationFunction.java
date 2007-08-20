/*
 * Created on Aug 3, 2005
 *
 */
package aima.learning.statistics;

/**
 * @author Ravi Mohan
 * 
 */

public interface ActivationFunction {
	double activation(double parameter);

	double deriv(double parameter);
}
