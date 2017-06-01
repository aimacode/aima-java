package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * ProbabilityNumber interface defines arithmetic specifically for probability
 * values. A probability value is quantified between 0 and 1. Operations that
 * violate the property of a probability value are not permitted. This interface
 * is implemented by the various ProbabilityNumber implementations.
 * 
 * @author Nagaraj Poti
 *
 */
public interface ProbabilityNumber extends Comparable<ProbabilityNumber> {

	/**
	 * Get the probability value represented by the ProbabilityNumber
	 * implementation as a standard BigDecimal.
	 * 
	 * @return value of BigDecimal type.
	 */
	BigDecimal getValue();

	/**
	 * Get the MathContext object associated with the ProbabilityNumber object.
	 * MathContext can be set for BigDecimalProbabilityNumber and
	 * RationalProbabilityNumber via the constructor mechanism. The default
	 * MathContext object associated with the implementations is of DECIMAL128
	 * precision. It is possible to go upto an even higher precision all the way
	 * upto UNLIMITED setting.
	 * 
	 * UNLIMITED - A MathContext object whose settings have the values required
	 * for unlimited precision arithmetic. The values of the settings are:
	 * precision=0 and roundingMode=HALF_UP (ignored in this case). When a
	 * MathContext object is supplied with a precision setting of 0
	 * (MathContext.UNLIMITED), arithmetic operations are exact. In the case of
	 * divide, the exact quotient could have an infinitely long decimal
	 * expansion; for example, 1 divided by 3. If the quotient has a
	 * nonterminating decimal expansion and the operation is specified to return
	 * an exact result, an ArithmeticException is thrown. Otherwise, the exact
	 * result of the division is returned, as done for other operations.
	 * 
	 * The rounding modes and precision setting determine how operations return
	 * results with a limited number of digits when the exact result has more
	 * digits than the number of digits returned. First, the total number of
	 * digits to return is specified by the MathContext's precision setting;
	 * this determines the result's precision. The digit count starts from the
	 * leftmost nonzero digit of the exact result. The rounding mode determines
	 * how any discarded trailing digits affect the returned result.
	 * 
	 * For all arithmetic operators, the operation is carried out as though an
	 * exact intermediate result were first calculated and then rounded to the
	 * number of digits specified by the precision setting (if necessary), using
	 * the selected rounding mode.
	 * 
	 * @return MathContext associated with the ProbabilityNumber value.
	 */
	MathContext getMathContext();

	/**
	 * Checks if the probability value is zero.
	 * 
	 * @return true if zero, false otherwise.
	 */
	boolean isZero();

	/**
	 * Checks if the probability value is one.
	 * 
	 * @return true if one, false otherwise.
	 */
	boolean isOne();

	/**
	 * Check if two ProbabilityNumber values are equal.
	 * 
	 * @param that
	 *            is a ProbabilityNumber.
	 * 
	 * @return true if equal, false otherwise.
	 */
	boolean equals(ProbabilityNumber that);

	/**
	 * Add two ProbabilityNumber types and return a ProbabilityNumber.
	 * 
	 * @param addend
	 * 
	 * @return result of addition of two ProbabilityNumber types.
	 */
	ProbabilityNumber add(ProbabilityNumber addend);

	/**
	 * Subtract two ProbabilityNumber types and return a ProbabilityNumber.
	 * 
	 * @param subtrahend
	 * 
	 * @return result of subtraction of two ProbabilityNumber types.
	 */
	ProbabilityNumber subtract(ProbabilityNumber subtrahend);

	/**
	 * Multiply two ProbabilityNumber types and return a ProbabilityNumber.
	 * 
	 * @param multiplicand
	 * 
	 * @return result of multiplication of two ProbabilityNumber types.
	 */
	ProbabilityNumber multiply(ProbabilityNumber multiplicand);

	/**
	 * Divide two ProbabilityNumber types and return a ProbabilityNumber.
	 * 
	 * @param divisor
	 * 
	 * @return result of division of two ProbabilityNumber types.
	 */
	ProbabilityNumber divide(ProbabilityNumber divisor);

	/**
	 * Raise this ProbabilityNumber to an integer exponent.
	 * 
	 * @param exponent
	 * 
	 * @return result of exponentiation.
	 */
	ProbabilityNumber pow(int exponent);

	/**
	 * Raise this ProbabilityNumber to a BigInteger exponent.
	 * 
	 * @param exponent
	 * 
	 * @return result of exponentiation.
	 */
	ProbabilityNumber pow(BigInteger exponent);

	/**
	 * Sum all the ProbabilityNumber elements constituting the iterable and
	 * check if they sum to one.
	 * 
	 * @param allProbabilities
	 * 
	 * @return true if the probabilities sum to one, false otherwise.
	 */
	boolean sumsToOne(Iterable<ProbabilityNumber> allProbabilities);
}