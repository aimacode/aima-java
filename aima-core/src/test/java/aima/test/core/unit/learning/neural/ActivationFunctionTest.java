/**
 * 
 */
package aima.test.core.unit.learning.neural;

import org.junit.Assert;

import aima.core.learning.neural.ActivationFunction;

/**
 * @author Haris Riaz
 *
 */
public class ActivationFunctionTest implements ActivationFunction {

	public void testActivationFunction()
	{
		// test activation function
		Assert.assertEquals(0.5, activation(0.0));
		Assert.assertEquals(0.6224593312, activation(0.5));
		Assert.assertEquals(0.9999546021, activation(10.0));
		Assert.assertEquals(0.7310585786, activation(1));

	}

	@Override
	public double activation(double parameter) {
		// sigmoid function as activation function
		  return (1.0 / (1 + Math.exp(-1 * parameter)));
	
	}

	@Override
	public double deriv(double parameter) {
		// numerical derivative
		double h = parameter * 1e-8;
		return (activation(parameter+h) - activation(parameter-h)) / (2*h);
		
	}

}
