package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * ProbabilityNumber interface defines arithmetic specifically for probability
 * values. A probability value is quantified between 0 and 1. Although a
 * ProbabilityNumber cannot be initialised with a value outside the bounds [0,
 * 1], operations may cause the value represented to exceed these bounds. It is
 * therefore necessary to validate the result obtained from a series of
 * computations to ensure that the result is a valid probability value. This
 * interface is implemented by the various ProbabilityNumber implementations.
 * 
 * The subclasses are intended to be immutable. Implementations may implement
 * overriden versions of equals and hashcode methods.
 * 
 * @author Nagaraj Poti
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
	 * Precision (def.) - the number of significant digits (Zero before decimal
	 * point does not contribute to the precision count)<br>
	 * 
	 * <br>
	 * Get the MathContext object associated with the ProbabilityNumber object.
	 * MathContext can be set for BigDecimalProbabilityNumber and
	 * RationalProbabilityNumber instances via the constructor mechanism. The
	 * precision of the result of a computation is specified by the precision of
	 * the operands (default) or can be arbitrarily specified all the way upto
	 * the highest precision defined by the UNLIMITED setting (overriding
	 * default behaviour).<br>
	 * 
	 * <br>
	 * UNLIMITED - A MathContext object whose settings have the values required
	 * for unlimited precision arithmetic. The values of the settings are:
	 * precision=0 and roundingMode=HALF_UP (ignored in this case). When a
	 * MathContext object is supplied with a precision setting of 0
	 * (MathContext.UNLIMITED), results of arithmetic operations returned are
	 * exact. In the case of division, the exact quotient could have an
	 * infinitely long decimal expansion; for example, 1 divided by 3. If the
	 * quotient has a nonterminating decimal expansion and the operation is
	 * specified to return an exact result, an ArithmeticException is thrown.
	 * Otherwise, the exact result of the division is returned, as done for
	 * other operations.<br>
	 * 
	 * <br>
	 * The rounding modes and precisions determine how operations return results
	 * with a limited number of digits when the exact result has more digits
	 * than the number of digits returned. The total number of digits returned
	 * is determined by (i.e min(operand_1_precision, operand_2_precision) + 1);
	 * this behaviour can however be overriden. The digit count starts from the
	 * leftmost nonzero digit of the exact result. The rounding mode determines
	 * how any discarded trailing digits affect the returned result.<br>
	 * 
	 * <br>
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
	 * Checks if the probability value represented is valid i.e it falls within
	 * the range [0, 1]. It is possible for operations on ProbabilityNumber
	 * instances to cause the result to either overflow or underflow the range
	 * [0, 1].
	 * 
	 * @return true if a valid probability value, false otherwise.
	 */
	boolean isValid();

	/**
	 * Add two ProbabilityNumber types and return a new ProbabilityNumber
	 * representing the result of addition.
	 * 
	 * @param addend
	 * 
	 * @return a new ProbabilityNumber representing the result of addition of
	 *         two ProbabilityNumber types.
	 */
	ProbabilityNumber add(ProbabilityNumber addend);

	/**
	 * Subtract two ProbabilityNumber types and return a new ProbabilityNumber
	 * representing the result of subtraction.
	 * 
	 * @param subtrahend
	 * 
	 * @return a new ProbabilityNumber representing the result of subtraction of
	 *         two ProbabilityNumber types.
	 */
	ProbabilityNumber subtract(ProbabilityNumber subtrahend);

	/**
	 * Multiply two ProbabilityNumber types and return a new ProbabilityNumber
	 * representing the result of multiplication.
	 * 
	 * @param multiplicand
	 * 
	 * @return a new ProbabilityNumber representing the result of multiplication
	 *         of two ProbabilityNumber types.
	 */
	ProbabilityNumber multiply(ProbabilityNumber multiplicand);

	/**
	 * Multiply two ProbabilityNumber types and return a new ProbabilityNumber
	 * representing the result of multiplication.
	 * 
	 * @param multiplicand
	 * 
	 * @param mc
	 *            MathContext for DEFINED_PRECISION computation.
	 * 
	 * @return a new ProbabilityNumber representing the result of multiplication
	 *         of two ProbabilityNumber types.
	 */
	ProbabilityNumber multiply(ProbabilityNumber multiplicand, MathContext mc);

	/**
	 * Divide two ProbabilityNumber types and return a ProbabilityNumber
	 * representing the result of division.
	 * 
	 * @param divisor
	 * 
	 * @return a new ProbabilityNumber representing the result of division of
	 *         two ProbabilityNumber types.
	 */
	ProbabilityNumber divide(ProbabilityNumber divisor);

	/**
	 * Divide two ProbabilityNumber types and return a ProbabilityNumber
	 * representing the result of division.
	 * 
	 * @param divisor
	 * 
	 * @param mc
	 *            MathContext for DEFINED_PRECISION computation.
	 * 
	 * @return a new ProbabilityNumber representing the result of division of
	 *         two ProbabilityNumber types.
	 */
	ProbabilityNumber divide(ProbabilityNumber divisor, MathContext mc);

	/**
	 * Raise this ProbabilityNumber to an integer exponent.
	 * 
	 * @param exponent
	 * 
	 * @return a new ProbabilityNumber representing the result of
	 *         exponentiation.
	 */
	ProbabilityNumber pow(int exponent);

	/**
	 * Raise this ProbabilityNumber to an integer exponent.
	 * 
	 * @param exponent
	 * 
	 * @param mc
	 *            MathContext for DEFINED_PRECISION computation.
	 * 
	 * @return a new ProbabilityNumber representing the result of
	 *         exponentiation.
	 */
	ProbabilityNumber pow(int exponent, MathContext mc);

	/**
	 * Raise this ProbabilityNumber to a BigInteger exponent.
	 * 
	 * @param exponent
	 * 
	 * @return a new ProbabilityNumber representing the result of
	 *         exponentiation.
	 */
	ProbabilityNumber pow(BigInteger exponent);

	/**
	 * Raise this ProbabilityNumber to a BigInteger exponent.
	 * 
	 * @param exponent
	 * 
	 * @param mc
	 *            MathContext for DEFINED_PRECISION computation.
	 * 
	 * @return a new ProbabilityNumber representing the result of
	 *         exponentiation.
	 */
	ProbabilityNumber pow(BigInteger exponent, MathContext mc);

	/**
	 * Sum all the ProbabilityNumber elements constituting the iterable and
	 * check if they sum to one.
	 * 
	 * @param allProbabilities
	 * 
	 * @return true if the probabilities sum to one, false otherwise.
	 */
	Boolean sumsToOne(Iterable<ProbabilityNumber> allProbabilities);
}