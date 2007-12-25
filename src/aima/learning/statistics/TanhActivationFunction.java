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

public class TanhActivationFunction implements ActivationFunction {

	public double activation(double paramter) {
		return Math.tanh(paramter);
	}

	public double deriv(double parameter) {
		return (1.0 - (2.0 * parameter));
	}

}
