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
public class RationalProbabilityNumber extends ProbabilityNumber {

	// Static members

	/**
	 * Maximum integer value that can be accomodated as exponent in the
	 * Math.pow() function
	 */
	private static final int EXPONENT_MAX = Integer.MAX_VALUE;

	/**
	 * Precision value corresponding to MathContext.UNLIMITED.
	 */
	private static final Integer UNLIMITED_PRECISION = 0;

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
	private MathContext currentMathContext = new MathContext(DEFAULT_MAX_PRECISION, DEFAULT_PRECISION_ROUNDING_MODE);

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
	 * 
	 * @param denominator
	 *            the rational value's denominator.
	 */
	public RationalProbabilityNumber(BigInteger numerator, BigInteger denominator) {
		this(numerator, denominator, null);
	}

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
	 * @param mc
	 *            MathContext of result.
	 */
	public RationalProbabilityNumber(BigInteger numerator, BigInteger denominator, MathContext mc) {
		if (numerator == null || denominator == null) {
			throw new IllegalArgumentException("The numerator or denominator may not be null.");
		}
		if (mc != null) {
			this.currentMathContext = mc;
		}
		// Normalize the numerator and the denominator
		normalize(numerator, denominator);
	}

	/**
	 * Construct a Rational from a numerator only of type BigInteger.
	 * 
	 * @param numerator
	 *            the rational's numerator.
	 */
	public RationalProbabilityNumber(BigInteger numerator) {
		this(numerator, BigInteger.ONE, null);
	}

