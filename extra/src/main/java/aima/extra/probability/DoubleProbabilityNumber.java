package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A simple wrapper class for operations with the primitive Java double
 * datatype. The maximum precision achievable is that supported by double
 * datatype (approximately 15.95 digits). The double primitive type conforms to
 * the IEEE 754 standard's 64 bit double precision format (1 sign bit, 11 bits
 * for the exponent and 52 bits for the significand). Unlimited precision
 * computations default to the maximum precision achievable by double type. The
 * class is immutable.
 * 
 * @author Nagaraj Poti
 * 
 */
public class DoubleProbabilityNumber extends AbstractProbabilityNumber {

	// Static members

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
	 * Construct a DoubleProbabilityNumber from a Double type.
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 */
	public DoubleProbabilityNumber(Double value) {
		this(BigDecimal.valueOf(value), null);
	}

	/**
	 * Construct a DoubleProbabilityNumber from a Double type.
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	public DoubleProbabilityNumber(Double value, MathContext mc) {
		this(BigDecimal.valueOf(value), mc);
	}

	/**
	 * Construct a DoubleProbabilityNumber from a BigDecimal type (loss of
	 * precision possible when converting from a BigDecimal to double type).
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 */
	public DoubleProbabilityNumber(BigDecimal value) {
		this(value, null);
	}

