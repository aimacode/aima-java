package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A simple wrapper class for operations with the primitive Java double
 * datatype. The maximum precision achievable is that supported by double
 * datatype. The double primitive type conforms to the IEEE 754 standard's 64
 * bit double precision format (1 sign bit, 11 bits for the exponent and 52 bits
 * for the significand). The class is immutable.
 * 
 * @author Nagaraj Poti
 * 
 */
public class DoubleProbabilityNumber implements ProbabilityNumber, Comparable<ProbabilityNumber> {

	// Constants

	/**
	 * Default threshold for checking rounding errors.
	 */
	private static final double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

	/**
	 * Maximum precision constrained by the underlying double primitive type.
	 * According to the IEEE 754 format, double values have a precision of 15.95
	 * decimal digits. Here, max_precision is set to 15 digits by default.
	 */
	private static final Integer DEFAULT_MAX_PRECISION = MathContext.DECIMAL64.getPrecision() - 1;

	/**
	 * RoundingMode.HALF_EVEN statistically minimizes cumulative error when
	 * applied repeatedly over a sequence of calculations.
	 */
	private static final RoundingMode DEFAULT_PRECISION_ROUNDING_MODE = RoundingMode.HALF_EVEN;

	// Internal fields

	private Double value;

	/**
	 * MathContext of value.
	 */
	private static MathContext precisionMathContext = new MathContext(DEFAULT_MAX_PRECISION,
			DEFAULT_PRECISION_ROUNDING_MODE);

	// Constructors

