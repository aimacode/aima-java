package aima.core.learning.neural;

/**
 * @author Ravi Mohan
 * 
 */
public class HardLimitActivationFunction implements ActivationFunction {

	public double activation(double parameter) {

		if (parameter < 0.0) {
			return 0.0;
		} else {
			return 1.0;
		}
	}

	public double deriv(double parameter) {
		return 0.0;
	}
}
