package aima.test.unit.probability;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import aima.extra.probability.BigDecimalProbabilityNumber;
import aima.extra.probability.DoubleProbabilityNumber;
import aima.extra.probability.LogProbabilityNumber;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RationalProbabilityNumber;
import aima.extra.probability.factory.ProbabilityFactory;
import aima.extra.probability.factory.RationalProbabilityFactory;

/**
 * Suite of tests for the ProbabilityNumber implementations.
 * 
 * @author Nagaraj Poti
 */
@RunWith(Enclosed.class)
public class ProbabilityNumberTest {

	public static List<Class<? extends ProbabilityNumber>> clazzes = Arrays.asList(DoubleProbabilityNumber.class,
			BigDecimalProbabilityNumber.class, LogProbabilityNumber.class, RationalProbabilityNumber.class);

	public static double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

	// Test 1
	@RunWith(Parameterized.class)
	public static class InvalidProbabilityNumberConstructionTest1 {

		@Parameter
		public Double value;

		@Parameter(1)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				Double test1 = -5.1;
				Double test2 = -0.1;
				testCases.add(new Object[] { test1, clazz });
				testCases.add(new Object[] { test2, clazz });
			}
			return testCases;
		}

		@Test(expected = IllegalArgumentException.class)
		public void testInvalidProbabilityNumber() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			probFactory.valueOf(value);
		}
	}

	// Test 2
	@RunWith(Parameterized.class)
	public static class InvalidProbabilityNumberConstructionTest2 {

		@Parameter
		public Long numerator;

		@Parameter(1)
		public Long denominator;

		@Parameter(2)
		public BigInteger bigNumerator;

		@Parameter(3)
		public BigInteger bigDenominator;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			Long numeratorTest1 = -1L, denominatorTest1 = 6L;
			BigInteger bigNumeratorTest1 = BigInteger.valueOf(numeratorTest1),
					bigDenominatorTest1 = BigInteger.valueOf(denominatorTest1);
			Long numeratorTest2 = 1874663L, denominatorTest2 = -64847464L;
			BigInteger bigNumeratorTest2 = BigInteger.valueOf(numeratorTest2),
					bigDenominatorTest2 = BigInteger.valueOf(denominatorTest2);
			testCases.add(new Object[] { numeratorTest1, denominatorTest1, bigNumeratorTest1, bigDenominatorTest1 });
			testCases.add(new Object[] { numeratorTest2, denominatorTest2, bigNumeratorTest2, bigDenominatorTest2 });
			return testCases;
		}

		@Test(expected = IllegalArgumentException.class)
		public void testInvalidProbabilityNumber1() {
			RationalProbabilityFactory rationalFactory = new RationalProbabilityFactory();
			rationalFactory.valueOf(numerator, denominator);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testInvalidProbabilityNumber2() {
			RationalProbabilityFactory rationalFactory = new RationalProbabilityFactory();
			rationalFactory.valueOf(bigNumerator, bigDenominator);
		}
	}

	// Test 3
	@RunWith(Parameterized.class)
	public static class ValidProbabilityNumberConstructionTest {

		@Parameter
		public Double value;

		@Parameter(1)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				Double test1 = 5.1;
				Double test2 = 0.1;
				testCases.add(new Object[] { test1, clazz });
				testCases.add(new Object[] { test2, clazz });
			}
			return testCases;
		}

		@Test
		public void testValidProbabilityNumber() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			probFactory.valueOf(value);
			assert (true);
		}
	}

	// Test 4
	public static class ValidRationalNumberConstructionTest {

		@Test
		public void testValidRationalNumber() {
			RationalProbabilityFactory rationalFactory = new RationalProbabilityFactory();
			rationalFactory.valueOf(-232424L, -34234234L);
			rationalFactory.valueOf(new BigInteger("-12312124"), new BigInteger("-32423439333993"));
			assert (true);
		}
	}

	/*
	 * BigDecimal instances can be initialized with values using different
	 * constructors. The MathContext objects that may be explicitly specified
	 * during constructor call are treated by the BigDecimal class in different
	 * ways.
	 * 
	 * BigDecimal n = new BigDecimal("1.3000000000", new MathContext(5,
	 * RoundingMode.HALF_EVEN)); System.out.println(n.precision());
	 */

	// Test 5
	@RunWith(Parameterized.class)
	public static class IsZeroTest {

		@Parameter
		public ProbabilityNumber value;

		@Parameter(1)
		public boolean expected;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1 = probFactory.valueOf(new BigDecimal("0.0000"));
				ProbabilityNumber test2 = probFactory.valueOf(new BigDecimal(0.0));
				ProbabilityNumber test3 = probFactory.valueOf(0.0);
				ProbabilityNumber test4 = probFactory.valueOf(0.00000001);
				ProbabilityNumber test5 = probFactory.valueOf(BigDecimal.ZERO);
				ProbabilityNumber test6 = probFactory.valueOf(0.000000000002);
				testCases.add(new Object[] { test1, true });
				testCases.add(new Object[] { test2, true });
				testCases.add(new Object[] { test3, true });
				testCases.add(new Object[] { test5, true });
				if (clazz == DoubleProbabilityNumber.class) {
					testCases.add(new Object[] { test4, true });
					testCases.add(new Object[] { test6, true });
				}
				if (clazz == BigDecimalProbabilityNumber.class) {
					testCases.add(new Object[] { test4, false });
					testCases.add(new Object[] { test6, false });
				}
				if (clazz == LogProbabilityNumber.class) {
					testCases.add(new Object[] { test4, true });
					testCases.add(new Object[] { test6, true });
				}
				if (clazz == RationalProbabilityNumber.class) {
					testCases.add(new Object[] { test4, false });
					testCases.add(new Object[] { test6, false });
				}
			}
			return testCases;
		}

		@Test
		public void testIsZero() {
			assertEquals(value.isZero(), expected);
		}
	}

	// Test 6
	@RunWith(Parameterized.class)
	public static class IsOneTest {

		@Parameter
		public ProbabilityNumber value;

		@Parameter(1)
		public boolean expected;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1 = probFactory.valueOf(new BigDecimal("1.0000"));
				ProbabilityNumber test2 = probFactory.valueOf(new BigDecimal(1.0));
				ProbabilityNumber test3 = probFactory.valueOf(1.0);
				ProbabilityNumber test4 = probFactory.valueOf(1.00000001);
				ProbabilityNumber test5 = probFactory.valueOf(BigDecimal.ONE);
				ProbabilityNumber test6 = probFactory.valueOf(1.000000000002);
				ProbabilityNumber test7 = probFactory.valueOf(0.999999999);
				testCases.add(new Object[] { test1, true });
				testCases.add(new Object[] { test2, true });
				testCases.add(new Object[] { test3, true });
				testCases.add(new Object[] { test5, true });
				if (clazz == DoubleProbabilityNumber.class) {
					testCases.add(new Object[] { test4, true });
					testCases.add(new Object[] { test6, true });
					testCases.add(new Object[] { test7, true });
				}
				if (clazz == BigDecimalProbabilityNumber.class) {
					testCases.add(new Object[] { test4, false });
					testCases.add(new Object[] { test6, false });
					testCases.add(new Object[] { test7, false });
				}
				if (clazz == LogProbabilityNumber.class) {
					testCases.add(new Object[] { test4, true });
					testCases.add(new Object[] { test6, true });
					testCases.add(new Object[] { test7, true });
				}
				if (clazz == RationalProbabilityNumber.class) {
					testCases.add(new Object[] { test4, false });
					testCases.add(new Object[] { test6, false });
					testCases.add(new Object[] { test7, false });
				}
			}
			return testCases;
		}

		@Test
		public void testIsOne() {
			assertEquals(value.isOne(), expected);
		}
	}

	// Test 7
	@RunWith(Parameterized.class)
	public static class ReducedFractionTest {

		@Parameter
		public long numerator;

		@Parameter(1)
		public long denominator;

		@Parameter(2)
		public ProbabilityNumber value;

		@Parameters
		public static Collection<Object[]> data() {
			RationalProbabilityFactory rationalFactory = new RationalProbabilityFactory();
			Collection<Object[]> testCases = new ArrayList<>();
			long numeratorTest1 = 3L, denominatorTest1 = 20L;
			ProbabilityNumber test1Value = rationalFactory.valueOf(new BigDecimal("0.15"));
			long numeratorTest2 = 1L, denominatorTest2 = 8L;
			ProbabilityNumber test2Value = rationalFactory.valueOf(new BigDecimal("0.125"));
			testCases.add(new Object[] { numeratorTest1, denominatorTest1, test1Value });
			testCases.add(new Object[] { numeratorTest2, denominatorTest2, test2Value });
			return testCases;
		}

		@Test
		public void testReducedFraction() {
			assertEquals(((RationalProbabilityNumber) value).getNumerator().longValue(), numerator);
			assertEquals(((RationalProbabilityNumber) value).getDenominator().longValue(), denominator);
		}
	}

	// Test 8
	@RunWith(Parameterized.class)
	public static class IsEqualTest {

		@Parameter
		public ProbabilityNumber value1;

		@Parameter(1)
		public ProbabilityNumber value2;

		@Parameter(2)
		public boolean expected;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1operand1 = probFactory.valueOf(new BigDecimal("0.2"));
				ProbabilityNumber test1operand2 = probFactory.valueOf(0.2);
				boolean test1expected = true;
				ProbabilityNumber test2operand1 = probFactory.valueOf(new BigDecimal("0.21"));
				ProbabilityNumber test2operand2 = probFactory.valueOf(new BigDecimal("0.20"));
				boolean test2expected = false;
				ProbabilityNumber test3operand1 = probFactory.valueOf(new BigDecimal("0.150000000"));
				ProbabilityNumber test3operand2 = probFactory.valueOf(0.150000001);
				ProbabilityNumber test4operand1 = probFactory.valueOf(0.15);
				ProbabilityNumber test4operand2 = probFactory.valueOf(0.1499999999);
				testCases.add(new Object[] { test1operand1, test1operand2, test1expected });
				testCases.add(new Object[] { test2operand1, test2operand2, test2expected });
				if (clazz == DoubleProbabilityNumber.class) {
					testCases.add(new Object[] { test3operand1, test3operand2, true });
					testCases.add(new Object[] { test4operand1, test4operand2, true });
				}
				if (clazz == BigDecimalProbabilityNumber.class) {
					testCases.add(new Object[] { test3operand1, test3operand2, false });
					testCases.add(new Object[] { test4operand1, test4operand2, true });
				}
				if (clazz == LogProbabilityNumber.class) {
					testCases.add(new Object[] { test3operand1, test3operand2, true });
					testCases.add(new Object[] { test4operand1, test4operand2, true });
				}
				if (clazz == RationalProbabilityNumber.class) {
					testCases.add(new Object[] { test3operand1, test3operand2, false });
					testCases.add(new Object[] { test4operand1, test4operand2, false });
					RationalProbabilityFactory rationalFactory = new RationalProbabilityFactory();
					ProbabilityNumber test5operand1 = probFactory.valueOf(new BigDecimal("0.15"));
					ProbabilityNumber test5operand2 = rationalFactory.valueOf(15, 100);
					boolean test5expected = true;
					testCases.add(new Object[] { test5operand1, test5operand2, test5expected });
				}
			}
			return testCases;
		}

		@Test
		public void testIsEqual() {
			assertEquals(value1.equals(value2), expected);
		}
	}

	// Test 9
	@RunWith(Parameterized.class)
	public static class AdditionTest {

		@Parameter
		public ProbabilityNumber value1;

		@Parameter(1)
		public ProbabilityNumber value2;

		@Parameter(2)
		public ProbabilityNumber expectedValue;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1operand1 = probFactory.valueOf(new BigDecimal("0.15"));
				ProbabilityNumber test1operand2 = probFactory.valueOf(0.1);
				ProbabilityNumber test1expected = probFactory.valueOf(0.25);
				ProbabilityNumber test2operand1 = probFactory.valueOf(0.15);
				ProbabilityNumber test2operand2 = probFactory.valueOf(0.8);
				ProbabilityNumber test2expected = probFactory.valueOf(0.95);
				ProbabilityNumber test3operand1 = probFactory.valueOf(new BigDecimal("0.777000"));
				ProbabilityNumber test3operand2 = probFactory.valueOf(0.213111);
				ProbabilityNumber test3expected = probFactory.valueOf(0.990111);
				testCases.add(new Object[] { test1operand1, test1operand2, test1expected });
				testCases.add(new Object[] { test2operand1, test2operand2, test2expected });
				testCases.add(new Object[] { test3operand1, test3operand2, test3expected });
			}
			return testCases;
		}

		@Test
		public void testAddition() {
			assertEquals(value1.add(value2), expectedValue);
		}

		@Test
		public void testProbabilityComputationAdd() {
			ProbabilityComputation compute = new ProbabilityComputation();
			compute.overrideComputationPrecision(MathContext.UNLIMITED);
			assertEquals(compute.add(value1, value2), expectedValue);
		}
	}

	// Test 10
	@RunWith(Parameterized.class)
	public static class SubtractionTest {

		@Parameter
		public ProbabilityNumber value1;

		@Parameter(1)
		public ProbabilityNumber value2;

		@Parameter(2)
		public ProbabilityNumber expectedValue;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1operand1 = probFactory.valueOf(new BigDecimal("0.15"));
				ProbabilityNumber test1operand2 = probFactory.valueOf(0.1);
				ProbabilityNumber test1expected = probFactory.valueOf(0.05);
				ProbabilityNumber test2operand1 = probFactory.valueOf(0.8);
				ProbabilityNumber test2operand2 = probFactory.valueOf(0.15);
				ProbabilityNumber test2expected = probFactory.valueOf(0.65);
				ProbabilityNumber test3operand1 = probFactory.valueOf(new BigDecimal("0.777000"));
				ProbabilityNumber test3operand2 = probFactory.valueOf(0.213111);
				ProbabilityNumber test3expected = probFactory.valueOf(0.563889);
				testCases.add(new Object[] { test1operand1, test1operand2, test1expected });
				testCases.add(new Object[] { test2operand1, test2operand2, test2expected });
				testCases.add(new Object[] { test3operand1, test3operand2, test3expected });
			}
			return testCases;
		}

		@Test
		public void testSubtraction() {
			assertEquals(value1.subtract(value2), expectedValue);
		}

		@Test
		public void testProbabilityComputationSubtract() {
			ProbabilityComputation compute = new ProbabilityComputation();
			compute.overrideComputationPrecision(MathContext.UNLIMITED);
			assertEquals(compute.sub(value1, value2), expectedValue);
		}
	}

	// Test 11
	@RunWith(Parameterized.class)
	public static class MultiplicationTest {

		@Parameter
		public ProbabilityNumber value1;

		@Parameter(1)
		public ProbabilityNumber value2;

		@Parameter(2)
		public ProbabilityNumber expectedValue;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1operand1 = probFactory.valueOf(new BigDecimal("0.15"));
				ProbabilityNumber test1operand2 = probFactory.valueOf(0.1);
				ProbabilityNumber test1expected = probFactory.valueOf(0.015);
				ProbabilityNumber test2operand1 = probFactory.valueOf(0.8);
				ProbabilityNumber test2operand2 = probFactory.valueOf(0.15);
				ProbabilityNumber test2expected = probFactory.valueOf(0.12);
				ProbabilityNumber test3operand1 = probFactory.valueOf(new BigDecimal("0.777000"));
				ProbabilityNumber test3operand2 = probFactory.valueOf(0.213111);
				ProbabilityNumber test3expected = probFactory.valueOf(new BigDecimal("0.165587247"));
				testCases.add(new Object[] { test1operand1, test1operand2, test1expected });
				testCases.add(new Object[] { test2operand1, test2operand2, test2expected });
				testCases.add(new Object[] { test3operand1, test3operand2, test3expected });
			}
			return testCases;
		}

		@Test
		public void testMultiplication() {
			assertEquals(value1.multiply(value2), expectedValue);
		}

		@Test
		public void testProbabilityComputationSubtract() {
			ProbabilityComputation compute = new ProbabilityComputation();
			compute.overrideComputationPrecision(MathContext.UNLIMITED);
			assertEquals(compute.mul(value1, value2), expectedValue);
		}
	}

	// Test 12
	@RunWith(Parameterized.class)
	public static class DivisionTest {

		@Parameter
		public ProbabilityNumber value1;

		@Parameter(1)
		public ProbabilityNumber value2;

		@Parameter(2)
		public ProbabilityNumber expectedValue;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1operand1 = probFactory.valueOf(new BigDecimal("0.15"));
				ProbabilityNumber test1operand2 = probFactory.valueOf(0.1);
				ProbabilityNumber test1expected = probFactory.valueOf(1.5);
				ProbabilityNumber test2operand1 = probFactory.valueOf(0.15);
				ProbabilityNumber test2operand2 = probFactory.valueOf(0.8);
				ProbabilityNumber test2expected = probFactory.valueOf(0.1875);
				ProbabilityNumber test3operand1 = probFactory.valueOf(new BigDecimal("0.777000"));
				ProbabilityNumber test3operand2 = probFactory.valueOf(0.213111);
				ProbabilityNumber test3expected = probFactory.valueOf(3.645987302);
				testCases.add(new Object[] { test1operand1, test1operand2, test1expected });
				testCases.add(new Object[] { test2operand1, test2operand2, test2expected });
				// RationalProbabilityNumber has exact fraction checks and thus
				// approximation checks don't work.
				if (clazz != RationalProbabilityNumber.class) {
					testCases.add(new Object[] { test3operand1, test3operand2, test3expected });
				} else {
					RationalProbabilityFactory rationalFactory = new RationalProbabilityFactory();
					ProbabilityNumber test3rationalexpected = rationalFactory.valueOf(777000, 213111);
					testCases.add(new Object[] { test3operand1, test3operand2, test3rationalexpected });
				}
			}
			return testCases;
		}

		@Test
		public void testDivision1() {
			assertEquals(value1.divide(value2), expectedValue);
		}

		@Test
		public void testProbabilityComputationDivide() {
			ProbabilityComputation compute = new ProbabilityComputation();
			compute.overrideComputationPrecision(MathContext.UNLIMITED);
			// For non-terminating decimal cases when infinite precision is
			// specified, ArithmeticException is thrown
			try {
				assertEquals(compute.div(value1, value2), expectedValue);
			} catch (ArithmeticException ex) {

			}
		}
	}

	// Test 13
	public static class DivisionTest2 {

		@Test
		public void testDivision2() {
			// Check for computations with different precision values
			ProbabilityFactory<?> doubleFactory = ProbabilityFactory.make(DoubleProbabilityNumber.class);
			ProbabilityNumber t1 = doubleFactory.valueOf(new BigDecimal(0.1), 3);
			ProbabilityNumber t2 = doubleFactory.valueOf(new BigDecimal(0.3), 5);
			assertEquals(0.3333, t1.divide(t2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
		}
	}
	
	// Test 14
	@RunWith(Parameterized.class)
	public static class ExponentiationTest {

		@Parameter
		public ProbabilityNumber value1;

		@Parameter(1)
		public int value2;

		@Parameter(2)
		public ProbabilityNumber expectedValue;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				ProbabilityNumber test1operand1 = probFactory.valueOf(new BigDecimal("0.15"));
				int test1operand2 = 2;
				ProbabilityNumber test1expected = probFactory.valueOf(0.0225);
				ProbabilityNumber test2operand1 = probFactory.valueOf(0.8);
				int test2operand2 = 3;
				ProbabilityNumber test2expected = probFactory.valueOf(0.512);
				testCases.add(new Object[] { test1operand1, test1operand2, test1expected });
				testCases.add(new Object[] { test2operand1, test2operand2, test2expected });
			}
			return testCases;
		}

		@Test
		public void testExponentiation() {
			assertEquals(value1.pow(value2), expectedValue);
		}

		@Test
		public void testProbabilityComputationSubtract() {
			ProbabilityComputation compute = new ProbabilityComputation();
			compute.overrideComputationPrecision(MathContext.UNLIMITED);
			assertEquals(compute.pow(value1, value2), expectedValue);
		}
	}

	// Test 15
	@RunWith(Parameterized.class)
	public static class ValidityTest {

		@Parameter
		public ProbabilityFactory<?> probFactory;

		@Parameters
		public static Collection<Object[]> data() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				testCases.add(new Object[] { probFactory });
			}
			return testCases;
		}

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

		@Test(expected = IllegalArgumentException.class)
		public void testIfValid3() {
			ProbabilityNumber testValue1 = probFactory.valueOf(0.8);
			ProbabilityNumber testValue2 = probFactory.valueOf(0.7);
			testValue2.subtract(testValue1);
		}
	}
	
	/**
	 * Computation to test number representation precision
	 */
	public static class PrecisionTest {
		
		@Test
		public void testBigDecimalNumber() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(BigDecimalProbabilityNumber.class);
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
		
		@Test
		public void testRational() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(BigDecimalProbabilityNumber.class);
			RationalProbabilityFactory rationalFactory = new RationalProbabilityFactory();
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
			
			// Raise RationalProbabilityNumber values to powers
			ProbabilityNumber v1 = rationalFactory.valueOf(6, 10);

			ProbabilityNumber v2 = probFactory.valueOf(new BigDecimal("0.1"));
			assertEquals(0.6 * 0.6, v1.pow(2).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
			assertEquals(0.1 * 0.1 * 0.1 * 0.1 * 0.1, v2.pow(5).getValue().doubleValue(), DEFAULT_ROUNDING_THRESHOLD);
			// BigDecimal uses integer scale internally, thus scale value cannot
			// exceed beyond the integer range
			// System.out.println("Power -> " + v1.pow(BigInteger.valueOf(4L)).getValue());
		}	
	}
}
