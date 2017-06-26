package aima.extra.probability;

import java.math.BigInteger;
import java.math.MathContext;

/**
 * ProbTensor is a helper class which allows for ProbabilityNumber operations to
 * return ProbabilityNumber objects that may contain invalid values. It is
 * possible for intermediate calculations to result in invalid probability
 * values, whereas the outcome after applying all the operations results in a
 * valid probability value.
 * 
 * @author Nagaraj Poti
 */
public class ProbTensor {

	public MathContext ARBITRARY_PRECISION = null;
	
	/**
	 * Add two ProbabilityNumber operands and return the result.
	 * 
	 * @param operand1
	 * @param operand2
	 * 
	 * @return ProbabilityNumber
	 */
	public ProbabilityNumber add(ProbabilityNumber operand1, ProbabilityNumber operand2) {
		synchronized (ProbabilityNumber.class) {
			ProbabilityNumber.checkRequired = false;
			ProbabilityNumber result = operand1.add(operand2);
			ProbabilityNumber.checkRequired = true;
			return result;
		}
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
		synchronized (ProbabilityNumber.class) {
			ProbabilityNumber.checkRequired = false;
			ProbabilityNumber result = operand1.subtract(operand2);
			ProbabilityNumber.checkRequired = true;
			return result;
		}
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
		synchronized (ProbabilityNumber.class) {
			ProbabilityNumber.checkRequired = false;
			ProbabilityNumber result;
			if (null != ARBITRARY_PRECISION) {
				result = operand1.multiply(operand2, ARBITRARY_PRECISION);
			} else {
				result = operand1.multiply(operand2);
			}
			ProbabilityNumber.checkRequired = true;
			return result;
		}
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
		synchronized (ProbabilityNumber.class) {
			ProbabilityNumber.checkRequired = false;
			ProbabilityNumber result;
			if (null != ARBITRARY_PRECISION) {
				result = operand1.divide(operand2, ARBITRARY_PRECISION);
			} else {
				result = operand1.divide(operand2);
			}
			ProbabilityNumber.checkRequired = true;
			return result;
		}
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
		synchronized (ProbabilityNumber.class) {
			ProbabilityNumber.checkRequired = false;
			ProbabilityNumber result;
			if (null != ARBITRARY_PRECISION) {
				result = operand1.pow(operand2, ARBITRARY_PRECISION);
			} else {
				result = operand1.pow(operand2);
			}
			ProbabilityNumber.checkRequired = true;
			return result;
		}
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
		synchronized (ProbabilityNumber.class) {
			ProbabilityNumber.checkRequired = false;
			ProbabilityNumber result;
			if (null != ARBITRARY_PRECISION) {
				result = operand1.pow(operand2, ARBITRARY_PRECISION);
			} else {
				result = operand1.pow(operand2);
			}
			ProbabilityNumber.checkRequired = true;
			return result;
		}
	}
	
	/**
	 * Override the precision of ProbabilityNumber instances returned as a
	 * result of performing operations.
	 * 
	 * @param mc
	 */
	public void overrideComputationPrecision(MathContext mc) {
		this.ARBITRARY_PRECISION = mc;
	}
	
}