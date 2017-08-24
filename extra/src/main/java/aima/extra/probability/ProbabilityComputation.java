package aima.extra.probability;

import java.math.BigInteger;
import java.math.MathContext;
import aima.extra.probability.ProbabilityNumber;

/**
 * ProbabilityComputation is a helper class which allows for easy
 * ProbabilityNumber operations when using Java streams. It is possible for
 * intermediate calculations to result in invalid probability values, whereas
 * the outcome after applying all the operations results in a valid probability
 * value.
 * 
 * @author Nagaraj Poti
 */
public class ProbabilityComputation {

	/**
	 * Global mechanism for overriding precision of ProbabilityNumber
	 * computations.
	 */
	private MathContext OVERRIDE_PRECISION = null;

	/**
	 * Add two ProbabilityNumber operands and return the result.
	 * 
	 * @param operand1
	 * @param operand2
	 * 
	 * @return ProbabilityNumber
	 */
	public ProbabilityNumber add(ProbabilityNumber operand1, ProbabilityNumber operand2) {
		ProbabilityNumber result;
		if (null != OVERRIDE_PRECISION) {
			result = operand1.add(operand2, OVERRIDE_PRECISION);
		} else {
			result = operand1.add(operand2);
		}
		return result;
	}

	/**
	 * Subtract two ProbabilityNumber operands and return the result.
	 * 
	 * @param operand1
	 * @param operand2
	 * 
	 * @return ProbabilityNumber
	 */
	public ProbabilityNumber sub(ProbabilityNumber operand1, ProbabilityNumber operand2) {
		ProbabilityNumber result;
		if (null != OVERRIDE_PRECISION) {
			result = operand1.subtract(operand2, OVERRIDE_PRECISION);
		} else {
			result = operand1.subtract(operand2);
		}
		return result;
	}

	/**
	 * Multiply two ProbabilityNumber operands and return the result.
	 * 
	 * @param operand1
	 * @param operand2
	 * 
	 * @return ProbabilityNumber
	 */
	public ProbabilityNumber mul(ProbabilityNumber operand1, ProbabilityNumber operand2) {
		ProbabilityNumber result;
		if (null != OVERRIDE_PRECISION) {
			result = operand1.multiply(operand2, OVERRIDE_PRECISION);
		} else {
			result = operand1.multiply(operand2);
		}
		return result;
	}

	/**
	 * Divide two ProbabilityNumber operands and return the result.
	 * 
	 * @param operand1
	 * @param operand2
	 * 
	 * @return ProbabilityNumber
	 */
	public ProbabilityNumber div(ProbabilityNumber operand1, ProbabilityNumber operand2) {
		ProbabilityNumber result;
		if (null != OVERRIDE_PRECISION) {
			result = operand1.divide(operand2, OVERRIDE_PRECISION);
		} else {
			result = operand1.divide(operand2);
		}
		return result;
	}

	/**
	 * Raise a ProbabilityNumber operand to a power and return the result.
	 * 
	 * @param operand1
	 * @param operand2
	 *            power of int type.
	 * 
	 * @return ProbabilityNumber
	 */
	public ProbabilityNumber pow(ProbabilityNumber operand1, int operand2) {
		ProbabilityNumber result;
		if (null != OVERRIDE_PRECISION) {
			result = operand1.pow(operand2, OVERRIDE_PRECISION);
		} else {
			result = operand1.pow(operand2);
		}
		return result;
	}

	/**
	 * Raise a ProbabilityNumber operand to a power and return the result.
	 * 
	 * @param operand1
	 * @param operand2
	 *            power of BigInteger type.
	 * 
	 * @return ProbabilityNumber
	 */
	public ProbabilityNumber pow(ProbabilityNumber operand1, BigInteger operand2) {
		ProbabilityNumber result;
		if (null != OVERRIDE_PRECISION) {
			result = operand1.pow(operand2, OVERRIDE_PRECISION);
		} else {
			result = operand1.pow(operand2);
		}
		return result;
	}

	/**
	 * Override the precision of ProbabilityNumber instances returned as a
	 * result of performing operations.
	 * 
	 * @param mc
	 *            MathContext object defining precision of intermediate
	 *            computations.
	 */
	public void overrideComputationPrecision(MathContext mc) {
		this.OVERRIDE_PRECISION = mc;
	}
}