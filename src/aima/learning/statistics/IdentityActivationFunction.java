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

public class IdentityActivationFunction implements ActivationFunction {

	public double activation(double parameter) {
		return parameter;
	}

	public double deriv(double parameter) {
		return 1;
	}

}
