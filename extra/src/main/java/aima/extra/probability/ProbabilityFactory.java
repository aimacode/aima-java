package aima.extra.probability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Factory class to generate ProbabilityNumber types.
 * 
 * @author Nagaraj Poti
 *
 */
public class ProbabilityFactory {

	/**
	 * Construct DoubleProbabilityNumber objects.
	 */
	public static DoubleProbabilityNumber doubleValueOf(double v) {
		return new DoubleProbabilityNumber(v);
	}

	public static DoubleProbabilityNumber doubleValueOf(BigDecimal v) {
		return new DoubleProbabilityNumber(v);
	}

	/**
	 * Construct BigDecimalProbabilityNumber objects.
	 */
	public static BigDecimalProbabilityNumber decimalValueOf(double v) {
		return new BigDecimalProbabilityNumber(v);
	}

	public static BigDecimalProbabilityNumber decimalValueOf(BigDecimal v) {
		return new BigDecimalProbabilityNumber(v);
	}

	/**
	 * @param v of BigDecimal type.
	 * 
	 * @param maximum precision of the ProbabilityNumber.
	 */
	public static BigDecimalProbabilityNumber decimalValueOf(BigDecimal v, int precision) {
		MathContext mc = new MathContext(precision, RoundingMode.HALF_EVEN);
		return new BigDecimalProbabilityNumber(v, mc);
	}

	/**
	 * Construct LogProbabilityNumber objects.
	 */
	public static LogProbabilityNumber logValueOf(double v) {
		return new LogProbabilityNumber(v);
	}

	public static LogProbabilityNumber logValueOf(BigDecimal v) {
		return new LogProbabilityNumber(v);
	}

	/**
	 * Construct RationalProbabilityNumber objects.
	 */
	public static RationalProbabilityNumber rationalValueOf(BigInteger numerator, BigInteger denominator) {
		return new RationalProbabilityNumber(numerator, denominator);
	}

	public static RationalProbabilityNumber rationalValueOf(BigInteger numerator) {
		return new RationalProbabilityNumber(numerator);
	}

	public static RationalProbabilityNumber rationalValueOf(long numerator, long denominator) {
		return new RationalProbabilityNumber(numerator, denominator);
	}

	public static RationalProbabilityNumber rationalValueOf(long numerator) {
		return new RationalProbabilityNumber(numerator);
	}

	public static RationalProbabilityNumber rationalValueOf(BigDecimal v) {
		return new RationalProbabilityNumber(v);
	}

	/**
	 * @param v of BigDecimal type.
	 * 
	 * @param maximum precision of the ProbabilityNumber.
	 */
	public static RationalProbabilityNumber rationalValueOf(BigDecimal v, int precision) {
		MathContext mc = new MathContext(precision, RoundingMode.HALF_EVEN);
		return new RationalProbabilityNumber(v, mc);
	}
}