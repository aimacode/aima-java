package aima.test.unit.probability;

import aima.extra.probability.*;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.factory.ProbabilityFactory;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Test;

/**
 * ProbabilityTest to check the various functions of the DoubleProbabilityNumber class
 */
public class DoubleTypeTest {

	private double DEFAULT_ROUNDING_THRESHOLD = 1e-8;
	
	private ProbabilityFactory<?> probFactory = ProbabilityFactory.make(DoubleProbabilityNumber.class);
	
	@Test(expected = IllegalArgumentException.class)
    public void testInvalidProbabilityNumber1() {
		new DoubleProbabilityNumber(4.0);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testInvalidProbabilityNumber2() {
		new DoubleProbabilityNumber(-5.1);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testInvalidProbabilityNumber3() {
		new DoubleProbabilityNumber(-0.1);
    }
	
	// Check if zero
	
	@Test
	public void testIsZero1() {
		ProbabilityNumber testValue0 = probFactory.valueOf(0.00000001);
		// Check if zero
		assertEquals(testValue0.isZero(), true);
	}
	
	@Test
	public void testIsZero2() {
		ProbabilityNumber testValue0 = probFactory.valueOf(0.0);
		assertEquals(testValue0.isZero(), true);
	}
	
	// Check if one
	
	@Test
	public void testIsOne1() {
		ProbabilityNumber testValue1 = probFactory.valueOf(0.999999999);
		assertEquals(testValue1.isOne(), true);
	}
	
	@Test
	public void testIsOne2() {
		ProbabilityNumber testValue1 = probFactory.valueOf(BigDecimal.ONE);
		assertEquals(testValue1.isOne(), true);
	}
	
	// Check if two DoubleProbabilityNumber values are equal or not
	
	@Test
	public void testIfEquals1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue4 = probFactory.valueOf(0.150000001);
		assertEquals(testValue2.equals(testValue4), true);
	}
	
	@Test
	public void testIfEquals2() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue4 = probFactory.valueOf(0.1499999999);
		assertEquals(testValue2.equals(testValue4), true);
	}
	
	@Test
	public void testIfEquals3() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.1);
		ProbabilityNumber testValue4 = probFactory.valueOf(0.23);
		assertEquals(testValue2.equals(testValue4), false);
	}
	
	// Add DoubleProbabilityNumber values

	@Test
	public void testAddition1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue5 = probFactory.valueOf(0.1);
		assertEquals(0.15 + 0.1, testValue2.add(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	@Test
	public void testAddition2() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue6 = probFactory.valueOf(0.8);
		assertEquals(0.8 + 0.15, testValue6.add(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	// Subtract DoubleProbabilityNumber values

	@Test
	public void testSubtraction1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue5 = probFactory.valueOf(0.1);
		assertEquals(0.15 - 0.1, testValue2.subtract(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	@Test
	public void testSubtraction2() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue6 = probFactory.valueOf(0.8);
		assertEquals(0.8 - 0.15, testValue6.subtract(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	// Multiply DoubleProbabilityNumber values

	@Test
	public void testMultiplication1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue5 = probFactory.valueOf(0.1);
		assertEquals(0.15 * 0.1, testValue2.multiply(testValue5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	@Test
	public void testMultiplication2() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue6 = probFactory.valueOf(0.8);
		assertEquals(0.8 * 0.15, testValue6.multiply(testValue2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	// Divide DoubleProbabilityNumber values

	@Test
	public void testDivision() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue6 = probFactory.valueOf(0.8);
		assertEquals(0.15 / 0.8, testValue2.divide(testValue6).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	// Raise DoubleProbabilityNumber values to powers (check for boundary conditions
	// (positive infinity, negative infinity))
	
	@Test
	public void testExponentiation1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		assertEquals(0.15 * 0.15, testValue2.pow(2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	@Test
	public void testExponentiation2() {
		ProbabilityNumber testValue6 = probFactory.valueOf(0.8);
		assertEquals(0.8 * 0.8 * 0.8, testValue6.pow(3).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	// Check if valid ProbabilityNumber or not
	
	@Test (expected = IllegalArgumentException.class)
	public void testIfValid1() {
		ProbabilityComputation adder = new ProbabilityComputation();
		ProbabilityNumber testValue1 = probFactory.valueOf(0.8);
		ProbabilityNumber testValue2 = probFactory.valueOf(0.7);
		adder.add(testValue1, testValue2).isValid();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIfValid2() {
		ProbabilityComputation compute = new ProbabilityComputation();
		ProbabilityNumber testValue1 = probFactory.valueOf(0.8);
		ProbabilityNumber testValue2 = probFactory.valueOf(0.7);
		ProbabilityNumber testValue3 = probFactory.valueOf(0.5);
		compute.sub(compute.add(testValue1, testValue2), testValue3).isValid();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIfValid3() {
		ProbabilityNumber testValue1 = probFactory.valueOf(0.8);
		ProbabilityNumber testValue2 = probFactory.valueOf(0.7);
		testValue1.add(testValue2).isValid();
	}
}