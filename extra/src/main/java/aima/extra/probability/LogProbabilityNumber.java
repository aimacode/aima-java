package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Logspace implementation that allows double type to support a larger range of
 * values yet retain it's performance more or less. The log function utilised is
 * the natural log due to the convenience of use of its complement exp function.
 * The class is immutable.
 * 
 * @author Nagaraj Poti
 *
 */
public class LogProbabilityNumber implements ProbabilityNumber, Comparable<ProbabilityNumber> {

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
	 * Construct a LogProbabilityNumber from a primitive double type.
	 * 
	 * @param value
	 *            to be assigned to LogProbabilityNumber value.
	 */
	public LogProbabilityNumber(double value) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException();
		}
		this.value = Math.log(value);
	}

	/**
	 * Construct a DoubleProbabilityNumber from a BigDecimal type (loss of
	 * precision possible when converting from a BigDecimal to double type).
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 */
	public LogProbabilityNumber(BigDecimal value) {
		if (null == value || value.compareTo(new BigDecimal(0)) == -1 || value.compareTo(new BigDecimal(1)) == 1) {
			// Probability value must between 0 and 1
			throw new IllegalArgumentException();
		}
		this.value = Math.log(value.doubleValue());
	}

	// Private constructor

	/**
	 * For internal use only. Construct a LogProbabilityNumber from an already
	 * log computed value.
	 * 
	 * @param value
	 *            to be assigned to the LogProbabilityNumber value.
	 * 
	 * @param isLogValue
	 *            true.
	 */
	private LogProbabilityNumber(double value, boolean isLogValue) {
		this.value = value;
	}

	// Private methods

	/**
	 * Covert other implementations of the ProbabilityNumber interface to a
	 * LogProbabilityNumber.
	 * 
	 * @param that
	 *            LogProbabilityNumber.
	 */
	private LogProbabilityNumber toInternalType(ProbabilityNumber that) {
		if (that instanceof LogProbabilityNumber) {
			return (LogProbabilityNumber) that;
		}
		return new LogProbabilityNumber(that.getValue());
	}

	// Public methods

	/**
	 * @return value of BigDecimal type.
	 */
	@Override
	public BigDecimal getValue() {
		return new BigDecimal(Math.exp(value));
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
	 * Checks if the value is zero or not. The first check is an absolute check
	 * upto MAX_PRECISION digits. If the check fails, an approximation check
	 * based on the specified DEFAULT_ROUNDING_THRESHOLD is made.
	 * 
	 * @return true if zero, false otherwise.
	 */
	@Override
	public boolean isZero() {
		if (value.isInfinite()) {
			return true;
		}
		// Convert value from logspace to decimal and perform approximation
		// check with 0
		return Math.abs(Math.exp(value) - 0) <= DEFAULT_ROUNDING_THRESHOLD;
	}

	/**
	 * Checks if the value is one. The first check is an absolute check upto
	 * MAX_PRECISION digits. If the check fails, an approximation check based on
	 * the specified DEFAULT_ROUNDING_THRESHOLD is made.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		if (value == 0) {
			return true;
		}
		// Logspace value approximation checked against 0
		return Math.abs(value - 0) <= DEFAULT_ROUNDING_THRESHOLD;
	}

	/**
	 * Checks if argument implementing ProbabilityNumber interface is equal to
	 * the value of the current LogProbabilityNumber. The first check is an
	 * absolute check upto MAX_PRECISION digits. If the check fails, an
	 * approximation check based on the specified DEFAULT_ROUNDING_THRESHOLD is
	 * made.
	 * 
	 * @param that
	 *            the ProbabilityNumber type that is to be compared with this
	 *            LogProbabilityNumber.
	 *
	 * @return true if this == that, false otherwise.
	 */
	@Override
	public boolean equals(ProbabilityNumber that) {
		return (this.compareTo(that) == 0);
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
		LogProbabilityNumber second = toInternalType(that);
		if (value == second.value) {
			return 0;
		}
		boolean result = Math.abs(value - second.value) <= DEFAULT_ROUNDING_THRESHOLD;
		if (result == true) {
			return 0;
		} else {
			return ((value > second.value) ? 1 : -1);
		}
	}

	/**
	 * Add a LogProbabilityNumber to this LogProbabilityNumber and return a new
	 * LogProbabilityNumber. <br/>
	 * 
	 * log(x + y) = log(x) + log(1 + e<sup>(log(y) - log(x))</sup>)
	 * 
	 * @param that
	 *            the LogProbabilityNumber that is to be added to this
	 *            LogProbabilityNumber.
	 * 
	 * @return a new LogProbabilityNumber that is the result of addition.
	 */
	@Override
	public ProbabilityNumber add(ProbabilityNumber that) {
		LogProbabilityNumber addend = toInternalType(that);
		return new LogProbabilityNumber(value + Math.log(1 + Math.exp(addend.value - value)), true);
	}

	/**
	 * Subtract a LogProbabilityNumber from this LogProbabilityNumber and return
	 * a new LogProbabilityNumber.
	 * 
	 * log(x - y) = log(x) + log(1 - e<sup>(log(y) - log(x))</sup>)
	 * 
	 * @param that
	 *            the LogProbabilityNumber that is to be subtracted from this
	 *            LogProbabilityNumber.
	 * 
	 * @return a new LogProbabilityNumber that is the result of subtraction.
	 */
	@Override
	public ProbabilityNumber subtract(ProbabilityNumber that) {
		LogProbabilityNumber subtrahend = toInternalType(that);
		return new LogProbabilityNumber(value + Math.log(1 - Math.exp(subtrahend.value - value)), true);
	}

	/**
	 * Multiply a LogProbabilityNumber with this LogProbabilityNumber and return
	 * a new LogProbabilityNumber.
	 * 
	 * log(x * y) = log(x) + log(y)
	 * 
	 * @param that
	 *            the LogProbabilityNumber that is to be multiplied to this
	 *            LogProbabilityNumber.
	 * 
	 * @result a new LogProbabilityNumber that is the result of multiplication.
	 */
	@Override
	public ProbabilityNumber multiply(ProbabilityNumber that) {
		LogProbabilityNumber multiplier = toInternalType(that);
		return new LogProbabilityNumber(value + multiplier.value, true);
	}

	/**
	 * Divide a LogProbabilityNumber with this LogProbabilityNumber and return a
	 * new LogProbabilityNumber.
	 * 
	 * log(x / y) = log(x) - log(y)
	 * 
	 * @param that
	 *            the LogProbabilityNumber that is the divisor of this
	 *            LogProbabilityNumber.
	 * 
	 * @return a new LogProbabilityNumber that is the result of division.
	 */
	@Override
	public ProbabilityNumber divide(ProbabilityNumber that) {
		LogProbabilityNumber divisor = toInternalType(that);
		return new LogProbabilityNumber(value - divisor.value, true);
	}

	/**
	 * Calculate the LogProbabilityNumber raised to an integer exponent.
	 * 
	 * log(x<sup>b</sup>) = b * log(x)
	 * 
	 * @param exponent
	 *            of integer type.
	 * 
	 * @result a new LogProbabilityNumber that is this LogProbabilityNumber
	 *         raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(int exponent) {
		return new LogProbabilityNumber(exponent * value, true);
	}

	/**
	 * Calculate the LogProbabilityNumber raised to a BigInteger exponent. If
	 * the value of the BigInteger is greater than that representable by integer
	 * type, then the lower order 32 bits are chosen by default.
	 * 
	 * @param exponent
	 *            of BigInteger type.
	 * 
	 * @result a new LogProbabilityNumber that is this LogProbabilityNumber
	 *         raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(BigInteger exponent) {
		return new LogProbabilityNumber(exponent.intValue() * value, true);
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
		LogProbabilityNumber sumOfProbabilities = new LogProbabilityNumber(0);
		for (ProbabilityNumber probability : allProbabilities) {
			LogProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (LogProbabilityNumber) (sumOfProbabilities.add(specificType));
		}
		return this.isOne();
	}
	
	public String toString() {
		return getValue().toString();
	}
}