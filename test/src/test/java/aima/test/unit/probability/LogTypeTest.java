package aima.test.unit.probability;

import aima.extra.probability.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class LogTypeTest {

	@Test
	public void test() {
		
		double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

		// Constructors with double parameter type
		
		// Throws IllegalArgumentException
		// ProbabilityNumber testValue1 = ProbabilityFactory.logValueOf(4.0);
		// ProbabilityNumber testValue2 = ProbabilityFactory.logValueOf(-5.1);
		
		ProbabilityNumber testValue2 = ProbabilityFactory.logValueOf(0.15);

		// Get BigDecimal value
		// System.out.println(testValue2.getValue());
		
		ProbabilityNumber testValue0 = ProbabilityFactory.logValueOf(0.00000001);
		// Check if zero
		assertEquals(testValue0.isZero(), true);

		ProbabilityNumber testValue1 = ProbabilityFactory.logValueOf(0.999999999);
		// Check if one
		assertEquals(testValue1.isOne(), true);
		
		ProbabilityNumber testValue4 = ProbabilityFactory.logValueOf(0.150000001);
		// Check if two LogProbabilityNumber values are equal or not
		assertEquals(testValue2.equals(testValue4), true);

		ProbabilityNumber testValue5 = ProbabilityFactory.logValueOf(0.1);
		ProbabilityNumber testValue6 = ProbabilityFactory.logValueOf(0.8);
		
		// Add LogProbabilityNumber values
		assertEquals(0.15 + 0.1, testValue2.add(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 + 0.15, testValue6.add(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		// Subtract LogProbabilityNumber values
		assertEquals(0.15 - 0.1, testValue2.subtract(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 - 0.15, testValue6.subtract(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);

		// Multiply LogProbabilityNumber values
		assertEquals(0.15 * 0.1, testValue2.multiply(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 * 0.15, testValue6.multiply(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		// Divide LogProbabilityNumber values
		assertEquals(0.15 / 0.8, testValue2.divide(testValue6).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		assertEquals(0.15 * 0.15, testValue2.pow(2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.8 * 0.8 * 0.8, testValue6.pow(3).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		
		// System.out.println(testValue2.pow(BigInteger.valueOf(20)).getValue());
	}
}
