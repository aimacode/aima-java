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
public class BigDecimalProbabilityNumber implements ProbabilityNumber {

	// Static members

	/**
	 * Maximum integer value that can be accomodated as exponent in the
	 * BigDecimal pow() function.
	 */
	private static final int EXPONENT_MAX = 999999999;

	/**
	 * Global precision setting for operations. Set to null by default but can
	 * be overriden to ensure all computation results are specified to this
	 * precision.
	 */
	private static MathContext ARBITRARY_PRECISION = null;

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
	public BigDecimalProbabilityNumber(double value) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException("Probability value must be between 0 and 1");
		}
		this.value = BigDecimal.valueOf(value);
	}

	/**
	 * Construct a BigDecimalProbabilityNumber from BigDecimal type.
	 * 
	 * @param value
	 *            to be assigned.
	 * 
	 * @param mc
	 *            MathContext to be associated with value.
	 */
	public BigDecimalProbabilityNumber(BigDecimal value, MathContext mc) {
		if (value.compareTo(BIG_DECIMAL_ZERO) == -1 || value.compareTo(BIG_DECIMAL_ONE) == 1) {
			throw new IllegalArgumentException("Probability value must be between 0 and 1");
		} else if (null == mc) {
			this.value = value;
		} else {
			this.value = new BigDecimal(value.toString(), mc);
			this.roundingMode = mc.getRoundingMode();
		}
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

	// Public methods

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
		return new MathContext(this.value.precision(), this.roundingMode);
	}

	/**
	 * Checks if the value is zero or not.
	 * 
	 * @return true if zero, false otherwise.
	 */
	@Override
	public boolean isZero() {
		return (compareBigDecimal(this.value, this.getMathContext(), BIG_DECIMAL_ZERO, MathContext.UNLIMITED) == 0);
	}

	/**
	 * Checks if the value is one.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		return (compareBigDecimal(this.value, this.getMathContext(), BIG_DECIMAL_ONE, MathContext.UNLIMITED) == 0);
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
		return (this.compareTo(new BigDecimalProbabilityNumber(BIG_DECIMAL_ZERO, MathContext.UNLIMITED)) >= 0
				&& this.compareTo(new BigDecimalProbabilityNumber(BIG_DECIMAL_ONE, MathContext.UNLIMITED)) <= 0);
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
		return new BigDecimalProbabilityNumber(this.value.add(addend.value, resultMathContext));
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
		return new BigDecimalProbabilityNumber(this.value.subtract(subtrahend.value, resultMathContext));
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
		BigDecimalProbabilityNumber multiplier = toInternalType(that);
		MathContext resultMathContext = getResultMathContext(this.getMathContext(), multiplier.getMathContext());
		return new BigDecimalProbabilityNumber(this.value.multiply(multiplier.value, resultMathContext));
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
		BigDecimalProbabilityNumber divisor = toInternalType(that);
		if (divisor.isZero()) {
			throw new IllegalArgumentException("Division by 0 not allowed");
		}
		MathContext resultMathContext = getResultMathContext(this.getMathContext(), divisor.getMathContext());
		return new BigDecimalProbabilityNumber(this.value.divide(divisor.value, resultMathContext));
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
		if (exponent > EXPONENT_MAX) {
			return pow(BigInteger.valueOf(exponent));
		}
		MathContext resultMathContext = getResultMathContext(this.getMathContext(), MathContext.UNLIMITED);
		return new BigDecimalProbabilityNumber(this.value.pow(exponent, resultMathContext));
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
		if (exponent.compareTo(BigInteger.valueOf(EXPONENT_MAX)) <= 0) {
			int exp = exponent.intValueExact();
			return pow(exp);
		} else {
			MathContext resultMathContext = getResultMathContext(this.getMathContext(), MathContext.UNLIMITED);
			BigDecimal result = BIG_DECIMAL_ONE;
			BigDecimal base = value;
			while (exponent.compareTo(BigInteger.valueOf(0)) == 1) {
				if (exponent.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(1)) == 0) {
					result = result.multiply(base, resultMathContext);
				}
				base = base.multiply(base, resultMathContext);
				exponent = exponent.divide(BigInteger.valueOf(2));
			}
			return new BigDecimalProbabilityNumber(result);
		}
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
		BigDecimalProbabilityNumber sumOfProbabilities = new BigDecimalProbabilityNumber(0);
		for (ProbabilityNumber probability : allProbabilities) {
			BigDecimalProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (BigDecimalProbabilityNumber) (sumOfProbabilities.add(specificType));
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
		ARBITRARY_PRECISION = mc;
	}

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
		BigDecimalProbabilityNumber specificType = toInternalType((ProbabilityNumber) that);
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
		BigDecimalProbabilityNumber second = toInternalType(that);
		return compareBigDecimal(this.value, this.getMathContext(), second.value, second.getMathContext());
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
	 * BigDecimalProbabilityNumber.
	 * 
	 * @param that
	 *            BigDecimalProbabilityNumber.
	 */
	private BigDecimalProbabilityNumber toInternalType(ProbabilityNumber that) {
		if (that instanceof BigDecimalProbabilityNumber) {
			return (BigDecimalProbabilityNumber) that;
		}
		return new BigDecimalProbabilityNumber(that.getValue());
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
		if (mc1.getPrecision() == mc2.getPrecision()) {
			return first.compareTo(second);
		}
		int minPrecision = getMinPrecision(mc1.getPrecision(), mc2.getPrecision());
		if (mc1.getPrecision() == minPrecision) {
			BigDecimal valueB = new BigDecimal(second.toString(), new MathContext(minPrecision, mc2.getRoundingMode()));
			return first.compareTo(valueB);
		} else {
			BigDecimal valueA = new BigDecimal(first.toString(), new MathContext(minPrecision, mc1.getRoundingMode()));
			return valueA.compareTo(second);
		}
	}

	/**
	 * Compare two MathContext objects corresponding to operands and create the
	 * MathContext object associated with the result. If ARBITRARY_PRECISION is
	 * overriden, it takes precedence and is returned back. Otherwise return
	 * MathContext set to minimum of two precision values plus one. If both
	 * values are 0, then 0 is returned. If either one of the precision values
	 * is 0, then the other value plus one is returned.
	 * 
	 * @param mcA
	 *            MathContext object of this instance.
	 * @param mcB
	 * 
	 * @return resultMathContext with precision set to (min(precisionA,
	 *         precisionB) + 1) or 0 (if both precision values are 0) or
	 *         ARBITRARY_PRECISION if not null. The RoundingMode is set to that
	 *         of mcA.
	 */
	private MathContext getResultMathContext(MathContext mcA, MathContext mcB) {
		if (null == ARBITRARY_PRECISION) {
			int minPrecision = getMinPrecision(mcA.getPrecision(), mcB.getPrecision());
			if (minPrecision == 0) {
				return new MathContext(minPrecision, mcA.getRoundingMode());
			} else {
				return new MathContext(minPrecision + 1, mcA.getRoundingMode());
			}
		} else {
			return ARBITRARY_PRECISION;
		}
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
		if (precisionA == 0 || precisionB == 0) {
			return Math.max(precisionA, precisionB);
		} else {
			return Math.min(precisionA, precisionB);
		}
	}
}