	/**
	 * Construct a DoubleProbabilityNumber from a BigDecimal type (loss of
	 * precision possible when converting from a BigDecimal to double type).
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	public DoubleProbabilityNumber(BigDecimal value, MathContext mc) {
		init(value, mc);
	}

	/**
	 * Construct a DoubleProbabilityNumber from a ProbabilityNumber type.
	 * 
	 * @param that
	 *            ProbabilityNumber.
	 */
	public DoubleProbabilityNumber(ProbabilityNumber that) {
		init(that.getValue(), null);
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
		BigDecimal result = new BigDecimal(this.value, this.currentMathContext);
		return result;
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
	 * Checks if the value is zero or not.
	 * 
	 * @return true if zero, false otherwise.
	 */
	@Override
	public boolean isZero() {
		boolean result = (compareDouble(this.value, 0) == 0);
		return result;
	}

	/**
	 * Checks if the value is one or not.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		boolean result = (compareDouble(this.value, 1) == 0);
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
		boolean result = (compareDouble(this.value, 0) >= 0 && compareDouble(this.value, 1) <= 0);
		return result;
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
		ProbabilityNumber result = this.add(that, null);
		return result;
	}

	/**
	 * Add a DoubleProbabilityNumber to this DoubleProbabilityNumber and return
	 * a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is to be added to this
	 *            DoubleProbabilityNumber.
	 * @param mc
	 *            MathContext of computation.
	 * 
	 * @return a new DoubleProbabilityNumber that is the result of addition.
	 */
	@Override
	public ProbabilityNumber add(ProbabilityNumber that, MathContext mc) {
		DoubleProbabilityNumber addend = toInternalType(that);
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), addend.getMathContext());
		}
		ProbabilityNumber result = new DoubleProbabilityNumber(this.value + addend.value, resultMathContext);
		return result;
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
		ProbabilityNumber result = this.subtract(that, null);
		return result;
	}

	/**
	 * Subtract a DoubleProbabilityNumber from this DoubleProbabilityNumber and
	 * return a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is to be subtracted from this
	 *            DoubleProbabilityNumber.
	 * @param mc
	 *            MathContext of computation.
	 * 
	 * @return a new DoubleProbabilityNumber that is the result of subtraction
	 */
	@Override
	public ProbabilityNumber subtract(ProbabilityNumber that, MathContext mc) {
		DoubleProbabilityNumber subtrahend = toInternalType(that);
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), subtrahend.getMathContext());
		}
		ProbabilityNumber result = new DoubleProbabilityNumber(this.value - subtrahend.value, resultMathContext);
		return result;
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
		ProbabilityNumber result = this.multiply(that, null);
		return result;
	}

	/**
	 * Multiply a DoubleProbabilityNumber with this DoubleProbabilityNumber and
	 * return a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is to be multiplied to this
	 *            DoubleProbabilityNumber.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @result a new DoubleProbabilityNumber that is the result of
	 *         multiplication.
	 */
	@Override
	public ProbabilityNumber multiply(ProbabilityNumber that, MathContext mc) {
		DoubleProbabilityNumber multiplier = toInternalType(that);
		MathContext resultMathContext;
		if (null != mc) {
			checkValidityOfArguments(0.0, mc);
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), multiplier.getMathContext());
		}
		ProbabilityNumber result = new DoubleProbabilityNumber(this.value * multiplier.value, resultMathContext);
		return result;
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
		ProbabilityNumber result = this.divide(that, null);
		return result;
	}

	/**
	 * Divide a DoubleProbabilityNumber with this DoubleProbabilityNumber and
	 * return a new DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            the DoubleProbabilityNumber that is the divisor of this
	 *            DoubleProbabilityNumber.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @return a new DoubleProbabilityNumber that is the result of division.
	 */
	@Override
	public ProbabilityNumber divide(ProbabilityNumber that, MathContext mc) {
		DoubleProbabilityNumber divisor = toInternalType(that);
		MathContext resultMathContext;
		if (divisor.isZero()) {
			throw new IllegalArgumentException("Division by 0 not allowed.");
		}
		if (null != mc) {
			checkValidityOfArguments(0.0, mc);
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), divisor.getMathContext());
		}
		ProbabilityNumber result = new DoubleProbabilityNumber(this.value / divisor.value, resultMathContext);
		return result;
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
		ProbabilityNumber result = this.pow(BigInteger.valueOf(exponent), null);
		return result;
	}

	/**
	 * Calculate the DoubleProbabilityNumber raised to an integer exponent.
	 * 
	 * @param exponent
	 *            of integer type.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @result a new DoubleProbabilityNumber that is this
	 *         DoubleProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(int exponent, MathContext mc) {
		ProbabilityNumber result = this.pow(BigInteger.valueOf(exponent), mc);
		return result;
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
		ProbabilityNumber result = this.pow(exponent, null);
		return result;
	}

	/**
	 * Calculate the DoubleProbabilityNumber raised to a BigInteger exponent. If
	 * the value of the BigInteger is greater than that representable by integer
	 * type, then the lower order 32 bits are chosen by default.
	 * 
	 * @param exponent
	 *            of BigInteger type.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @result a new DoubleProbabilityNumber that is this
	 *         DoubleProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(BigInteger exponent, MathContext mc) {
		MathContext resultMathContext;
		if (null != mc) {
			checkValidityOfArguments(0.0, mc);
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(),
					new MathContext(MAX_PRECISION, ROUNDING_MODE));
		}
		ProbabilityNumber result = new DoubleProbabilityNumber(Math.pow(this.value, exponent.intValue()),
				resultMathContext);
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
		DoubleProbabilityNumber sumOfProbabilities = new DoubleProbabilityNumber(0.0);
		for (ProbabilityNumber probability : allProbabilities) {
			DoubleProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (DoubleProbabilityNumber) (sumOfProbabilities.add(specificType));
		}
		return this.isOne();
	}

	// END-ProbabilityNumber

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
	public boolean equals(Object that) {
		boolean result;
		if (!(that instanceof ProbabilityNumber)) {
			result = false;
		} else {
			DoubleProbabilityNumber second = toInternalType((ProbabilityNumber) that);
			result = (compareDouble(this.value, second.value) == 0);
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
		DoubleProbabilityNumber second = toInternalType(that);
		int result = compareDouble(this.value, second.value);
		return result;
	}

	// Private methods

	/**
	 * Constructor invoked initialization method.
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	private void init(BigDecimal value, MathContext mc) {
		if (null == value) {
			throw new IllegalArgumentException("A probability value must be specified.");
		}
		checkValidityOfArguments(value.doubleValue(), mc);
		this.value = value.doubleValue();
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
	 * Check if arguments satisfy criteria to initialize
	 * DoubleProbabilityNumber.
	 * 
	 * @param value
	 *            to be assigned to DoubleProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	private static void checkValidityOfArguments(Double value, MathContext mc) {
		if (value < 0) {
			throw new IllegalArgumentException("ProbabilityNumber must be non-negative.");
		}
		if (null != mc) {
			if (mc.getPrecision() > MAX_PRECISION) {
				throw new IllegalArgumentException("Maximum precision possible for DoubleProbabilityNumber is 15.");
			}
		}
	}

	/**
	 * Covert other implementations of the ProbabilityNumber interface to a
	 * DoubleProbabilityNumber.
	 * 
	 * @param that
	 *            DoubleProbabilityNumber.
	 */
	private DoubleProbabilityNumber toInternalType(ProbabilityNumber that) {
		DoubleProbabilityNumber converted;
		if (that instanceof DoubleProbabilityNumber) {
			converted = (DoubleProbabilityNumber) that;
		} else {
			converted = new DoubleProbabilityNumber(that.getValue());
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
	 *            value of double type.
	 * 
	 * @param second
	 *            value of double type.
	 * 
	 * @return 1 if first > second, 0 if first == second, -1 if first < second.
	 */
	private int compareDouble(double first, double second) {
		int result;
		if (first == second) {
			result = 0;
		} else {
			boolean withinThreshold = Math.abs(first - second) <= DEFAULT_ROUNDING_THRESHOLD;
			if (withinThreshold) {
				result = 0;
			} else {
				result = (first > second) ? 1 : -1;
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
	 * 
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