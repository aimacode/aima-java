/*
 * Created on Aug 3, 2005
 *
 */
package aima.learning.statistics;

import aima.learning.neural.ActivationFunction;

/**
 * @author Ravi Mohan
 * 
 */

public class SquareActivationFunction implements ActivationFunction {

	public double activation(double parameter) {
		return parameter * parameter;
	}

	public double deriv(double parameter) {
		return 2.0 * parameter;
	}

}
