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
public class LogProbabilityNumber implements ProbabilityNumber {

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
	private static Integer MAX_PRECISION = MathContext.DECIMAL64.getPrecision() - 1;

	/**
	 * RoundingMode.HALF_EVEN statistically minimizes cumulative error when
	 * applied repeatedly over a sequence of calculations.
	 */
	private static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

	// Internal fields

	private Double value;

	// Constructors

	/**
	 * Construct a LogProbabilityNumber from a primitive double type.
	 * 
	 * @param value
	 *            to be assigned to LogProbabilityNumber value.
	 */
	public LogProbabilityNumber(double value) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException("Probability value must be between 0 and 1");
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
			throw new IllegalArgumentException("Probability value must be between 0 and 1");
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

	// Public methods

	/**
	 * The BigDecimal value returned represents the double value represented by
	 * this class with MAX_PRECISION.
	 * 
	 * @return value of BigDecimal type.
	 */
	@Override
	public BigDecimal getValue() {
		return new BigDecimal(Math.exp(this.value), this.getMathContext());
	}

	/**
	 * @return MathContext set to DECIMAL64 - 1 (15 digits) precision
	 *         and HALF_EVEN RoundingMode by default.
	 */
	@Override
	public MathContext getMathContext() {
		return new MathContext(MAX_PRECISION, ROUNDING_MODE);
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
		return (compareLog(this.value, Double.NEGATIVE_INFINITY) == 0);
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
		return (compareLog(this.value, 0.0) == 0);
	}

	/**
	 * Checks if the probability value represented is valid i.e it falls within
	 * the range [0, 1]. It is possible for operations on ProbabilityNumber
	 * instances to cause the result to either overflow or underflow the range
	 * [0, 1].
	 * 
	 * @return true if a valid probability value, false otherwise.
	 */
	@Override
	public boolean isValid() {
		return (compareLog(this.value, Double.NEGATIVE_INFINITY) >= 0 && compareLog(this.value, 0.0) <= 0);
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
		return new LogProbabilityNumber(this.value + Math.log(1 + Math.exp(addend.value - this.value)), true);
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
		return new LogProbabilityNumber(this.value + Math.log(1 - Math.exp(subtrahend.value - this.value)), true);
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
		return new LogProbabilityNumber(this.value + multiplier.value, true);
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
		return new LogProbabilityNumber(this.value - divisor.value, true);
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
		return new LogProbabilityNumber(exponent * this.value, true);
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
		return new LogProbabilityNumber(exponent.intValue() * this.value, true);
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
	
	/**
	 * Override the precision of ProbabilityNumber instances returned as a
	 * result of performing operations.
	 * 
	 * @param mc
	 */
	@Override
	public void overrideComputationPrecisionGlobally(MathContext mc) {
		if (mc.getPrecision() > 15) {
			throw new IllegalArgumentException("Maximum precision possible for LogProbabilityNumber is 15");
		}
		MAX_PRECISION = mc.getPrecision();
		ROUNDING_MODE = mc.getRoundingMode();
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
	public boolean equals(Object that) {
		LogProbabilityNumber specificType = toInternalType((ProbabilityNumber) that);
		return (this.compareTo(specificType) == 0);
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
		LogProbabilityNumber specificType = toInternalType(that);
		return compareLog(this.value, specificType.value);

	}

	/**
	 * @return string representation of value.
	 */
	public String toString() {
		return getValue().toString();
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

	/**
	 * Compare two double type values for equality. The first check is an
	 * absolute check upto DEFAULT_MAX_PRECISION digits. If the check fails, an
	 * approximation check based on the specified DEFAULT_ROUNDING_THRESHOLD is
	 * made.
	 * 
	 * @param first
	 *            value of Double type.
	 * @param second
	 *            value of Double type.
	 * 
	 * @return 1 if first > second, 0 if first == second, -1 if first < second.
	 */
	private int compareLog(Double first, Double second) {
		boolean result;
		if (first == second) {
			return 0;
		} else if (first.isInfinite()) {
			// Convert value from logspace to decimal and perform approximation
			// check with 0
			result = (Math.abs(Math.exp(second) - 0) <= DEFAULT_ROUNDING_THRESHOLD);
		} else if (second.isInfinite()) {
			result = Math.abs(Math.exp(first) - 0) <= DEFAULT_ROUNDING_THRESHOLD;
		} else {
			result = Math.abs(first - second) <= DEFAULT_ROUNDING_THRESHOLD;
		}
		if (result == true) {
			return 0;
		} else {
			return ((first > second) ? 1 : -1);
		}
	}
}