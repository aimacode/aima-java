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
public class LogProbabilityNumber extends ProbabilityNumber {

	// Constants

	/**
	 * Default threshold for checking rounding errors.
	 */
	private static final double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

	/**
	 * Precision value corresponding to MathContext.UNLIMITED.
	 */
	private static final Integer UNLIMITED_PRECISION = 0;

	/**
	 * Maximum precision constrained by the underlying double primitive type.
	 * According to the IEEE 754 format, double values have a precision of 15.95
	 * decimal digits. Here, max_precision is set to 15 digits by default.
	 */
	private static final Integer MAX_PRECISION = MathContext.DECIMAL64.getPrecision() - 1;

	/**
	 * RoundingMode.HALF_EVEN statistically minimizes cumulative error when
	 * applied repeatedly over a sequence of calculations.
	 */
	private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

	// Internal fields

	private Double value;

	private MathContext currentMathContext = new MathContext(MAX_PRECISION, ROUNDING_MODE);

	// Constructors

	/**
	 * Construct a LogProbabilityNumber from a Double type.
	 * 
	 * @param value
	 *            to be assigned to LogProbabilityNumber value.
	 */
	public LogProbabilityNumber(Double value) {
		this(BigDecimal.valueOf(value), null);
	}

	/**
	 * Construct a LogProbabilityNumber from a BigDecimal type (loss of
	 * precision possible when converting from a BigDecimal to double type).
	 * 
	 * @param value
	 *            to be assigned to LogProbabilityNumber value.
	 */
	public LogProbabilityNumber(BigDecimal value) {
		this(value, null);
	}

	/**
	 * Construct a LogProbabilityNumber from a BigDecimal type (loss of
	 * precision possible when converting from a BigDecimal to double type).
	 * 
	 * @param value
	 *            to be assigned to LogProbabilityNumber value.
	 * 
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	public LogProbabilityNumber(BigDecimal value, MathContext mc) {
		if (null == value) {
			throw new IllegalArgumentException("A probability value must be specified.");
		}
		if (checkRequired) {
			checkValidityOfArguments(value.doubleValue(), mc, false);
		}
		this.value = Math.log(value.doubleValue());
		if (null != mc) {
			this.currentMathContext = mc;
		}
	}

	// Private constructor

	/**
	 * For internal use only. Construct a LogProbabilityNumber from an already
	 * log computed value.
	 * 
	 * @param value
	 *            to be assigned to the LogProbabilityNumber value.
	 * 
	 * @param mc
	 *            MathContext to be associated with value.
	 * 
	 * @param isLogValue
	 *            true.
	 */
	private LogProbabilityNumber(Double value, MathContext mc, boolean isLogValue) {
		if (null == value) {
			throw new IllegalArgumentException("A probability value must be specified.");
		}
		if (checkRequired) {
			checkValidityOfArguments(value, mc, true);
		}
		this.value = value;
		this.currentMathContext = mc;
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
		return new BigDecimal(Math.exp(this.value), this.currentMathContext);
	}

