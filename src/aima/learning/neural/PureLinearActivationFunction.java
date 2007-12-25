package aima.learning.neural;

import aima.learning.statistics.ActivationFunction;

public class PureLinearActivationFunction implements ActivationFunction {

	public double activation(double parameter) {
		return parameter;
	}

	public double deriv(double parameter) {

		return 1;
	}

}
