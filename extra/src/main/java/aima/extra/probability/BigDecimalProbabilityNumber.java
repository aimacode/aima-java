package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Implementation based on the underlying BigDecimal datatype. Supports higher
 * precision than DoubleProbabilityNumber (DECIMAL128 precision by default, but
 * can go upto UNLIMITED). The class is immutable.
 * 
 * @author Nagaraj Poti
 *
 */
public class BigDecimalProbabilityNumber implements ProbabilityNumber, Comparable<ProbabilityNumber> {

	// Constants

	/**
	 * IEEE 754R Decimal128 format - Precision of 34 digits and a rounding mode
	 * of HALF_EVEN. HALF_EVEN rounding mode statistically minimizes cumulative
	 * error when applied repeatedly over a sequence of calculations.
	 */
	private static final Integer DEFAULT_PRECISION = MathContext.DECIMAL128.getPrecision();

	private static final RoundingMode DEFAULT_PRECISION_ROUNDING_MODE = MathContext.DECIMAL128.getRoundingMode();

	/**
	 * Maximum integer value that can be accomodated as exponent in the
	 * BigDecimal pow() function
	 */
	private static final int EXPONENT_MAX = 999999999;

	// Internal fields

	private BigDecimal value;

	/**
	 * MathContext of value.
	 */
	private MathContext precisionMathContext = new MathContext(DEFAULT_PRECISION, DEFAULT_PRECISION_ROUNDING_MODE);

	private BigDecimal BIG_DECIMAL_ZERO = new BigDecimal(0, precisionMathContext);

	private BigDecimal BIG_DECIMAL_ONE = new BigDecimal(1, precisionMathContext);

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
			throw new IllegalArgumentException();
		}
		this.value = new BigDecimal(Double.toString(value), precisionMathContext);
	}

	/**
	 * Construct a BigDecimalProbabilityNumber from BigDecimal type.
	 * 
	 * @param value
	 *            to be assigned.
	 */
	public BigDecimalProbabilityNumber(BigDecimal value) {
		if (null == value || value.compareTo(BIG_DECIMAL_ZERO) == -1 || value.compareTo(BIG_DECIMAL_ONE) == 1) {
			throw new IllegalArgumentException();
		}
		this.value = new BigDecimal(value.toString(), precisionMathContext);
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
		precisionMathContext = mc;
		setConstants();
		if (value.compareTo(BIG_DECIMAL_ZERO) == -1 || value.compareTo(BIG_DECIMAL_ONE) == 1) {
			throw new IllegalArgumentException();
		}
		this.value = new BigDecimal(value.toString(), precisionMathContext);
	}

	// Private methods

	/**
	 * Initialize constants BIG_DECIMAL_ZERO and BIG_DECIMAL_ONE according to
	 * precisionMathContext that is defined.
	 */
	private void setConstants() {
		BIG_DECIMAL_ZERO = new BigDecimal(0, precisionMathContext);
		BIG_DECIMAL_ONE = new BigDecimal(1, precisionMathContext);
	}

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
		return new BigDecimalProbabilityNumber(that.getValue(), that.getMathContext());
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
		BigDecimal valueA = new BigDecimal(first.toString(), new MathContext(minPrecision, mc1.getRoundingMode()));
		BigDecimal valueB = new BigDecimal(second.toString(), new MathContext(minPrecision, mc2.getRoundingMode()));
		return valueA.compareTo(valueB);
	}

	/**
	 * Return the minimum of two precision values. If any of the precision
	 * values is 0, the maximum value of the two is returned.
	 * 
	 * @param precisionA
	 * @param precisionB
	 * @return
	 */
	private int getMinPrecision(int precisionA, int precisionB) {
		if (precisionA == 0 || precisionB == 0) {
			return Math.max(precisionA, precisionB);
		} else {
			return Math.min(precisionA, precisionB);
		}
	}

	// Public methods

	/**
	 * @return value of BigDecimal type.
	 */
	@Override
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @return precisionMathContext used by value set to DECIMAL128 precision
	 *         and HALF_EVEN RoundingMode by default.
	 */
	@Override
	public MathContext getMathContext() {
		return precisionMathContext;
	}

	/**
	 * Checks if the value is zero or not. The first check is an absolute check
	 * upto MAX_PRECISION digits. If the check fails, an approximation check
	 * based on the specified DEFAULT_SCALE and DEFAULT_SCALE_ROUNDING_MODE is
	 * made.
	 * 
	 * @return true if zero, false otherwise.
	 */
	@Override
	public boolean isZero() {
		return (compareBigDecimal(this.value, this.precisionMathContext, BIG_DECIMAL_ZERO, this.precisionMathContext) == 0);
	}

	/**
	 * Checks if the value is one. The first check is an absolute check upto
	 * MAX_PRECISION digits. If the check fails, an approximation check based on
	 * the specified DEFAULT_SCALE and DEFAULT_SCALE_ROUNDING_MODE is made.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		return (compareBigDecimal(this.value, this.precisionMathContext, BIG_DECIMAL_ONE, this.precisionMathContext) == 0);
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
	public boolean equals(ProbabilityNumber that) {
		BigDecimalProbabilityNumber specificType = toInternalType(that);
		return (compareBigDecimal(this.value, this.precisionMathContext, specificType.getValue(), specificType.getMathContext()) == 0);
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
		return compareBigDecimal(this.value, this.precisionMathContext, second.getValue(), second.getMathContext());
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
		int minPrecision = getMinPrecision(this.precisionMathContext.getPrecision(), addend.getMathContext().getPrecision());
		MathContext resultMathContext = new MathContext(minPrecision, DEFAULT_PRECISION_ROUNDING_MODE);
		return new BigDecimalProbabilityNumber(value.add(addend.getValue(), resultMathContext), resultMathContext);
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
		int minPrecision = getMinPrecision(this.precisionMathContext.getPrecision(),
				subtrahend.getMathContext().getPrecision());
		MathContext resultMathContext = new MathContext(minPrecision, DEFAULT_PRECISION_ROUNDING_MODE);
		return new BigDecimalProbabilityNumber(value.subtract(subtrahend.getValue(), resultMathContext),
				resultMathContext);
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
		int minPrecision = getMinPrecision(this.precisionMathContext.getPrecision(),
				multiplier.getMathContext().getPrecision());
		MathContext resultMathContext = new MathContext(minPrecision, DEFAULT_PRECISION_ROUNDING_MODE);
		return new BigDecimalProbabilityNumber(value.multiply(multiplier.value, resultMathContext), resultMathContext);
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
			throw new IllegalArgumentException();
		}
		int minPrecision = getMinPrecision(this.precisionMathContext.getPrecision(), divisor.getMathContext().getPrecision());
		MathContext resultMathContext = new MathContext(minPrecision, DEFAULT_PRECISION_ROUNDING_MODE);
		return new BigDecimalProbabilityNumber(value.divide(divisor.value, resultMathContext), resultMathContext);
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
		return new BigDecimalProbabilityNumber(value.pow(exponent, precisionMathContext));
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
			BigDecimal result = BIG_DECIMAL_ONE;
			BigDecimal base = value;
			while (exponent.compareTo(BigInteger.valueOf(0)) == 1) {
				if (exponent.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(1)) == 0) {
					result = result.multiply(base, precisionMathContext);
				}
				base = base.multiply(base, precisionMathContext);
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
	
	public String toString() {
		return getValue().toString();
	}
}
