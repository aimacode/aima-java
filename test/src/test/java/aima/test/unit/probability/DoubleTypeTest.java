package aima.test.unit.probability;

import aima.extra.probability.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class DoubleTypeTest {

	/**
	 * ProbabilityTest to check the various functions of the DoubleProbabilityNumber class
	 */
	@Test
	public void testDouble() {

		double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

		// Constructors with double parameter type
		
		// Throws IllegalArgumentException
		// ProbabilityNumber testValue1 = new DoubleProbabilityNumber(4.0);
		// ProbabilityNumber testValue2 = new DoubleProbabilityNumber(-5.1);
		
		ProbabilityNumber testValue2 = ProbabilityFactory.doubleValueOf(0.15);

		// Get BigDecimal value
		// System.out.println(testValue2.getValue());
		
		ProbabilityNumber testValue0 = ProbabilityFactory.doubleValueOf(0.00000001);
		// Check if zero
		assertEquals(testValue0.isZero(), true);

		ProbabilityNumber testValue1 = ProbabilityFactory.doubleValueOf(0.999999999);
		// Check if one
		assertEquals(testValue1.isOne(), true);
		
		ProbabilityNumber testValue4 = ProbabilityFactory.doubleValueOf(0.150000001);
		// Check if two DoubleProbabilityNumber values are equal or not
		assertEquals(testValue2.equals(testValue4), true);

		ProbabilityNumber testValue5 = ProbabilityFactory.doubleValueOf(0.1);
		ProbabilityNumber testValue6 = ProbabilityFactory.doubleValueOf(0.8);
		
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
	}
}
