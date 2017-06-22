package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Rational supports arbitrary precision arithmetic operations. Operations
 * supported are addition, subtraction, multiplication, division and
 * exponentiation. The results are computed to exact precision. Internally
 * numbers are represented by their fractional components (numerator and
 * denominator) and they are converted to decimal format only when explicitly
 * required. The class is immutable.
 * 
 * @author Nagaraj Poti
 *
 */
public class RationalProbabilityNumber implements ProbabilityNumber {

	// Static members

	/**
	 * Maximum integer value that can be accomodated as exponent in the
	 * Math.pow() function
	 */
	private static final int EXPONENT_MAX = Integer.MAX_VALUE;

	private static final BigInteger BIG_INTEGER_ONE = BigInteger.ONE;

	private static final BigInteger BIG_INTEGER_ZERO = BigInteger.ZERO;

	/**
	 * IEEE 754R Decimal128 format - Precision of 34 digits and a rounding mode
	 * of HALF_EVEN. HALF_EVEN rounding mode statistically minimizes cumulative
	 * error when applied repeatedly over a sequence of calculations.
	 */
	private static final Integer DEFAULT_MAX_PRECISION = MathContext.DECIMAL128.getPrecision();

	private static final RoundingMode DEFAULT_PRECISION_ROUNDING_MODE = MathContext.DECIMAL128.getRoundingMode();

	/**
	 * MathContext of BigDecimal value returned by getValue().
	 */
	private static MathContext resultMathContext = new MathContext(DEFAULT_MAX_PRECISION,
			DEFAULT_PRECISION_ROUNDING_MODE);

	// Internal fields

	/**
	 * Numerator. Numerator may be positive or zero. It cannot be negative as
	 * there is no notion of negative probability.
	 */
	private BigInteger numerator;

	/**
	 * Denominator. Denominator can only be positive. It can never be negative
	 * or zero.
	 */
	private BigInteger denominator;

	// Constructors

	/**
	 * Construct a Rational from numerator and denominator of type BigInteger.
	 * The numerator and denominator values may not be normalized (i.e they have
	 * common factors, do not follow rules for numerator and denominator values
	 * set above), and thus must be normalized.
	 * 
	 * @param numerator
	 *            the rational value's numerator.
	 * @param denominator
	 *            the rational value's denominator.
	 */
	public RationalProbabilityNumber(BigInteger numerator, BigInteger denominator) {
		if (this.signum(numerator, denominator) == -1) {
			throw new IllegalArgumentException("Probability value must be between 0 and 1");
		} else if (null != denominator && denominator.compareTo(BIG_INTEGER_ZERO) == 0) {
			throw new IllegalArgumentException();
		} else if (null == denominator) {
			if (numerator.compareTo(BIG_INTEGER_ONE) == 1) {
				throw new IllegalArgumentException("Probability value must be between 0 and 1");
			}
			this.numerator = numerator;
			// Default denominator value is set to 1 if null
			this.denominator = BIG_INTEGER_ONE;
		} else {
			if (numerator.compareTo(denominator) == 1) {
				throw new IllegalArgumentException("Probability value must be between 0 and 1");
			}
			this.numerator = numerator;
			this.denominator = denominator;
			// Normalize the numerator and the denominator
			normalize(numerator, denominator);
		}
	}

	/**
	 * Construct a Rational from a numerator only of type BigInteger.
	 * 
	 * @param numerator
	 *            the rational's numerator.
	 */
	public RationalProbabilityNumber(BigInteger numerator) {
		this(numerator, BIG_INTEGER_ONE);
	}