	/**
	 * Construct a DoubleProbabilityNumber from a primitive double type.
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 */
	public DoubleProbabilityNumber(double value) {
		if (value < 0 || value > 1) {
			// Probability value must between 0 and 1
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	/**
	 * Construct a DoubleProbabilityNumber from a BigDecimal type (loss of
	 * precision possible when converting from a BigDecimal to double type).
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 */
	public DoubleProbabilityNumber(BigDecimal value) {
		if (null == value || value.compareTo(new BigDecimal(0)) == -1 || value.compareTo(new BigDecimal(1)) == 1) {
			// Probability value must between 0 and 1
			throw new IllegalArgumentException();
		}
		this.value = value.doubleValue();
	}

	// Private methods

	/**
	 * Covert other implementations of the ProbabilityNumber interface to a
	 * DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            DoubleProbabilityNumber.
	 */
	private DoubleProbabilityNumber toInternalType(ProbabilityNumber that) {
		if (that instanceof DoubleProbabilityNumber) {
			return (DoubleProbabilityNumber) that;
		}
		return new DoubleProbabilityNumber(that.getValue());
	}

	/**
	 * Compare two double type values for equality. The first check is an
	 * absolute check upto DEFAULT_MAX_PRECISION digits. If the check fails, an
	 * approximation check based on the specified DEFAULT_ROUNDING_THRESHOLD is
	 * made.
	 * 
	 * @param first
	 *            value of double type.
	 * @param second
	 *            value of double type.
	 * 
	 * @return 1 if first > second, 0 if first == second, -1 if first < second.
	 */
	private int compareDouble(double first, double second) {
		if (first == second) {
			return 0;
		}
		boolean result = Math.abs(first - second) <= DEFAULT_ROUNDING_THRESHOLD;
		if (result == true) {
			return 0;
		} else {
			return ((first > second) ? 1 : -1);
		}
	}

	// Public methods

	/**
	 * @return value of BigDecimal type.
	 */
	@Override
	public BigDecimal getValue() {
		return new BigDecimal(value, precisionMathContext);
	}

	/**
	 * @return precisionMathContext set to DECIMAL64 - 1 (15 digits) precision
	 *         and HALF_EVEN RoundingMode by default.
	 */
	@Override
	public MathContext getMathContext() {
		return precisionMathContext;
	}

	/**
	 * Checks if the value is zero or not.
	 * 
	 * @return true if zero, false otherwise.
	 */
	@Override
	public boolean isZero() {
		return (compareDouble(this.value, 0) == 0);
	}

	/**
	 * Checks if the value is one or not.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		return (compareDouble(this.value, 1) == 0);
	}

	/**
	 * Checks if argument implementing ProbabilityNumber interface is equal to
	 * the value of the current DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the ProbabilityNumber type that is to be compared with this
	 *            DoubleProbabilityNumber.
	 *
	 * @return true if this == that, false otherwise
	 */
	@Override
	public boolean equals(ProbabilityNumber that) {
		DoubleProbabilityNumber second = toInternalType(that);
		return (compareDouble(this.value, second.value) == 0);
	}

	/**
	 * Compare this with another ProbabilityNumber value (that).
	 * 
	 * @param that
	 *            of type ProbabilityNumber.
	 * 
	 * @return 1 if this > that, 0 if this == that, -1 if this < that.
	 */
	@Override
	public int compareTo(ProbabilityNumber that) {
		DoubleProbabilityNumber second = toInternalType(that);
		return compareDouble(this.value, second.value);
	}

	/**
	 * Add a DoubleProbabilityNumber to this DoubleProbabilityNumber and return
	 * a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is to be added to this
	 *            DoubleProbabilityNumber.
	 * 
	 * @return a new DoubleProbabilityNumber that is the result of addition.
	 */
	@Override
	public ProbabilityNumber add(ProbabilityNumber that) {
		DoubleProbabilityNumber addend = toInternalType(that);
		return new DoubleProbabilityNumber(value + addend.value);
	}

	/**
	 * Subtract a DoubleProbabilityNumber from this DoubleProbabilityNumber and
	 * return a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is to be subtracted from this
	 *            DoubleProbabilityNumber.
	 * 
	 * @return a new DoubleProbabilityNumber that is the result of subtraction
	 */
	@Override
	public ProbabilityNumber subtract(ProbabilityNumber that) {
		DoubleProbabilityNumber subtrahend = toInternalType(that);
		// Should value be checked for being negative? In intermediate
		// computations, values may become negative temporarily
		return new DoubleProbabilityNumber(value - subtrahend.value);
	}

	/**
	 * Multiply a DoubleProbabilityNumber with this DoubleProbabilityNumber and
	 * return a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is to be multiplied to this
	 *            DoubleProbabilityNumber.
	 * 
	 * @result a new DoubleProbabilityNumber that is the result of
	 *         multiplication.
	 */
	@Override
	public ProbabilityNumber multiply(ProbabilityNumber that) {
		DoubleProbabilityNumber multiplier = toInternalType(that);
		return new DoubleProbabilityNumber(value * multiplier.value);
	}

	/**
	 * Divide a DoubleProbabilityNumber with this DoubleProbabilityNumber and
	 * return a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is the divisor of this
	 *            DoubleProbabilityNumber.
	 * 
	 * @return a new DoubleProbabilityNumber that is the result of division.
	 */
	@Override
	public ProbabilityNumber divide(ProbabilityNumber that) {
		DoubleProbabilityNumber divisor = toInternalType(that);
		if (divisor.isZero()) {
			throw new IllegalArgumentException();
		}
		return new DoubleProbabilityNumber(value / divisor.value);
	}

	/**
	 * Calculate the DoubleProbabilityNumber raised to an integer exponent.
	 * 
	 * @param exponent
	 *            of integer type.
	 * 
	 * @result a new DoubleProbabilityNumber that is this
	 *         DoubleProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(int exponent) {
		return new DoubleProbabilityNumber(Math.pow(value, exponent));
	}

	/**
	 * Calculate the DoubleProbabilityNumber raised to a BigInteger exponent. If
	 * the value of the BigInteger is greater than that representable by integer
	 * type, then the lower order 32 bits are chosen by default.
	 * 
	 * @param exponent
	 *            of BigInteger type.
	 * 
	 * @result a new DoubleProbabilityNumber that is this
	 *         DoubleProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(BigInteger exponent) {
		return new DoubleProbabilityNumber(Math.pow(value, exponent.intValue()));
	}

	/**
	 * Sum all elements implementing ProbabilityNumber stored in an Iterable
	 * object.
	 * 
	 * @param allProbabilities
	 *            an iterable object containing elements of type
	 *            ProbabilityNumber.
	 * 
	 * @return true if sum of all elements constituting the iterable sum to one,
	 *         false otherwise.
	 */
	@Override
	public boolean sumsToOne(Iterable<ProbabilityNumber> allProbabilities) {
		DoubleProbabilityNumber sumOfProbabilities = new DoubleProbabilityNumber(0);
		for (ProbabilityNumber probability : allProbabilities) {
			DoubleProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (DoubleProbabilityNumber) (sumOfProbabilities.add(specificType));
		}
		return this.isOne();
	}

	public String toString() {
		return getValue().toString();
	}
}