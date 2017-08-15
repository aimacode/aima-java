package aima.test.unit.probability;

import aima.extra.probability.*;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.factory.ProbabilityFactory;
import static org.junit.Assert.*;
import java.math.*;
import org.junit.Test;

/**
 * ProbabilityTest to check various functions of the BigDecimalProbabilityNumber class
 */
public class BigDecimalTypeTest {

	private double DEFAULT_ROUNDING_THRESHOLD = 1e-8;
	
	private ProbabilityFactory<?> probFactory = ProbabilityFactory.make(BigDecimalProbabilityNumber.class);

	@Test
    public void testValidProbabilityNumber1() {
		probFactory.valueOf(4.0);
		assert(true);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testInvalidProbabilityNumber2() {
		probFactory.valueOf(-5.1);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testInvalidProbabilityNumber3() {
		probFactory.valueOf(-0.1);
    }
	
	/* 
	 * BigDecimal instances can be initialized with values using different constructors. 
	 * The MathContext objects that may be explicitly specified during constructor call
	 * are treated by the BigDecimal class in different ways. 
	 * 
	 * BigDecimal n = new BigDecimal("1.3000000000", new MathContext(5, RoundingMode.HALF_EVEN));
	 * System.out.println(n.precision());
	 */
	
	// Check if zero
	
	@Test
	public void testIsZero1() {
		ProbabilityNumber testValue0 = probFactory.valueOf(new BigDecimal("0.000"));
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
		ProbabilityNumber testValue1 = probFactory.valueOf(
				new BigDecimal("1.000000"));
		// Check if one
		assertEquals(testValue1.isOne(), true);
	}
	
	@Test
	public void testIsOne2() {
		ProbabilityNumber testValue1 = probFactory.valueOf(1.0);
		assertEquals(testValue1.isOne(), true);
	}
	
	// Check if two BigDecimalProbabilityNumber values are equal or not
	
	@Test
	public void testIfEquals1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue4 = probFactory.valueOf(
				new BigDecimal("0.151"));
		// Check if two BigDecimalProbabilityNumber values are equal or not
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
	
	// Add BigDecimalProbabilityNumber values


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
	
	// Subtract BigDecimalProbabilityNumber values

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
	
	// Multiply BigDecimalProbabilityNumber values

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
	
	// Divide BigDecimalProbabilityNumber values

	@Test
	public void testDivision1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		ProbabilityNumber testValue6 = probFactory.valueOf(0.8);
		assertEquals(0.19, testValue2.divide(testValue6).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	/*
	@Test
	public void testDivision2() {
		// Unlimited precision (non terminating decimal value)
		ProbabilityNumber t1 = probFactory.valueOf(new BigDecimal("0.1"));
		ProbabilityNumber t2 = probFactory.valueOf(new BigDecimal("0.3"));
		t1.overrideComputationPrecisionGlobally(MathContext.UNLIMITED);
		assertEquals(0.1 / 0.3, t1.divide(t2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	*/
	
	@Test
	public void testDivision3() {
		// Check for computations with different precision values
		ProbabilityNumber t1 = probFactory.valueOf(new BigDecimal(0.1), 3);
		ProbabilityNumber t2 = probFactory.valueOf(new BigDecimal(0.3), 5);
		assertEquals(0.3333, t1.divide(t2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	// Raise BigDecimalProbabilityNumber values to powers (check for boundary conditions
	// (positive infinity, negative infinity))
	
	@Test
	public void testExponentiation1() {
		ProbabilityNumber testValue2 = probFactory.valueOf(0.15);
		assertEquals(0.15 * 0.15, testValue2.pow(2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	@Test
	public void testExponentiation2() {
		ProbabilityNumber testValue6 = probFactory.valueOf(0.8);
		assertEquals(0.51, testValue6.pow(3).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
	}
	
	// Check if valid ProbabilityNumber or not
	
	@Test
	public void testIfValid1() {
		ProbabilityComputation adder = new ProbabilityComputation();
		ProbabilityNumber testValue1 = probFactory.valueOf(0.8);
		ProbabilityNumber testValue2 = probFactory.valueOf(0.7);
		assertFalse(adder.add(testValue1, testValue2).isValid());
	}
	
	@Test 
	public void testIfValid2() {
		ProbabilityComputation compute = new ProbabilityComputation();
		ProbabilityNumber testValue1 = probFactory.valueOf(0.8);
		ProbabilityNumber testValue2 = probFactory.valueOf(0.7);
		ProbabilityNumber testValue3 = probFactory.valueOf(0.5);
		assertTrue(compute.sub(compute.add(testValue1, testValue2), testValue3).isValid());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIfValid3() {
		ProbabilityNumber testValue1 = probFactory.valueOf(0.8);
		ProbabilityNumber testValue2 = probFactory.valueOf(0.7);
		testValue2.subtract(testValue1);
	}
	
	/**
	 * Computation to test number representation precision
	 */
	@Test
	public void testBigDecimal() {
		// Consider two numbers of type double that are very close to each
		// other.
		double a = 0.005;
		double b = 0.0049;
		// Note the slight precision loss
		// System.out.println("double representation -> " + (a - b));
		// Accurate value when using string constructors
		ProbabilityNumber a1 = probFactory.valueOf(new BigDecimal("0.005"));
		ProbabilityNumber a2 = probFactory.valueOf(new BigDecimal("0.0049"));
		// Precision of the underlying double type representation is the cause
		// for the inaccuracy
		ProbabilityNumber b1 = probFactory.valueOf(new BigDecimal(a));
		ProbabilityNumber b2 = probFactory.valueOf(new BigDecimal(b));
		// System.out.println("String constructor initialised -> " + a1.subtract(a2).getValue().doubleValue());
		// System.out.println("Double constructor initialised -> " + b1.subtract(b2).getValue().doubleValue());
		assertEquals(0.0001, a1.subtract(a2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.0001, b1.subtract(b2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		// Raise BigDecimalProbabilityNumber values to powers
		ProbabilityNumber c1 = probFactory.valueOf(new BigDecimal("0.6"));
		ProbabilityNumber c2 = probFactory.valueOf(new BigDecimal(0.1));
		assertEquals(0.6 * 0.6, c1.pow(2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		assertEquals(0.1 * 0.1 * 0.1 * 0.1 * 0.1, c2.pow(5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		// BigDecimal uses integer scale internally, thus scale value cannot
		// exceed beyond the integer range
		// System.out.println("Power -> " + c1.pow(BigInteger.valueOf(1000000000L)).getValue());
	}
}