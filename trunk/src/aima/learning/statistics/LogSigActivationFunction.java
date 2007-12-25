/*
 * Created on Aug 5, 2005
 *
 */
package aima.learning.statistics;

import aima.learning.neural.ActivationFunction;

/**
 * @author Ravi Mohan
 * 
 */
public class LogSigActivationFunction implements ActivationFunction {

	public double activation(double parameter) {
		return 1.0 / (1.0 + Math.pow(Math.E, (-1.0 * parameter)));
	}

	public double deriv(double parameter) {
		return parameter * (1.0 - parameter);
	}

}