	/**
	 * @return MathContext set to DECIMAL64 - 1 (15 digits) precision and
	 *         HALF_EVEN RoundingMode by default.
	 */
	@Override
	public MathContext getMathContext() {
		return this.currentMathContext;
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
		MathContext resultMathContext = getResultMathContext(this.getMathContext(), addend.getMathContext());
		return new LogProbabilityNumber(this.value + Math.log(1 + Math.exp(addend.value - this.value)),
				resultMathContext, true);
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
		MathContext resultMathContext = getResultMathContext(this.getMathContext(), subtrahend.getMathContext());
		return new LogProbabilityNumber(this.value + Math.log(1 - Math.exp(subtrahend.value - this.value)),
				resultMathContext, true);
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
		return this.multiply(that, null);
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
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @result a new LogProbabilityNumber that is the result of multiplication.
	 */
	@Override
	public ProbabilityNumber multiply(ProbabilityNumber that, MathContext mc) {
		LogProbabilityNumber multiplier = toInternalType(that);
		MathContext resultMathContext;
		if (null != mc) {
			checkValidityOfArguments(0.0, mc, false);
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), multiplier.getMathContext());
		}
		return new LogProbabilityNumber(this.value + multiplier.value, resultMathContext, true);
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
		return this.divide(that, null);
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
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @return a new LogProbabilityNumber that is the result of division.
	 */
	@Override
	public ProbabilityNumber divide(ProbabilityNumber that, MathContext mc) {
		LogProbabilityNumber divisor = toInternalType(that);
		MathContext resultMathContext;
		if (divisor.isZero()) {
			throw new IllegalArgumentException("Division by 0 not allowed.");
		}
		if (null != mc) {
			checkValidityOfArguments(0.0, mc, false);
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), divisor.getMathContext());
		}
		return new LogProbabilityNumber(this.value - divisor.value, resultMathContext, true);
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
		return this.pow(BigInteger.valueOf(exponent), null);
	}

	/**
	 * Calculate the LogProbabilityNumber raised to an integer exponent.
	 * 
	 * log(x<sup>b</sup>) = b * log(x)
	 * 
	 * @param exponent
	 *            of integer type.
	 * @param mc
	 *            MathContext of result;
	 * 
	 * @result a new LogProbabilityNumber that is this LogProbabilityNumber
	 *         raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(int exponent, MathContext mc) {
		return this.pow(BigInteger.valueOf(exponent), mc);
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
		return this.pow(exponent, null);
	}

	/**
	 * Calculate the LogProbabilityNumber raised to a BigInteger exponent. If
	 * the value of the BigInteger is greater than that representable by integer
	 * type, then the lower order 32 bits are chosen by default.
	 * 
	 * @param exponent
	 *            of BigInteger type.
	 * @param mc
	 *            MathContext of result;
	 * 
	 * @result a new LogProbabilityNumber that is this LogProbabilityNumber
	 *         raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(BigInteger exponent, MathContext mc) {
		MathContext resultMathContext;
		if (null != mc) {
			checkValidityOfArguments(0.0, mc, false);
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(),
					new MathContext(MAX_PRECISION, ROUNDING_MODE));
		}
		return new LogProbabilityNumber(exponent.intValue() * this.value, resultMathContext, true);
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
		LogProbabilityNumber sumOfProbabilities = new LogProbabilityNumber(0.0);
		for (ProbabilityNumber probability : allProbabilities) {
			LogProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (LogProbabilityNumber) (sumOfProbabilities.add(specificType));
		}
		return this.isOne();
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
		if (!(that instanceof ProbabilityNumber)) {
			return false;
		}
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

	/**
	 * Check if arguments satisfy criteria to initialize LogProbabilityNumber.
	 * 
	 * @param value
	 *            to be assigned to LogProbabilityNumber value.
	 * 
	 * @param mc
	 *            MathContext to be associated with value.
	 * 
	 * @param isLogValue
	 */
	public static void checkValidityOfArguments(Double value, MathContext mc, boolean isLogValue) {
		if (isLogValue && value > 0) {
			throw new IllegalArgumentException("Probability value must be in the interval [0,1].");
		} else if (!isLogValue && (value < 0 || value > 1)) {
			throw new IllegalArgumentException("Probability value must be in the interval [0,1].");
		}
		if (null != mc) {
			if (mc.getPrecision() > MAX_PRECISION) {
				throw new IllegalArgumentException("Maximum precision possible for LogProbabilityNumber is 15.");
			}
			if (mc.getPrecision() == UNLIMITED_PRECISION) {
				throw new IllegalArgumentException("LogProbabilityNumber does not support unlimited precision.");
			}
		}
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

	/**
	 * Compare two MathContext objects corresponding to operands and create the
	 * MathContext object associated with the result. Return MathContext set to
	 * minimum of two precision values plus one.
	 * 
	 * @param mcA
	 *            MathContext object of this instance.
	 * 
	 * @param mcB
	 *            second MathContext object.
	 * 
	 * @return resultMathContext with precision set to (min(precisionA,
	 *         precisionB) + 1). The RoundingMode is set to that of mcA.
	 */
	private MathContext getResultMathContext(MathContext mcA, MathContext mcB) {
		int minPrecision = getMinPrecision(mcA.getPrecision(), mcB.getPrecision());
		return new MathContext(Math.min(minPrecision + 1, MAX_PRECISION), mcA.getRoundingMode());
	}

	/**
	 * Return the minimum of two precision values.
	 * 
	 * @param precisionA
	 * @param precisionB
	 * 
	 * @return min(precisionA, precisionB)
	 */
	private int getMinPrecision(int precisionA, int precisionB) {
		return Math.min(precisionA, precisionB);
	}
}