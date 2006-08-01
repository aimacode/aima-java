/*
 * Created on Aug 6, 2005
 *
 */
package aima.learning.statistics;

public class SigmoidActivationFunction implements ActivationFunction {

	public double activation(double parameter) {
		if (Double.isNaN(parameter)){
			System.out.println("parameter in sigmoidaf is NAN");
		}
		if (Double.isNaN(Math.tanh(parameter))){
			//System.out.println("value of tanh of " + parameter+ " is  NAN");
		}
		return Math.tanh(parameter);
	}

	public double deriv(double parameter) {
		//TODO
		if (parameter > 1000000){
			return -999999;
		}
		return 1.0 - (parameter*parameter);
	}

}
