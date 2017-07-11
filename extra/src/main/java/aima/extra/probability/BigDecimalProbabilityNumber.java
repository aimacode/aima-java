package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Implementation based on the underlying BigDecimal datatype. Supports higher
 * precision than DoubleProbabilityNumber (precision of result is determined by
 * operands, but can be specified to any arbitrary precision upto UNLIMITED
 * precision). The class is immutable.
 * 
 * @author Nagaraj Poti
 *
 */
public class BigDecimalProbabilityNumber extends AbstractProbabilityNumber {

	// Static members

	/**
	 * Maximum integer value that can be accomodated as exponent in the
	 * BigDecimal pow() function.
	 */
	private static final int EXPONENT_MAX = 999999999;

	/**
	 * Precision value corresponding to MathContext.UNLIMITED.
	 */
	private static final Integer UNLIMITED_PRECISION = 0;

	/**
	 * Constants of UNLIMITED precision.
	 */
	private static BigDecimal BIG_DECIMAL_ZERO = new BigDecimal(0);

	private static BigDecimal BIG_DECIMAL_ONE = new BigDecimal(1);

	// Internal fields

	private BigDecimal value;

	/**
	 * HALF_EVEN rounding mode statistically minimizes cumulative error when
	 * applied repeatedly over a sequence of calculations. Set as default
	 */
	private RoundingMode roundingMode = RoundingMode.HALF_EVEN;

	// Constructors

	/**
	 * Construct a BigDecimalProbabilityNumber from double type.
	 * BigDecimal.valueOf() builds from a double type based on its canonical
	 * representation. Precision of double type is at max 15.95 (~15).
	 * 
	 * @param value
	 *            to be assigned.
	 */
	public BigDecimalProbabilityNumber(Double value) {
		this(BigDecimal.valueOf(value), null);
	}

	/**
	 * Construct a BigDecimalProbabilityNumber from BigDecimal type.
	 * 
	 * @param value
	 *            to be assigned.
	 */
	public BigDecimalProbabilityNumber(BigDecimal value) {
		this(value, null);
	}

	/**
	 * Construct a BigDecimalProbabilityNumber from BigDecimal type.
	 * 
	 * @param value
	 *            to be assigned.
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	public BigDecimalProbabilityNumber(BigDecimal value, MathContext mc) {
		init(value, mc);
	}

	/**
	 * Construct a BigDecimalProbabilityNumber from a ProbabilityNumber type.
	 * 
	 * @param that
	 *            ProbabilityNumber.
	 */
	public BigDecimalProbabilityNumber(ProbabilityNumber that) {
		init(that.getValue(), null);
	}

	// Public methods
	// START-ProbabilityNumber

	/**
	 * @return value of BigDecimal type.
	 */
	@Override
	public BigDecimal getValue() {
		return this.value;
	}

	/**
	 * @return MathContext associated with the value.
	 */
	@Override
	public MathContext getMathContext() {
		MathContext currentMathContext = new MathContext(this.value.precision(), this.roundingMode);
		return currentMathContext;
	}

	/**
	 * Checks if the value is zero or not.
	 * 
	 * @return true if zero, false otherwise.
	 */
	@Override
	public boolean isZero() {
		boolean result = (compareBigDecimal(this.value, this.getMathContext(), BIG_DECIMAL_ZERO,
				MathContext.UNLIMITED) == 0);
		return result;
	}

	/**
	 * Checks if the value is one.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		boolean result = (compareBigDecimal(this.value, this.getMathContext(), BIG_DECIMAL_ONE,
				MathContext.UNLIMITED) == 0);
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
		boolean result = (this.compareTo(new BigDecimalProbabilityNumber(BIG_DECIMAL_ZERO, MathContext.UNLIMITED)) >= 0
				&& this.compareTo(new BigDecimalProbabilityNumber(BIG_DECIMAL_ONE, MathContext.UNLIMITED)) <= 0);
		return result;
	}

	/**
	 * Add a BigDecimalProbabilityNumber to this BigDecimalProbabilityNumber and
	 * return a new BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            the BigDecimalProbabilityNumber that is to be added to this
	 *            BigDecimalProbabilityNumber.
	 * 
	 * @return a new BigDecimalProbabilityNumber that is the result of addition.
	 */
	@Override
	public ProbabilityNumber add(ProbabilityNumber that) {
		BigDecimalProbabilityNumber addend = toInternalType(that);
		MathContext resultMathContext = getResultMathContext(this.getMathContext(), addend.getMathContext());
		ProbabilityNumber result = new BigDecimalProbabilityNumber(this.value.add(addend.value, resultMathContext));
		return result;
	}

