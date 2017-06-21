package aima.test.unit.probability;

import aima.extra.probability.*;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Test;

public class RationalTypeTest {

	@Test
	public void test() {
		
		double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

		// Constructors with double parameter type
		
		// Throws IllegalArgumentException
		// ProbabilityNumber testValue1 = ProbabilityFactory.rationalValueOf(new BigDecimal(4.0));
		// ProbabilityNumber testValue2 = ProbabilityFactory.rationalValueOf(new BigDecimal(-5.1));
		// ProbabilityNumber v = ProbabilityFactory.rationalValueOf(-1, 6);
		
		ProbabilityNumber testValue2 = ProbabilityFactory.rationalValueOf(new BigDecimal("0.15"));

		// Get BigDecimal value
		// System.out.println(testValue2.getValue());

		// System.out.println("Numerator -> " + ((RationalProbabilityNumber) testValue2).getNumerator());
		// System.out.println("Denominator -> " + ((RationalProbabilityNumber) testValue2).getDenominator());
		
		ProbabilityNumber testValue0 = ProbabilityFactory.rationalValueOf(new BigDecimal("0.000"));
		// Check if zero
		assertEquals(testValue0.isZero(), true);

		ProbabilityNumber testValue1 = ProbabilityFactory.rationalValueOf(1);
		// Check if one
		assertEquals(testValue1.isOne(), true);

		ProbabilityNumber testValue4 = ProbabilityFactory.rationalValueOf(15, 100);
		// Check if two DoubleProbabilityNumber values are equal or not
		assertEquals(testValue2.equals(testValue4), true);

		ProbabilityNumber testValue5 = ProbabilityFactory.rationalValueOf(new BigDecimal("0.1"));
		ProbabilityNumber testValue6 = ProbabilityFactory.rationalValueOf(new BigDecimal("0.8"));
		
		// Add DoubleProbabilityNumber values
		assertEquals(0.15 + 0.1, testValue2.add(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 + 0.15, testValue6.add(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);

		// Subtract DoubleProbabilityNumber values
		assertEquals(0.15 - 0.1, testValue2.subtract(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 - 0.15, testValue6.subtract(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);

		// Multiply DoubleProbabilityNumber values
		assertEquals(0.15 * 0.1, testValue2.multiply(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 * 0.15, testValue6.multiply(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		// Divide DoubleProbabilityNumber values
		assertEquals(0.15 / 0.8, testValue2.divide(testValue6).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		// Raise DoubleProbabilityNumber values to powers (check for boundary conditions
		// (positive infinity, negative infinity))
		assertEquals(0.15 * 0.15, testValue2.pow(2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 * 0.8 * 0.8, testValue6.pow(3).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		// System.out.println(testValue2.pow(BigInteger.valueOf(20)).getValue());
	
		/**
		 * Computation to test number representation precision
		 */
		// Consider two numbers of type double that are very close to each
		// other.
		double a = 0.005;
		double b = 0.0049;
		// Note the slight precision loss
		// System.out.println("double representation -> " + (a - b));
		// Accurate value when using string constructors
		ProbabilityNumber a1 = ProbabilityFactory.rationalValueOf(new BigDecimal("0.005"));
		ProbabilityNumber a2 = ProbabilityFactory.rationalValueOf(new BigDecimal("0.0049"));
		// Precision of the underlying double type representation is the cause
		// for the inaccuracy
		ProbabilityNumber b1 = ProbabilityFactory.rationalValueOf(new BigDecimal(a));
		ProbabilityNumber b2 = ProbabilityFactory.rationalValueOf(new BigDecimal(b));
		// System.out.println("String constructor initialised -> " + a1.subtract(a2).getValue().doubleValue());
		// System.out.println("Double constructor initialised -> " + b1.subtract(b2).getValue().doubleValue());
		assertEquals(0.0001, a1.subtract(a2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.0001, b1.subtract(b2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		// Raise RationalProbabilityNumber values to powers
		ProbabilityNumber v1 = ProbabilityFactory.rationalValueOf(6, 10);

		ProbabilityNumber v2 = ProbabilityFactory.rationalValueOf(new BigDecimal("0.1"));
		assertEquals(0.6 * 0.6, v1.pow(2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.1 * 0.1 * 0.1 * 0.1 * 0.1, v2.pow(5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		// BigDecimal uses integer scale internally, thus scale value cannot
		// exceed beyond the integer range
		// System.out.println("Power -> " + v1.pow(BigInteger.valueOf(4L)).getValue());
	}
}