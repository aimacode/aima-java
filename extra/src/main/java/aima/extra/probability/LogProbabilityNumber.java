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
public class LogProbabilityNumber extends AbstractProbabilityNumber {

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
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	public LogProbabilityNumber(BigDecimal value, MathContext mc) {
		if (null == value) {
			throw new IllegalArgumentException("A probability value must be specified.");
		}
		init(value.doubleValue(), mc, false);
	}

	/**
	 * Construct a LogProbabilityNumber from a ProbabilityNumber type.
	 * 
	 * @param that
	 *            ProbabilityNumber.
	 */
	public LogProbabilityNumber(ProbabilityNumber that) {
		this(that.getValue(), null);
	}

	// Private constructor

	/**
	 * For internal use only. Construct a LogProbabilityNumber from an already
	 * log computed value.
	 * 
	 * @param value
	 *            to be assigned to the LogProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 * @param isLogValue
	 *            true.
	 */
	private LogProbabilityNumber(Double value, MathContext mc, boolean isLogValue) {
		init(value, mc, isLogValue);
	}

	// Public methods
	// START-ProbabilityNumber

	/**
	 * The BigDecimal value returned represents the double value represented by
	 * this class with MAX_PRECISION.
	 * 
	 * @return value of BigDecimal type.
	 */
	@Override
	public BigDecimal getValue() {
		BigDecimal actualValue = new BigDecimal(Math.exp(this.value), this.currentMathContext);
		return actualValue;
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
		boolean result = (compareLog(this.value, Double.NEGATIVE_INFINITY) == 0);
		return result;
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
		boolean result = (compareLog(this.value, 0.0) == 0);
		return result;
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
		boolean result = (compareLog(this.value, Double.NEGATIVE_INFINITY) >= 0 && compareLog(this.value, 0.0) <= 0);
		return result;
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
		ProbabilityNumber result = this.add(that, null);
		return result;
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
	 * @param mc
	 *            MathContext of computation.
	 * 
	 * @return a new LogProbabilityNumber that is the result of addition.
	 */
	@Override
	public ProbabilityNumber add(ProbabilityNumber that, MathContext mc) {
		LogProbabilityNumber addend = toInternalType(that);
		ProbabilityNumber result;
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), addend.getMathContext());
		}
		if (this.isZero()) {
			result = new LogProbabilityNumber(addend.value, resultMathContext, true);
		} else if (addend.isZero()) {
			result = new LogProbabilityNumber(this.value, resultMathContext, true);
		} else {
			result = new LogProbabilityNumber(this.value + Math.log1p(Math.exp(addend.value - this.value)),
					resultMathContext, true);
		}
		return result;
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
		ProbabilityNumber result = this.subtract(that, null);
		return result;
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
	 * @param mc
	 *            MathContext of computation.
	 * 
	 * @return a new LogProbabilityNumber that is the result of subtraction.
	 */
	@Override
	public ProbabilityNumber subtract(ProbabilityNumber that, MathContext mc) {
		LogProbabilityNumber subtrahend = toInternalType(that);
		if (this.compareTo(subtrahend) == -1) {
			throw new IllegalArgumentException("Subtrahend must be smaller than the minuend.");
		}
		ProbabilityNumber result;
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), subtrahend.getMathContext());
		}
		if (this.isZero() && subtrahend.isZero()) {
			result = new LogProbabilityNumber(this.value, resultMathContext, true);
		} else {
			result = new LogProbabilityNumber(this.value + Math.log1p(-Math.exp(subtrahend.value - this.value)),
					resultMathContext, true);
		}
		return result;
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
		ProbabilityNumber result = this.multiply(that, null);
		return result;
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
		ProbabilityNumber result = new LogProbabilityNumber(this.value + multiplier.value, resultMathContext, true);
		return result;
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
		ProbabilityNumber result = this.divide(that, null);
		return result;
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
		ProbabilityNumber result = new LogProbabilityNumber(this.value - divisor.value, resultMathContext, true);
		return result;
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
		ProbabilityNumber result = this.pow(BigInteger.valueOf(exponent), null);
		return result;
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
		ProbabilityNumber result = this.pow(BigInteger.valueOf(exponent), mc);
		return result;
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
		ProbabilityNumber result = this.pow(exponent, null);
		return result;
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
		ProbabilityNumber result = new LogProbabilityNumber(exponent.intValue() * this.value, resultMathContext, true);
		return result;
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
	public Boolean sumsToOne(Iterable<ProbabilityNumber> allProbabilities) {
		LogProbabilityNumber sumOfProbabilities = new LogProbabilityNumber(0.0);
		for (ProbabilityNumber probability : allProbabilities) {
			LogProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (LogProbabilityNumber) (sumOfProbabilities.add(specificType));
		}
		return this.isOne();
	}

	// END-ProbabilityNumber

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
		boolean result;
		if (!(that instanceof ProbabilityNumber)) {
			result = false;
		} else {
			LogProbabilityNumber specificType = toInternalType((ProbabilityNumber) that);
			result = (this.compareTo(specificType) == 0);
		}
		return result;
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
		int result = compareLog(this.value, specificType.value);
		return result;
	}

	// Private methods

	/**
	 * Constructor invoked initialization method.
	 * 
	 * @param value
	 *            to be assigned to the LogProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 * @param isLogValue
	 *            is true if value is already a log value, otherwise false.
	 */
	private void init(Double value, MathContext mc, boolean isLogValue) {
		if (null == value) {
			throw new IllegalArgumentException("A probability value must be specified.");
		}
		checkValidityOfArguments(value, mc, isLogValue);
		if (isLogValue) {
			this.value = value;
		} else {
			this.value = Math.log(value);
		}
		if (null != mc) {
			if (mc.getPrecision() == UNLIMITED_PRECISION) {
				// Defaults MathContext setting to MAX_PRECISION
				this.currentMathContext = new MathContext(MAX_PRECISION, ROUNDING_MODE);
			} else {
				this.currentMathContext = mc;
			}
		}
	}

	/**
	 * Check if arguments satisfy criteria to initialize LogProbabilityNumber.
	 * 
	 * @param value
	 *            to be assigned to LogProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 * @param isLogValue
	 */
	private static void checkValidityOfArguments(Double value, MathContext mc, boolean isLogValue) {
		if (!isLogValue && value < 0) {
			throw new IllegalArgumentException("ProbabilityNumber must be non-negative.");
		}
		if (null != mc) {
			if (mc.getPrecision() > MAX_PRECISION) {
				throw new IllegalArgumentException("Maximum precision possible for LogProbabilityNumber is 15.");
			}
		}
	}

	/**
	 * Covert other implementations of the ProbabilityNumber interface to a
	 * LogProbabilityNumber.
	 * 
	 * @param that
	 *            LogProbabilityNumber.
	 */
	private LogProbabilityNumber toInternalType(ProbabilityNumber that) {
		LogProbabilityNumber converted;
		if (that instanceof LogProbabilityNumber) {
			converted = (LogProbabilityNumber) that;
		} else {
			converted = new LogProbabilityNumber(that.getValue());
		}
		return converted;
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
		int result;
		if (first == second) {
			result = 0;
		} else {
			boolean withinThreshold;
			if (first.isInfinite()) {
				// Convert value from logspace to decimal and perform
				// approximation check with 0
				withinThreshold = (Math.abs(Math.exp(second) - 0) <= DEFAULT_ROUNDING_THRESHOLD);
			} else if (second.isInfinite()) {
				withinThreshold = Math.abs(Math.exp(first) - 0) <= DEFAULT_ROUNDING_THRESHOLD;
			} else {
				withinThreshold = Math.abs(first - second) <= DEFAULT_ROUNDING_THRESHOLD;
			}
			if (withinThreshold) {
				result = 0;
			} else {
				result = ((first > second) ? 1 : -1);
			}
		}
		return result;
	}

	/**
	 * Compare two MathContext objects corresponding to operands and create the
	 * MathContext object associated with the result. Return MathContext set to
	 * minimum of two precision values plus one.
	 * 
	 * @param mcA
	 *            MathContext object of this instance.
	 * @param mcB
	 *            second MathContext object.
	 * 
	 * @return resultMathContext with precision set to (min(precisionA,
	 *         precisionB) + 1). The RoundingMode is set to that of mcA.
	 */
	private MathContext getResultMathContext(MathContext mcA, MathContext mcB) {
		int minPrecision = getMinPrecision(mcA.getPrecision(), mcB.getPrecision());
		MathContext resultMathContext = new MathContext(Math.min(minPrecision + 1, MAX_PRECISION),
				mcA.getRoundingMode());
		return resultMathContext;
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
		int result = Math.min(precisionA, precisionB);
		return result;
	}
}