	/**
	 * Subtract a BigDecimalProbabilityNumber from this
	 * BigDecimalProbabilityNumber and return a new BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            the BigDecimalProbabilityNumber that is to be subtracted from
	 *            this BigDecimalProbabilityNumber.
	 * 
	 * @return a new BigDecimalProbabilityNumber that is the result of
	 *         subtraction.
	 */
	@Override
	public ProbabilityNumber subtract(ProbabilityNumber that) {
		BigDecimalProbabilityNumber subtrahend = toInternalType(that);
		MathContext resultMathContext = getResultMathContext(this.getMathContext(), subtrahend.getMathContext());
		ProbabilityNumber result = new BigDecimalProbabilityNumber(
				this.value.subtract(subtrahend.value, resultMathContext));
		return result;
	}

	/**
	 * Multiply a BigDecimalProbabilityNumber with this
	 * BigDecimalProbabilityNumber and return a new BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            the BigDecimalProbabilityNumber that is to be multiplied to
	 *            this BigDecimalProbabilityNumber.
	 * 
	 * @result a new BigDecimalProbabilityNumber that is the result of
	 *         multiplication.
	 */
	@Override
	public ProbabilityNumber multiply(ProbabilityNumber that) {
		ProbabilityNumber result = this.multiply(that, null);
		return result;
	}

