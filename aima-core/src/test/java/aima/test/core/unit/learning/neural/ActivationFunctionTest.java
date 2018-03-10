/**
 * 
 */
package aima.test.core.unit.learning.neural;

import org.junit.Assert;

import org.junit.Test;

import aima.core.learning.neural.ActivationFunction;

/**
 * @author Haris Riaz
 *
 */
public class ActivationFunctionTest implements ActivationFunction {

	@Test
	public void testActivationFunction()
	{
		// test activation function
		Assert.assertEquals(4.5398e-005, activation(-10.0),0.001);
		Assert.assertEquals(0.0066929, activation(-5.0),0.001);
		Assert.assertEquals(0.50, activation(0.0),0.001);
		Assert.assertEquals(0.99331, activation(5.0),0.001);
		Assert.assertEquals(0.99995, activation(10.0),0.001);
	}
	
	@Test
	public void testDerivativeFunction()
	{
		// test derivative function
		Assert.assertEquals( 4.5396e-005, deriv(-10.0),0.001);
		Assert.assertEquals( 0.0066481, deriv(-5.0),0.001);		
		Assert.assertEquals( 0.25, deriv(0.0),0.001);
		Assert.assertEquals( 0.0066481, deriv(5.0),0.001);
		Assert.assertEquals( 4.5396e-005, deriv(10.0),0.001);
	}
	

	@Override
	public double activation(double parameter) {
		// activation function as sigmoid function
		  return (1.0 / (1 + Math.exp(-1 * parameter)));
	}

	@Override
	public double deriv(double parameter) {
		// derivative of sigmoid function
		return (activation(parameter) * (1 - activation(parameter)));	
	}

}