	/**
	 * Construct a Rational from numerator and denominator of type long.
	 * 
	 * @param numerator
	 *            the rational's numerator.
	 * @param denominator
	 *            the rational's denominator.
	 */
	public RationalProbabilityNumber(long numerator, long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Constructor a Rational from a numerator only of type long.
	 * 
	 * @param numerator
	 *            the rational's numerator.
	 */
	public RationalProbabilityNumber(long numerator) {
		this(BigInteger.valueOf(numerator), BIG_INTEGER_ONE);
	}

	/**
	 * Construct a Rational from a BigDecimal representation of the value.
	 * 
	 * @param value
	 *            of BigDecimal type.
	 */
	public RationalProbabilityNumber(BigDecimal value) {
		if (null == value || value.compareTo(new BigDecimal(0)) == -1 || value.compareTo(new BigDecimal(1)) == 1) {
			throw new IllegalArgumentException("Probability value must be between 0 and 1");
		}
		String strRational = value.toString();
		int dot = strRational.indexOf(".");
		if (dot == -1) {
			normalize(new BigInteger(strRational), BIG_INTEGER_ONE);
		} else {
			int zeroLength = strRational.length() - (dot + 1);
			strRational = strRational.substring(0, dot) + strRational.substring(dot + 1);
			String denominatorStr = "1";
			for (int i = 0; i < zeroLength; i++) {
				denominatorStr += "0";
			}
			normalize(new BigInteger(strRational), new BigInteger(denominatorStr));
		}
	}

	/**
	 * Construct a Rational from a BigDecimal representation of the value and a
	 * MathContext object.
	 * 
	 * @param value
	 *            of BigDecimal type.
	 */
	public RationalProbabilityNumber(BigDecimal value, MathContext mc) {
		this(new BigDecimal(value.toString(), mc));
	}

	// Getter methods

	public BigInteger getNumerator() {
		return numerator;
	}

	public BigInteger getDenominator() {
		return denominator;
	}

	// Public methods

	/**
	 * @return value of BigDecimal type.
	 */
	@Override
	public BigDecimal getValue() {
		BigDecimal numerator = new BigDecimal(this.numerator);
		return numerator.divide(new BigDecimal(denominator), resultMathContext);
	}

	/**
	 * @return resultMathContext set to DECIMAL128 precision and HALF_EVEN
	 *         RoundingMode by default.
	 */
	@Override
	public MathContext getMathContext() {
		return resultMathContext;
	}

	/**
	 * Checks if the value is zero or not.
	 * 
	 * @return true if zero, false otherwise.
	 */
	@Override
	public boolean isZero() {
		return (this.compareTo(new RationalProbabilityNumber(BIG_INTEGER_ZERO)) == 0);
	}

	/**
	 * Checks if the value is one.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		return (this.compareTo(new RationalProbabilityNumber(BIG_INTEGER_ONE)) == 0);
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
		return (this.compareTo(new RationalProbabilityNumber(BIG_INTEGER_ZERO)) >= 0
				&& this.compareTo(new RationalProbabilityNumber(BIG_INTEGER_ONE)) <= 0);
	}

	/**
	 * Add a Rational to this Rational and return a new Rational.
	 * 
	 * @param that
	 *            the Rational to be added to this Rational.
	 * 
	 * @return a new Rational that is the result of adding to that.
	 */
	public ProbabilityNumber add(ProbabilityNumber that) {
		RationalProbabilityNumber addend = toInternalType(that);
		return new RationalProbabilityNumber(
				numerator.multiply(addend.denominator).add(addend.numerator.multiply(denominator)),
				denominator.multiply(addend.denominator));
	}

	/**
	 * Subtract a Rational with this Rational and return a new Rational.
	 * 
	 * @param that
	 *            the Rational to be subtracted away from this Rational.
	 * 
	 * @return a new Rational that is the result of subtracting away that.
	 */
	public ProbabilityNumber subtract(ProbabilityNumber that) {
		RationalProbabilityNumber subtrahend = toInternalType(that);
		return new RationalProbabilityNumber(
				numerator.multiply(subtrahend.denominator).subtract(subtrahend.numerator.multiply(denominator)),
				denominator.multiply(subtrahend.denominator));
	}

	/**
	 * Multiply a Rational to this Rational and return a new Rational.
	 * 
	 * @param that
	 *            the Rational to be multiplied with this Rational.
	 * 
	 * @return a new Rational that is the result of multiplying this with that.
	 */
	public ProbabilityNumber multiply(ProbabilityNumber that) {
		RationalProbabilityNumber multiplier = toInternalType(that);
		return new RationalProbabilityNumber(numerator.multiply(multiplier.numerator),
				denominator.multiply(multiplier.denominator));
	}

	/**
	 * Divide a Rational with this Rational and return a new Rational.
	 * 
	 * @param that
	 *            the Rational to be divided with this Rational.
	 * 
	 * @return a new Rational that is the result of dividing this by that.
	 */
	public ProbabilityNumber divide(ProbabilityNumber that) {
		RationalProbabilityNumber divisor = toInternalType(that);
		if (divisor.isZero()) {
			throw new IllegalArgumentException("Division by 0 not allowed");
		}
		return new RationalProbabilityNumber(numerator.multiply(divisor.denominator),
				denominator.multiply(divisor.numerator));
	}

	/**
	 * @param exponent
	 *            integer exponent.
	 */
	public ProbabilityNumber pow(int exponent) {
		BigInteger numerator = this.numerator.pow(exponent);
		BigInteger denominator = this.denominator.pow(exponent);
		return new RationalProbabilityNumber(numerator, denominator);
	}

	/**
	 * Calculate the Rational's power and return a new Rational.
	 * 
	 * @param that
	 *            exponent of type BigInteger.
	 * 
	 * @return a new Rational that is the result of raising this Rational by the
	 *         given power.
	 */
	public ProbabilityNumber pow(BigInteger exponent) {
		if (exponent.compareTo(BigInteger.valueOf(EXPONENT_MAX)) <= 0) {
			int exp = exponent.intValueExact();
			return pow(exp);
		} else {
			RationalProbabilityNumber result = new RationalProbabilityNumber(BIG_INTEGER_ONE);
			RationalProbabilityNumber base = this;
			while (exponent.compareTo(BIG_INTEGER_ZERO) == 1) {
				if (exponent.mod(BigInteger.valueOf(2)).compareTo(BIG_INTEGER_ONE) == 0) {
					result = (RationalProbabilityNumber) result.multiply(base);
				}
				base = (RationalProbabilityNumber) base.multiply(base);
				exponent = exponent.divide(BigInteger.valueOf(2));
			}
			return result;
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
		RationalProbabilityNumber sumOfProbabilities = new RationalProbabilityNumber(BIG_INTEGER_ZERO);
		for (ProbabilityNumber probability : allProbabilities) {
			RationalProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (RationalProbabilityNumber) (sumOfProbabilities.add(specificType));
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
		resultMathContext = mc;
	}
	
	/**
	 * Checks if argument implementing ProbabilityNumber interface is equal to
	 * the value of the current RationalProbabilityNumber. The check is an
	 * absolute check with numerators and denominators equated.
	 * 
	 * @param that
	 *            the ProbabilityNumber type that is to be compared with this
	 *            RationalProbabilityNumber.
	 *
	 * @return true if this == that, false otherwise.
	 */
	@Override
	public boolean equals(Object that) {
		RationalProbabilityNumber specificType = toInternalType((ProbabilityNumber) that);
		return (this.compareTo(specificType) == 0);
	}

	/**
	 * Returns a negative, zero, or positive number, indicating if this object
	 * is less than, equal to, or greater than that, respectively.
	 * 
	 * @param that
	 *            of type ProbabilityNumber.
	 * 
	 * @return 1 if this > that, 0 if this == that, -1 if this < that.
	 */
	public int compareTo(ProbabilityNumber that) {
		RationalProbabilityNumber specificType = toInternalType(that);
		// Easy case: this and that have the same denominator
		if (denominator.equals(specificType.denominator))
			return numerator.compareTo(specificType.numerator);
		// Otherwise first make the denominators equal, then compare the
		// numerators
		return numerator.multiply(specificType.denominator).compareTo(denominator.multiply(specificType.numerator));
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
	 * RationalProbabilityNumber.
	 * 
	 * @param that
	 *            RationalProbabilityNumber.
	 */
	private RationalProbabilityNumber toInternalType(ProbabilityNumber that) {
		if (that instanceof RationalProbabilityNumber) {
			return (RationalProbabilityNumber) that;
		}
		return new RationalProbabilityNumber(that.getValue());
	}

	/**
	 * Store the numerator and denominator in normalized form
	 * 
	 * @param numerator
	 *            of BigInteger type.
	 * 
	 * @param denominator
	 *            of BigInteger type.
	 */
	private void normalize(BigInteger numerator, BigInteger denominator) {
		numerator = numerator.abs();
		denominator = denominator.abs();
		BigInteger gcd = gcdCompute(numerator.max(denominator), denominator.min(numerator));
		this.numerator = numerator.divide(gcd);
		this.denominator = denominator.divide(gcd);
	}

	/**
	 * Compute the Greatest Common Divisor of two numbers (valA is greater than
	 * or equal to valB).
	 * 
	 * @param valA
	 *            of BigInteger type.
	 * 
	 * @param valB
	 *            of BigInteger type.
	 * 
	 * @return gcd of valA and valB.
	 */
	private BigInteger gcdCompute(BigInteger valA, BigInteger valB) {
		while (valB.compareTo(BIG_INTEGER_ZERO) == 1) {
			BigInteger valC = valA;
			valA = valB;
			valB = valC.mod(valB);
		}
		return valA;
	}

	/**
	 * cumulative signum() of an array of BigInteger values.
	 * 
	 * @return -1 (if negative), 0 (if zero), 1 (if positive).
	 */
	private int signum(BigInteger... bigIntegers) {
		int sign = 1;
		for (BigInteger val : bigIntegers) {
			sign *= val.signum();
		}
		return sign;
	}
}