	/**
	 * Multiply a BigDecimalProbabilityNumber with this
	 * BigDecimalProbabilityNumber and return a new BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            the BigDecimalProbabilityNumber that is to be multiplied to
	 *            this BigDecimalProbabilityNumber.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @result a new BigDecimalProbabilityNumber that is the result of
	 *         multiplication.
	 */
	@Override
	public ProbabilityNumber multiply(ProbabilityNumber that, MathContext mc) {
		BigDecimalProbabilityNumber multiplier = toInternalType(that);
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), multiplier.getMathContext());
		}
		ProbabilityNumber result = new BigDecimalProbabilityNumber(
				this.value.multiply(multiplier.value, resultMathContext));
		return result;
	}

	/**
	 * Divide a BigDecimalProbabilityNumber with this
	 * BigDecimalProbabilityNumber and return a new BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            the BigDecimalProbabilityNumber that is the divisor of this
	 *            BigDecimalProbabilityNumber.
	 * 
	 * @return a new BigDecimalProbabilityNumber that is the result of division.
	 */
	@Override
	public ProbabilityNumber divide(ProbabilityNumber that) {
		ProbabilityNumber result = this.divide(that, null);
		return result;
	}

	/**
	 * Divide a BigDecimalProbabilityNumber with this
	 * BigDecimalProbabilityNumber and return a new BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            the BigDecimalProbabilityNumber that is the divisor of this
	 *            BigDecimalProbabilityNumber.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @return a new BigDecimalProbabilityNumber that is the result of division.
	 */
	@Override
	public ProbabilityNumber divide(ProbabilityNumber that, MathContext mc) {
		BigDecimalProbabilityNumber divisor = toInternalType(that);
		MathContext resultMathContext;
		if (divisor.isZero()) {
			throw new IllegalArgumentException("Division by 0 not allowed.");
		}
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), divisor.getMathContext());
		}
		ProbabilityNumber result = new BigDecimalProbabilityNumber(this.value.divide(divisor.value, resultMathContext));
		return result;
	}

	/**
	 * Calculate the BigDecimalProbabilityNumber raised to an integer exponent.
	 * 
	 * @param exponent
	 *            of integer type.
	 * 
	 * @result a new BigDecimalProbabilityNumber that is this
	 *         BigDecimalProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(int exponent) {
		ProbabilityNumber result = this.pow(exponent, null);
		return result;
	}

	/**
	 * Calculate the BigDecimalProbabilityNumber raised to an integer exponent.
	 * 
	 * @param exponent
	 *            of integer type.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @result a new BigDecimalProbabilityNumber that is this
	 *         BigDecimalProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(int exponent, MathContext mc) {
		if (exponent > EXPONENT_MAX) {
			return this.pow(BigInteger.valueOf(exponent));
		}
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), MathContext.UNLIMITED);
		}
		ProbabilityNumber result = new BigDecimalProbabilityNumber(this.value.pow(exponent, resultMathContext));
		return result;
	}

	/**
	 * Calculate the BigDecimalProbabilityNumber raised to a BigInteger
	 * exponent.
	 * 
	 * @param exponent
	 *            of BigInteger type.
	 * 
	 * @result a new BigDecimalProbabilityNumber that is this
	 *         BigDecimalProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(BigInteger exponent) {
		ProbabilityNumber result = this.pow(exponent, null);
		return result;
	}

	/**
	 * Calculate the BigDecimalProbabilityNumber raised to a BigInteger
	 * exponent.
	 * 
	 * @param exponent
	 *            of BigInteger type.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @result a new BigDecimalProbabilityNumber that is this
	 *         BigDecimalProbabilityNumber raised to the exponent value.
	 */
	@Override
	public ProbabilityNumber pow(BigInteger exponent, MathContext mc) {
		ProbabilityNumber finalResult;
		if (exponent.compareTo(BigInteger.valueOf(EXPONENT_MAX)) <= 0) {
			int exp = exponent.intValueExact();
			finalResult = this.pow(exp);
		} else {
			MathContext resultMathContext;
			if (null != mc) {
				resultMathContext = mc;
			} else {
				resultMathContext = getResultMathContext(this.getMathContext(), MathContext.UNLIMITED);
			}
			BigInteger BIG_INTEGER_TWO = BigInteger.valueOf(2);
			BigDecimal result = BIG_DECIMAL_ONE;
			BigDecimal base = value;
			while (exponent.compareTo(BigInteger.ZERO) == 1) {
				if (exponent.mod(BIG_INTEGER_TWO).compareTo(BigInteger.ONE) == 0) {
					result = result.multiply(base, resultMathContext);
				}
				base = base.multiply(base, resultMathContext);
				exponent = exponent.divide(BIG_INTEGER_TWO);
			}
			finalResult = new BigDecimalProbabilityNumber(result);
		}
		return finalResult;
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
		BigDecimalProbabilityNumber sumOfProbabilities = new BigDecimalProbabilityNumber(BigDecimal.ZERO);
		for (ProbabilityNumber probability : allProbabilities) {
			BigDecimalProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (BigDecimalProbabilityNumber) (sumOfProbabilities.add(specificType));
		}
		return this.isOne();
	}

	// END-ProbabilityNumber

	/**
	 * Checks if argument implementing ProbabilityNumber interface is equal to
	 * the value of the current BigDecimalProbabilityNumber. The check between
	 * two ProbabilityNumbers involves a check only upto the minimum precision
	 * of the two values, i.e min(this.precision(), that.precision()).
	 * 
	 * @param that
	 *            the ProbabilityNumber type that is to be compared with this
	 *            BigDecimalProbabilityNumber.
	 *
	 * @return true if this == that upto precision value specified by
	 *         min(this.precision(), that.precision()), false otherwise.
	 */
	@Override
	public boolean equals(Object that) {
		boolean result;
		if (!(that instanceof ProbabilityNumber)) {
			result = false;
		} else {
			BigDecimalProbabilityNumber specificType = toInternalType((ProbabilityNumber) that);
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
		BigDecimalProbabilityNumber second = toInternalType(that);
		int result = compareBigDecimal(this.value, this.getMathContext(), second.value, second.getMathContext());
		return result;
	}

	// Private methods

	/**
	 * Constructor invoked initialization method.
	 * 
	 * @param value
	 *            to be assigned to BigDecimalProbabilityNumber value.
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	private void init(BigDecimal value, MathContext mc) {
		if (null == value) {
			throw new IllegalArgumentException("A probability value must be specified.");
		}
		checkValidityOfArguments(value);
		if (null != mc) {
			this.value = new BigDecimal(value.toString(), mc);
			this.roundingMode = mc.getRoundingMode();
		} else {
			this.value = value;
		}
	}

	/**
	 * Check if arguments satisfy criteria to initialize
	 * BigDecimalProbabilityNumber.
	 * 
	 * @param value
	 *            to be assigned to BigDecimalProbabilityNumber value.
	 */
	private static void checkValidityOfArguments(BigDecimal value) {
		if (value.compareTo(BIG_DECIMAL_ZERO) == -1 || value.compareTo(BIG_DECIMAL_ONE) == 1) {
			throw new IllegalArgumentException("Probability value must be in the interval [0,1].");
		}
	}

	/**
	 * Covert other implementations of the ProbabilityNumber interface to a
	 * BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            BigDecimalProbabilityNumber.
	 */
	private BigDecimalProbabilityNumber toInternalType(ProbabilityNumber that) {
		BigDecimalProbabilityNumber converted;
		if (that instanceof BigDecimalProbabilityNumber) {
			converted = (BigDecimalProbabilityNumber) that;
		} else {
			converted = new BigDecimalProbabilityNumber(that.getValue());
		}
		return converted;
	}

	/**
	 * Compare two BigDecimal values upto minimum precision.
	 * 
	 * @param first
	 *            of BigDecimal type.
	 * @param second
	 *            of BigDecimal type.
	 * 
	 * @return 1 if first > second, 0 if first == second, -1 if first < second.
	 */
	private int compareBigDecimal(BigDecimal first, MathContext mc1, BigDecimal second, MathContext mc2) {
		int result;
		if (mc1.getPrecision() == mc2.getPrecision()) {
			result = first.compareTo(second);
		} else {
			int minPrecision = getMinPrecision(mc1.getPrecision(), mc2.getPrecision());
			if (mc1.getPrecision() == minPrecision) {
				BigDecimal valueB = new BigDecimal(second.unscaledValue(), second.scale(),
						new MathContext(minPrecision, mc2.getRoundingMode()));
				result = first.compareTo(valueB);
			} else {
				BigDecimal valueA = new BigDecimal(first.unscaledValue(), first.scale(),
						new MathContext(minPrecision, mc1.getRoundingMode()));
				result = valueA.compareTo(second);
			}
		}
		return result;
	}

	/**
	 * Compare two MathContext objects corresponding to operands and create the
	 * MathContext object associated with the result. Return MathContext set to
	 * minimum of two precision values plus one. If both values are 0
	 * (UNLIMITED_PRECISION), then 0 is returned. If either one of the precision
	 * values is 0, then the other value plus one is returned.
	 * 
	 * @param mcA
	 *            MathContext object of this instance.
	 * @param mcB
	 *            second MathContext object.
	 * 
	 * @return resultMathContext with precision set to (min(precisionA,
	 *         precisionB) + 1) or 0 (if both precision values are 0). The
	 *         RoundingMode is set to that of mcA.
	 */
	private MathContext getResultMathContext(MathContext mcA, MathContext mcB) {
		MathContext resultMathContext;
		int minPrecision = getMinPrecision(mcA.getPrecision(), mcB.getPrecision());
		if (minPrecision == UNLIMITED_PRECISION) {
			resultMathContext = new MathContext(minPrecision, mcA.getRoundingMode());
		} else {
			resultMathContext = new MathContext(minPrecision + 1, mcA.getRoundingMode());
		}
		return resultMathContext;
	}

	/**
	 * Return the minimum of two precision values. If either of the precision
	 * values is 0, then the other value is returned.
	 * 
	 * @param precisionA
	 * @param precisionB
	 * 
	 * @return min(precisionA, precisionB) if both precisionA and precisionB are
	 *         non-zero, otherwise max(precisionA, precisionB).
	 */
	private int getMinPrecision(int precisionA, int precisionB) {
		int result;
		if (precisionA == UNLIMITED_PRECISION || precisionB == UNLIMITED_PRECISION) {
			result = Math.max(precisionA, precisionB);
		} else {
			result = Math.min(precisionA, precisionB);
		}
		return result;
	}
}