	/**
	 * Construct a Rational from numerator and denominator of type long.
	 * 
	 * @param numerator
	 *            the rational's numerator.
	 * 
	 * @param denominator
	 *            the rational's denominator.
	 */
	public RationalProbabilityNumber(long numerator, long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator), null);
	}

	/**
	 * Constructor a Rational from a numerator only of type long.
	 * 
	 * @param numerator
	 *            the rational's numerator.
	 */
	public RationalProbabilityNumber(long numerator) {
		this(BigInteger.valueOf(numerator), BigInteger.ONE, null);
	}

	/**
	 * Construct a Rational from a BigDecimal representation of the value.
	 * 
	 * @param value
	 *            of BigDecimal type.
	 */
	public RationalProbabilityNumber(BigDecimal value) {
		this(value, null);
	}

	/**
	 * Construct a Rational from a BigDecimal representation of the value and a
	 * MathContext object.
	 * 
	 * @param value
	 *            of BigDecimal type.
	 * @param mc
	 *            MathContext associated with value.
	 */
	public RationalProbabilityNumber(BigDecimal value, MathContext mc) {
		if (null == value) {
			throw new IllegalArgumentException("A probability value must be specified.");
		}
		if (null != mc) {
			value = new BigDecimal(value.toString(), mc);
			this.currentMathContext = mc;
		}
		String strRational = value.toString();
		BigInteger sign = BigInteger.ONE;
		switch (strRational.charAt(0)) {
		case '-':
			sign = new BigInteger("-1");
			strRational = strRational.substring(1);
			break;
		case '+':
			strRational = strRational.substring(1);
			break;
		}
		int dot = strRational.indexOf(".");
		if (dot == -1) {
			BigInteger numerator = (new BigInteger(strRational)).multiply(sign);
			BigInteger denominator = BigInteger.ONE;
			normalize(numerator, denominator);
		} else {
			int zeroLength = strRational.length() - (dot + 1);
			strRational = strRational.substring(0, dot) + strRational.substring(dot + 1);
			String denominatorStr = "1";
			for (int i = 0; i < zeroLength; i++) {
				denominatorStr += "0";
			}
			BigInteger numerator = (new BigInteger(strRational)).multiply(sign);
			BigInteger denominator = new BigInteger(denominatorStr);
			normalize(numerator, denominator);
		}
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
		return numerator.divide(new BigDecimal(denominator), this.currentMathContext);
	}

	/**
	 * @return resultMathContext set to DECIMAL128 precision and HALF_EVEN
	 *         RoundingMode by default.
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
		return (this.compareTo(new RationalProbabilityNumber(BigInteger.ZERO)) == 0);
	}

	/**
	 * Checks if the value is one.
	 * 
	 * @return true if one, false otherwise.
	 */
	@Override
	public boolean isOne() {
		return (this.compareTo(new RationalProbabilityNumber(BigInteger.ONE)) == 0);
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
		return (this.compareTo(new RationalProbabilityNumber(BigInteger.ZERO)) >= 0
				&& this.compareTo(new RationalProbabilityNumber(BigInteger.ONE)) <= 0);
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
		return this.multiply(that, null);
	}

	/**
	 * Multiply a Rational to this Rational and return a new Rational.
	 * 
	 * @param that
	 *            the Rational to be multiplied with this Rational.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @return a new Rational that is the result of multiplying this with that.
	 */
	public ProbabilityNumber multiply(ProbabilityNumber that, MathContext mc) {
		RationalProbabilityNumber multiplier = toInternalType(that);
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), multiplier.getMathContext());
		}
		return new RationalProbabilityNumber(numerator.multiply(multiplier.numerator),
				denominator.multiply(multiplier.denominator), resultMathContext);
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
		return this.divide(that, null);
	}

	/**
	 * Divide a Rational with this Rational and return a new Rational.
	 * 
	 * @param that
	 *            the Rational to be divided with this Rational.
	 * 
	 * @return a new Rational that is the result of dividing this by that.
	 */
	public ProbabilityNumber divide(ProbabilityNumber that, MathContext mc) {
		RationalProbabilityNumber divisor = toInternalType(that);
		MathContext resultMathContext;
		if (divisor.isZero()) {
			throw new IllegalArgumentException("Division by 0 not allowed");
		}
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), divisor.getMathContext());
		}
		return new RationalProbabilityNumber(numerator.multiply(divisor.denominator),
				denominator.multiply(divisor.numerator), resultMathContext);
	}

	/**
	 * @param exponent
	 *            integer exponent.
	 */
	public ProbabilityNumber pow(int exponent) {
		return this.pow(exponent, null);
	}

	/**
	 * @param exponent
	 *            integer exponent.
	 * @param mc
	 *            MathContext of result.
	 */
	public ProbabilityNumber pow(int exponent, MathContext mc) {
		BigInteger numerator = this.numerator.pow(exponent);
		BigInteger denominator = this.denominator.pow(exponent);
		MathContext resultMathContext;
		if (null != mc) {
			resultMathContext = mc;
		} else {
			resultMathContext = getResultMathContext(this.getMathContext(), MathContext.UNLIMITED);
		}
		return new RationalProbabilityNumber(numerator, denominator, resultMathContext);
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
		return this.pow(exponent, null);
	}

	/**
	 * Calculate the Rational's power and return a new Rational.
	 * 
	 * @param that
	 *            exponent of type BigInteger.
	 * @param mc
	 *            MathContext of result.
	 * 
	 * @return a new Rational that is the result of raising this Rational by the
	 *         given power.
	 */
	public ProbabilityNumber pow(BigInteger exponent, MathContext mc) {
		if (exponent.compareTo(BigInteger.valueOf(EXPONENT_MAX)) <= 0) {
			int exp = exponent.intValueExact();
			return this.pow(exp);
		} else {
			MathContext resultMathContext;
			if (null != mc) {
				resultMathContext = mc;
			} else {
				resultMathContext = getResultMathContext(this.getMathContext(), MathContext.UNLIMITED);
			}
			BigInteger BIG_INTEGER_TWO = BigInteger.valueOf(2);
			ProbabilityNumber result = new RationalProbabilityNumber(BigInteger.ONE);
			ProbabilityNumber base = this;
			while (exponent.compareTo(BigInteger.ZERO) == 1) {
				if (exponent.mod(BIG_INTEGER_TWO).compareTo(BigInteger.ONE) == 0) {
					result = result.multiply(base, resultMathContext);
				}
				base = base.multiply(base, resultMathContext);
				exponent = exponent.divide(BIG_INTEGER_TWO);
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
		RationalProbabilityNumber sumOfProbabilities = new RationalProbabilityNumber(BigInteger.ZERO);
		for (ProbabilityNumber probability : allProbabilities) {
			RationalProbabilityNumber specificType = toInternalType(probability);
			sumOfProbabilities = (RationalProbabilityNumber) (sumOfProbabilities.add(specificType));
		}
		return this.isOne();
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
		if (!(that instanceof ProbabilityNumber)) {
			return false;
		}
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
		if (checkRequired) {
			checkValidityOfArguments(numerator, denominator);
		}
		int sign = signum(numerator, denominator);
		if (sign == -1) {
			numerator = numerator.abs().multiply(new BigInteger("-1"));
			denominator = denominator.abs();
		} else {
			numerator = numerator.abs();
			denominator = denominator.abs();
		}
		BigInteger gcd = gcdCompute(numerator.abs().max(denominator.abs()), denominator.abs().min(numerator.abs()));
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
		while (valB.compareTo(BigInteger.ZERO) == 1) {
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
	private static int signum(BigInteger... bigIntegers) {
		int sign = 1;
		for (BigInteger val : bigIntegers) {
			sign *= val.signum();
		}
		return sign;
	}

	/**
	 * Check if arguments satisfy criteria to initialize
	 * RationalProbabilityNumber.
	 * 
	 * @param numerator
	 *            the rational value's numerator.
	 * @param denominator
	 *            the rational value's denominator.
	 */
	public static void checkValidityOfArguments(BigInteger numerator, BigInteger denominator) {
		if (signum(numerator, denominator) == -1) {
			throw new IllegalArgumentException("Probability value must be in the interval [0,1].");
		} else if (denominator.compareTo(BigInteger.ZERO) == 0) {
			throw new IllegalArgumentException("Denominator cannot be zero.");
		} else if (numerator.abs().compareTo(denominator.abs()) == 1) {
			throw new IllegalArgumentException("Probability value must be in the interval [0,1].");
		}
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
	 * 
	 * @param mcB
	 *            second MathContext object.
	 * 
	 * @return resultMathContext with precision set to (min(precisionA,
	 *         precisionB) + 1) or 0 (if both precision values are 0). The
	 *         RoundingMode is set to that of mcA.
	 */
	private MathContext getResultMathContext(MathContext mcA, MathContext mcB) {
		int minPrecision = getMinPrecision(mcA.getPrecision(), mcB.getPrecision());
		if (minPrecision == UNLIMITED_PRECISION) {
			return new MathContext(minPrecision, mcA.getRoundingMode());
		} else {
			return new MathContext(minPrecision + 1, mcA.getRoundingMode());
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
		if (precisionA == UNLIMITED_PRECISION || precisionB == UNLIMITED_PRECISION) {
			return Math.max(precisionA, precisionB);
		} else {
			return Math.min(precisionA, precisionB);
		}
	}
}