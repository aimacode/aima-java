package aima.test.unit.probability;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;
import aima.extra.probability.BigDecimalProbabilityNumber;
import aima.extra.probability.DoubleProbabilityNumber;
import aima.extra.probability.LogProbabilityNumber;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandVar;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.RationalProbabilityNumber;
import aima.extra.probability.bayes.ProbabilityTable;
import aima.extra.probability.domain.Domain;
import aima.extra.probability.domain.FiniteArbitraryTokenDomain;
import aima.extra.probability.domain.FiniteBooleanDomain;
import aima.extra.probability.domain.FiniteOrdinalDomain;
import aima.extra.probability.factory.ProbabilityFactory;

/**
 * Suite of tests for the ProbabilityTable class.
 * 
 * @author Nagaraj Poti
 */
@RunWith(Enclosed.class)
public class ProbabilityTableTest {

	public static List<Class<? extends ProbabilityNumber>> clazzes = Arrays.asList(DoubleProbabilityNumber.class,
			BigDecimalProbabilityNumber.class, LogProbabilityNumber.class, RationalProbabilityNumber.class);

	// Test 1
	@RunWith(Parameterized.class)
	public static class InvalidProbabilityTableConstructionTest {

		@Parameter
		public List<RandomVariable> vars;

		@Parameter(1)
		public List<ProbabilityNumber> values;

		@Parameter(2)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameters
		public static Collection<Object[]> testRandomVariables() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("X", ordinalDomain);
			RandomVariable rv3 = new RandVar("Y", booleanDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);

				// Test for missing values
				List<RandomVariable> test1vars = Arrays.asList(rv1, rv2);
				List<ProbabilityNumber> test1values = Arrays.asList(
						// X = true, Y = 1 (missing)
						// X = true, Y = 2
						probFactory.valueOf(0.1),
						// X = true, Y = 3
						probFactory.valueOf(0.2),
						// X = false, Y = 1
						probFactory.valueOf(0.3),
						// X = false, Y = 2
						probFactory.valueOf(0.1),
						// X = false, Y = 3
						probFactory.valueOf(0.1));

				// Test for invalid values size
				List<RandomVariable> test2vars = Arrays.asList(rv1, rv3);
				List<ProbabilityNumber> test2values = Arrays.asList(
						// X = true, Y = 1
						probFactory.valueOf(0.2),
						// X = true, Y = 2
						probFactory.valueOf(0.1),
						// X = true, Y = 3
						probFactory.valueOf(0.2),
						// X = false, Y = 1
						probFactory.valueOf(0.3),
						// X = false, Y = 2
						probFactory.valueOf(0.1),
						// X = false, Y = 3
						probFactory.valueOf(0.1));

				testCases.add(new Object[] { test1vars, test1values, clazz });
				testCases.add(new Object[] { test2vars, test2values, clazz });
				testCases.add(new Object[] { null, null, clazz });
				testCases.add(new Object[] { null, test2values, clazz });
				testCases.add(new Object[] { test2vars, test2values, null });
			}
			return testCases;
		}

		@Test(expected = Exception.class)
		public void testValidity() {
			new ProbabilityTable(vars, values, clazz);
		}
	}

	// Test 2
	@RunWith(Parameterized.class)
	public static class ContainsTest {

		@Parameter
		public boolean expected;

		@Parameter(1)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameter(2)
		public RandomVariable rv;

		@Parameters
		public static Collection<Object[]> testRandomVariables() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("X", ordinalDomain);
			RandomVariable rv3 = new RandVar("Y", ordinalDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				// Test cases with respect to ProbabilityTable defined in
				// the testContains() method
				testCases.add(new Object[] { true, clazz, rv1 });
				testCases.add(new Object[] { false, clazz, rv2 });
				testCases.add(new Object[] { true, clazz, rv3 });
				testCases.add(new Object[] { false, clazz, null });
			}
			return testCases;
		}

		@Test
		public void testContains() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("Y", ordinalDomain);
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			List<RandomVariable> vars = Arrays.asList(rv1, rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1
					probFactory.valueOf(0.2),
					// X = true, Y = 2
					probFactory.valueOf(0.1),
					// X = true, Y = 3
					probFactory.valueOf(0.2),
					// X = false, Y = 1
					probFactory.valueOf(0.3),
					// X = false, Y = 2
					probFactory.valueOf(0.1),
					// X = false, Y = 3
					probFactory.valueOf(0.1));
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			assertEquals(newTable.contains(rv), expected);
		}
	}

	// Test 3
	@RunWith(Parameterized.class)
	public static class GetValueTest1 {

		@Parameter
		public ProbabilityNumber expected;

		@Parameter(1)
		public Predicate<Map<RandomVariable, Object>> proposition;

		@Parameter(2)
		public Class<? extends ProbabilityNumber> clazz;

		public static Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
		public static Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("windy", "rainy"));
		public static RandomVariable rv1 = new RandVar("X", booleanDomain);
		public static RandomVariable rv2 = new RandVar("Y", arbitraryDomain);

		@Parameters
		public static Collection<Object[]> propositions() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				// Testcase 1
				Predicate<Map<RandomVariable, Object>> proposition1 = mp -> {
					return mp.get(rv1).equals(true) && mp.get(rv2).equals("windy");
				};
				ProbabilityNumber expected1 = probFactory.valueOf(0.35);
				// Testcase 2
				Predicate<Map<RandomVariable, Object>> proposition2 = mp -> {
					return mp.get(rv2).equals("rainy") && mp.get(rv1).equals(false);
				};
				ProbabilityNumber expected2 = probFactory.valueOf(new BigDecimal("0.06"));
				// Testcase 3
				Predicate<Map<RandomVariable, Object>> proposition3 = mp -> {
					return mp.get(rv1).equals(false) && mp.get(rv2).equals("windy");
				};
				ProbabilityNumber expected3 = probFactory.valueOf(new BigDecimal("0.27"));
				// Testcase 4
				Predicate<Map<RandomVariable, Object>> proposition4 = mp -> {
					return mp.get(rv1).equals(true) && mp.get(rv2).equals("rainy");
				};
				ProbabilityNumber expected4 = probFactory.valueOf(0.32);
				// Testcase 5
				Predicate<Map<RandomVariable, Object>> proposition5 = mp -> {
					return mp.get(rv1).equals(true);
				};
				ProbabilityNumber expected5 = probFactory.valueOf(0.67);
				// Testcase 6
				Predicate<Map<RandomVariable, Object>> proposition6 = mp -> {
					return !(mp.get(rv1).equals(true));
				};
				ProbabilityNumber expected6 = probFactory.valueOf(0.33);

				testCases.add(new Object[] { expected1, proposition1, clazz });
				testCases.add(new Object[] { expected2, proposition2, clazz });
				testCases.add(new Object[] { expected3, proposition3, clazz });
				testCases.add(new Object[] { expected4, proposition4, clazz });
				testCases.add(new Object[] { expected5, proposition5, clazz });
				testCases.add(new Object[] { expected6, proposition6, clazz });
			}
			return testCases;
		}

		@Test
		public void testGetValue() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			List<RandomVariable> vars = Arrays.asList(rv1, rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = windy
					probFactory.valueOf(new BigDecimal("0.35")),
					// X = true, Y = rainy
					probFactory.valueOf(new BigDecimal("0.32")),
					// X = false, Y = windy
					probFactory.valueOf(BigDecimal.valueOf(0.27)),
					// X = false, Y = rainy
					probFactory.valueOf(0.06));
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			assertEquals(newTable.getValue(proposition), expected);
		}
	}

	// Test 4
	@RunWith(Parameterized.class)
	public static class GetValueTest2 {

		@Parameter
		public ProbabilityNumber expected;

		@Parameter(1)
		public Predicate<Map<RandomVariable, Object>> proposition;

		@Parameter(2)
		public Class<? extends ProbabilityNumber> clazz;

		public static Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
		public static Domain ordinalDomain = new FiniteOrdinalDomain<Integer>(Arrays.asList(1, 3, 2, 9));
		public static RandomVariable rv1 = new RandVar("X", booleanDomain);
		public static RandomVariable rv2 = new RandVar("Y", ordinalDomain);

		@Parameters
		public static Collection<Object[]> propositions() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				// Testcase 1 (All domain values lesser than 3 i.e P(Y < 3))
				Predicate<Map<RandomVariable, Object>> proposition1 = mp -> {
					return ((Integer) (mp.get(rv2))).compareTo(3) == -1;
				};
				ProbabilityNumber expected1 = probFactory.valueOf(0.21);
				// Testcase 2 (All domain values greater than 3 i.e P(Y >= 3))
				Predicate<Map<RandomVariable, Object>> proposition2 = mp -> {
					return ((Integer) (mp.get(rv2))).compareTo(3) >= 0;
				};
				ProbabilityNumber expected2 = probFactory.valueOf(0.79);
				// Testcase 3 (All domain values not equal to 3 i.e P(Y != 3))
				Predicate<Map<RandomVariable, Object>> proposition3 = mp -> {
					return ((Integer) (mp.get(rv2))).compareTo(3) != 0;
				};
				ProbabilityNumber expected3 = probFactory.valueOf(0.43);
				// Testcase 4 - P(X = true AND Y > 2)
				Predicate<Map<RandomVariable, Object>> proposition4 = mp -> {
					return mp.get(rv1).equals(true) && ((Integer) (mp.get(rv2))).compareTo(2) == 1;
				};
				ProbabilityNumber expected4 = probFactory.valueOf(0.33);
				// Testcase 5 - P(X = false OR Y < 9)
				Predicate<Map<RandomVariable, Object>> proposition5 = mp -> {
					return mp.get(rv1).equals(false) || ((Integer) (mp.get(rv2))).compareTo(9) == -1;
				};
				ProbabilityNumber expected5 = probFactory.valueOf(0.94);
				// Testcase 6 - P((Y <= 2 OR Y > 3) AND X = false)
				// (AND has higher precedence than OR)
				Predicate<Map<RandomVariable, Object>> proposition6 = mp -> {
					return (((Integer) (mp.get(rv2))).compareTo(2) <= 0 || ((Integer) (mp.get(rv2))).compareTo(3) > 0)
							&& mp.get(rv1).equals(false);
				};
				ProbabilityNumber expected6 = probFactory.valueOf(0.29);

				testCases.add(new Object[] { expected1, proposition1, clazz });
				testCases.add(new Object[] { expected2, proposition2, clazz });
				testCases.add(new Object[] { expected3, proposition3, clazz });
				testCases.add(new Object[] { expected4, proposition4, clazz });
				testCases.add(new Object[] { expected5, proposition5, clazz });
				testCases.add(new Object[] { expected6, proposition6, clazz });
			}
			return testCases;
		}

		@Test
		public void testGetValue() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			List<RandomVariable> vars = Arrays.asList(rv1, rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1
					probFactory.valueOf(new BigDecimal("0.01")),
					// X = true, Y = 2
					probFactory.valueOf(new BigDecimal("0.07")),
					// X = true, Y = 3
					probFactory.valueOf(BigDecimal.valueOf(0.27)),
					// X = true, Y = 9
					probFactory.valueOf(0.06),
					// X = false, Y = 1
					probFactory.valueOf(new BigDecimal("0.03")),
					// X = false, Y = 2
					probFactory.valueOf(0.1),
					// X = false, Y = 3
					probFactory.valueOf(0.3),
					// X = false, Y = 9
					probFactory.valueOf(0.16));
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			assertEquals(newTable.getValue(proposition), expected);
		}
	}

	// Test 5
	@RunWith(Parameterized.class)
	public static class GetValueAndIndexTests {

		@Parameter
		public ProbabilityNumber expected;

		@Parameter(1)
		public Map<RandomVariable, Object> event;

		@Parameter(2)
		public Object[] eventValues;

		@Parameter(3)
		public int expectedIdx;

		@Parameter(4)
		public Class<? extends ProbabilityNumber> clazz;

		public static Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
		public static Domain ordinalDomain = new FiniteOrdinalDomain<Integer>(Arrays.asList(3, 2, 1));
		public static RandomVariable rv1 = new RandVar("X", booleanDomain);
		public static RandomVariable rv2 = new RandVar("Y", ordinalDomain);

		@Parameters
		public static Collection<Object[]> eventValues() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				// Testcase 1
				// Map<RandomVariable, Object> argument
				Map<RandomVariable, Object> test1 = new HashMap<RandomVariable, Object>();
				test1.put(rv1, true);
				test1.put(rv2, 3);
				// Object[] argument
				Object[] test1Values = new Object[] { true, 3 };
				ProbabilityNumber expected1 = probFactory.valueOf(new BigDecimal("0.32423423423423423"));
				int expectedIdx1 = 2;
				// Testcase 2
				// Map<RandomVariable, Object> argument
				Map<RandomVariable, Object> test2 = new HashMap<RandomVariable, Object>();
				test2.put(rv2, 2);
				test2.put(rv1, false);
				// Object[] argument
				Object[] test2Values = new Object[] { false, 2 };
				int expectedIdx2 = 4;
				ProbabilityNumber expected2 = probFactory.valueOf(new BigDecimal("0.111111111111111"));

				testCases.add(new Object[] { expected1, test1, test1Values, expectedIdx1, clazz });
				testCases.add(new Object[] { expected2, test2, test2Values, expectedIdx2, clazz });
			}
			return testCases;
		}

		@Test
		public void testGetValue() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			List<RandomVariable> vars = Arrays.asList(rv1, rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1
					probFactory.valueOf(new BigDecimal("0.00413938374738292")),
					// X = true, Y = 2
					probFactory.valueOf(new BigDecimal("0.03402304230423043")),
					// X = true, Y = 3
					probFactory.valueOf(new BigDecimal("0.32423423423423423")),
					// X = false, Y = 1
					probFactory.valueOf(new BigDecimal("0.2342343444343434")),
					// X = false, Y = 2
					probFactory.valueOf(new BigDecimal("0.111111111111111")),
					// X = false, Y = 3
					probFactory.valueOf(new BigDecimal("0.292257884168698")));
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			assertEquals(newTable.getValue(event), expected);
			assertEquals(newTable.getValue(eventValues), expected);
		}

		@Test
		public void testGetIndex() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			List<RandomVariable> vars = Arrays.asList(rv1, rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1
					probFactory.valueOf(new BigDecimal("0.00413938374738292")),
					// X = true, Y = 2
					probFactory.valueOf(new BigDecimal("0.03402304230423043")),
					// X = true, Y = 3
					probFactory.valueOf(new BigDecimal("0.32423423423423423")),
					// X = false, Y = 1
					probFactory.valueOf(new BigDecimal("0.2342343444343434")),
					// X = false, Y = 2
					probFactory.valueOf(new BigDecimal("0.111111111111111")),
					// X = false, Y = 3
					probFactory.valueOf(new BigDecimal("0.292257884168698")));
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			assertEquals(newTable.getIndex(event), expectedIdx);
			assertEquals(newTable.getIndex(eventValues), expectedIdx);
		}
	}

	// Testcase 6
	@RunWith(Parameterized.class)
	public static class MarginalizeTest {

		@Parameter
		public List<ProbabilityNumber> expectedMarginalizedValues;

		@Parameter(1)
		public List<RandomVariable> varsToMarginalize;

		@Parameter(2)
		public List<RandomVariable> vars;

		@Parameter(3)
		public List<ProbabilityNumber> values;

		@Parameter(4)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameters
		public static Collection<Object[]> data() {
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(9, 3));
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("summer", "winter"));
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			RandomVariable rv1 = new RandVar("X", ordinalDomain);
			RandomVariable rv2 = new RandVar("Y", arbitraryDomain);
			RandomVariable rv3 = new RandVar("Z", booleanDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				List<RandomVariable> test1vars = Arrays.asList(rv1, rv2);
				List<ProbabilityNumber> test1values = Arrays.asList(
						// X = 3, Y = summer
						probFactory.valueOf(0.32),
						// X = 3, Y = winter
						probFactory.valueOf(0.23),
						// X = 9, Y = summer
						probFactory.valueOf(0.1),
						// X = 9, Y = winter
						probFactory.valueOf(0.35));
				List<ProbabilityNumber> test1 = Arrays.asList(
						// X = 3
						probFactory.valueOf(0.55),
						// X = 9
						probFactory.valueOf(0.45));
				List<RandomVariable> varsToMarginalize1 = Arrays.asList(rv2);
				List<ProbabilityNumber> test2 = Arrays.asList(
						// Marginalize all variables
						probFactory.valueOf(1.0));
				List<RandomVariable> varsToMarginalize2 = Arrays.asList(rv1, rv2);
				List<RandomVariable> test2vars = Arrays.asList(rv2, rv3, rv1);
				List<ProbabilityNumber> test2values = Arrays.asList(
						// Y = summer, Z = true, X = 3
						probFactory.valueOf(0.02),
						// Y = summer, Z = true, X = 9
						probFactory.valueOf(0.09),
						// Y = summer, Z = false, X = 3
						probFactory.valueOf(0.11),
						// Y = summer, Z = false, X = 9
						probFactory.valueOf(0.01),
						// Y = winter, Z = true, X = 3
						probFactory.valueOf(0.02),
						// Y = winter, Z = true, X = 9
						probFactory.valueOf(0.14),
						// Y = winter, Z = false, X = 3
						probFactory.valueOf(0.3),
						// Y = winter, Z = false, X = 9
						probFactory.valueOf(0.31));
				List<ProbabilityNumber> test3 = Arrays.asList(
						// Z = true
						probFactory.valueOf(0.27),
						// Z = false
						probFactory.valueOf(0.73));
				List<RandomVariable> varsToMarginalize3 = Arrays.asList(rv1, rv2);
				List<ProbabilityNumber> test4 = Arrays.asList(
						// Y = summer, X = 3
						probFactory.valueOf(0.13),
						// Y = summer, X = 9
						probFactory.valueOf(0.1),
						// Y = winter, X = 3
						probFactory.valueOf(0.32),
						// Y = winter, X = 9
						probFactory.valueOf(0.45));
				List<RandomVariable> varsToMarginalize4 = Arrays.asList(rv3);
				testCases.add(new Object[] { test1, varsToMarginalize1, test1vars, test1values, clazz });
				testCases.add(new Object[] { test2, varsToMarginalize2, test1vars, test1values, clazz });
				testCases.add(new Object[] { test3, varsToMarginalize3, test2vars, test2values, clazz });
				testCases.add(new Object[] { test4, varsToMarginalize4, test2vars, test2values, clazz });
			}
			return testCases;
		}

		@Test
		public void testMarginalize() {
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			ProbabilityTable marginalized = newTable.marginalize(varsToMarginalize);
			assertThat(marginalized.getValues(), is(expectedMarginalizedValues));
		}
	}

	// Testcase 7
	@RunWith(Parameterized.class)
	public static class NormalizeAndGetSumTests {

		@Parameter
		public List<ProbabilityNumber> expectedNormalizedValues;

		@Parameter(1)
		public List<RandomVariable> vars;

		@Parameter(2)
		public List<ProbabilityNumber> values;

		@Parameter(3)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameter(4)
		public ProbabilityNumber expectedSumBeforeNormalization;

		@Parameter(5)
		public ProbabilityNumber expectedSumAfterNormalization;

		@Parameters
		public static Collection<Object[]> data() {
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(9, 3));
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("summer", "winter"));
			RandomVariable rv1 = new RandVar("X", ordinalDomain);
			RandomVariable rv2 = new RandVar("Y", arbitraryDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				List<RandomVariable> testVars = Arrays.asList(rv1, rv2);
				List<ProbabilityNumber> testValues = Arrays.asList(
						// X = 3, Y = summer
						probFactory.valueOf(new BigDecimal("0.7")),
						// X = 3, Y = winter
						probFactory.valueOf(0.8),
						// X = 9, Y = summer
						probFactory.valueOf(new BigDecimal("0.3")),
						// X = 9, Y = winter
						probFactory.valueOf(0.1));
				ProbabilityNumber normalizationFactor = probFactory.valueOf(1.9);
				ProbabilityNumber expectedSumBeforeNormalization = probFactory.valueOf(1.9);
				List<ProbabilityNumber> test = Arrays.asList(
						// X = 3, Y = summer
						probFactory.valueOf(0.7).divide(normalizationFactor),
						// X = 3, Y = winter
						probFactory.valueOf(new BigDecimal("0.8")).divide(normalizationFactor),
						// X = 9, Y = summer
						probFactory.valueOf(0.3).divide(normalizationFactor),
						// X = 9, Y = winter
						probFactory.valueOf(new BigDecimal("0.1")).divide(normalizationFactor));
				ProbabilityNumber expectedSumAfterNormalization = probFactory.valueOf(1.0);
				testCases.add(new Object[] { test, testVars, testValues, clazz, expectedSumBeforeNormalization,
						expectedSumAfterNormalization });
			}
			return testCases;
		}

		@Test
		public void testNormalize() {
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			ProbabilityTable normalized = newTable.normalize();
			assertThat(normalized.getValues(), is(expectedNormalizedValues));
		}

		@Test
		public void testGetSum() {
			ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
			assertEquals(newTable.getSum(), expectedSumBeforeNormalization);
			ProbabilityTable normalized = newTable.normalize();
			assertEquals(normalized.getSum(), expectedSumAfterNormalization);
		}
	}

	// Testcase 8
	@RunWith(Parameterized.class)
	public static class PointwiseProductTest {

		@Parameter
		public List<RandomVariable> vars1;

		@Parameter(1)
		public List<ProbabilityNumber> values1;

		@Parameter(2)
		public List<RandomVariable> vars2;

		@Parameter(3)
		public List<ProbabilityNumber> values2;

		@Parameter(4)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameter(5)
		public List<ProbabilityNumber> expected;

		@Parameters
		public static Collection<Object[]> data() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("Y", booleanDomain);
			RandomVariable rv3 = new RandVar("Z", booleanDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				List<RandomVariable> vars1Test1 = Arrays.asList(rv1, rv2);
				List<ProbabilityNumber> values1Test1 = Arrays.asList(
						// X = true, Y = true
						probFactory.valueOf(0.2),
						// X = true, Y = false
						probFactory.valueOf(0.3),
						// X = false, Y = true
						probFactory.valueOf(new BigDecimal("0.2")),
						// X = false, Y = false
						probFactory.valueOf(new BigDecimal("0.3")));
				List<RandomVariable> vars2Test1 = Arrays.asList(rv3);
				List<ProbabilityNumber> values2Test1 = Arrays.asList(
						// Z = true
						probFactory.valueOf(0.4),
						// Z = false
						probFactory.valueOf(0.6));
				List<ProbabilityNumber> expectedValuesTest1 = Arrays.asList(
						// X = true, Y = true, Z = true
						probFactory.valueOf(0.08),
						// X = true, Y = true, Z = false
						probFactory.valueOf(0.12),
						// X = true, Y = false, Z = true
						probFactory.valueOf(0.12),
						// X = true, Y = false, Z = false
						probFactory.valueOf(0.18),
						// X = false, Y = true, Z = true
						probFactory.valueOf(0.08),
						// X = false, Y = true, Z = false
						probFactory.valueOf(0.12),
						// X = false, Y = false, Z = true
						probFactory.valueOf(0.12),
						// X = false, Y = false, Z = false
						probFactory.valueOf(0.18));
				List<ProbabilityNumber> expectedValuesTest2 = Arrays.asList(
						// Z = true, X = true, Y = true
						probFactory.valueOf(0.08),
						// Z = true, X = true, Y = false
						probFactory.valueOf(0.12),
						// Z = true, X = false, Y = true
						probFactory.valueOf(0.08),
						// Z = true, X = false, Y = false
						probFactory.valueOf(0.12),
						// Z = false, X = true, Y = true
						probFactory.valueOf(0.12),
						// Z = false, X = true, Y = false
						probFactory.valueOf(0.18),
						// Z = false, X = false, Y = true
						probFactory.valueOf(0.12),
						// Z = false, X = false, Y = false
						probFactory.valueOf(0.18));
				List<RandomVariable> vars1Test3 = Arrays.asList(rv1, rv2);
				List<ProbabilityNumber> values1Test3 = Arrays.asList(
						// X = true, Y = true
						probFactory.valueOf(0.1),
						// X = true, Y = false
						probFactory.valueOf(0.2),
						// X = false, Y = true
						probFactory.valueOf(new BigDecimal("0.213")),
						// X = false, Y = false
						probFactory.valueOf(new BigDecimal("0.487")));
				List<RandomVariable> vars2Test3 = Arrays.asList(rv1);
				List<ProbabilityNumber> values2Test3 = Arrays.asList(
						// X = true
						probFactory.valueOf(0.3),
						// X = false
						probFactory.valueOf(0.7));
				List<ProbabilityNumber> expectedValuesTest3 = Arrays.asList(
						// X = true, Y = true
						probFactory.valueOf(0.03),
						// X = true, Y = false
						probFactory.valueOf(0.06),
						// X = false, Y = true
						probFactory.valueOf(0.1491),
						// X = false, Y = false
						probFactory.valueOf(0.3409));
				testCases.add(new Object[] { vars1Test1, values1Test1, vars2Test1, values2Test1, clazz,
						expectedValuesTest1 });
				testCases.add(new Object[] { vars2Test1, values2Test1, vars1Test1, values1Test1, clazz,
						expectedValuesTest2 });
				testCases.add(new Object[] { vars1Test3, values1Test3, vars2Test3, values2Test3, clazz,
						expectedValuesTest3 });
			}
			return testCases;
		}

		@Test
		public void testPointwiseProduct() {
			ProbabilityTable factor1 = new ProbabilityTable(vars1, values1, clazz);
			ProbabilityTable factor2 = new ProbabilityTable(vars2, values2, clazz);
			ProbabilityTable result = factor1.pointwiseProduct(factor2);
			assertThat(result.getValues(), is(expected));
		}
	}

	// Testcase 9
	@RunWith(Parameterized.class)
	public static class PointwiseProductPOSTest {

		@Parameter
		public List<RandomVariable> vars1;

		@Parameter(1)
		public List<ProbabilityNumber> values1;

		@Parameter(2)
		public List<RandomVariable> vars2;

		@Parameter(3)
		public List<ProbabilityNumber> values2;

		@Parameter(4)
		public List<RandomVariable> prodVarOrder;

		@Parameter(5)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameter(6)
		public List<ProbabilityNumber> expected;

		@Parameters
		public static Collection<Object[]> data() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("sunny", "rainy"));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("Y", booleanDomain);
			RandomVariable rv3 = new RandVar("Z", arbitraryDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				List<RandomVariable> vars1Test1 = Arrays.asList(rv1, rv2);
				List<ProbabilityNumber> values1Test1 = Arrays.asList(
						// X = true, Y = true
						probFactory.valueOf(0.2),
						// X = true, Y = false
						probFactory.valueOf(0.3),
						// X = false, Y = true
						probFactory.valueOf(new BigDecimal("0.2")),
						// X = false, Y = false
						probFactory.valueOf(new BigDecimal("0.3")));
				List<RandomVariable> vars2Test1 = Arrays.asList(rv3);
				List<ProbabilityNumber> values2Test1 = Arrays.asList(
						// Z = sunny
						probFactory.valueOf(0.4),
						// Z = rainy
						probFactory.valueOf(0.6));
				List<RandomVariable> posVarsTest1 = Arrays.asList(rv1, rv2, rv3);
				List<ProbabilityNumber> expectedValuesTest1 = Arrays.asList(
						// X = true, Y = true, Z = sunny
						probFactory.valueOf(0.08),
						// X = true, Y = true, Z = rainy
						probFactory.valueOf(0.12),
						// X = true, Y = false, Z = sunny
						probFactory.valueOf(0.12),
						// X = true, Y = false, Z = rainy
						probFactory.valueOf(0.18),
						// X = false, Y = true, Z = sunny
						probFactory.valueOf(0.08),
						// X = false, Y = true, Z = rainy
						probFactory.valueOf(0.12),
						// X = false, Y = false, Z = sunny
						probFactory.valueOf(0.12),
						// X = false, Y = false, Z = rainy
						probFactory.valueOf(0.18));
				List<RandomVariable> posVarsTest2 = Arrays.asList(rv3, rv1, rv2);
				List<ProbabilityNumber> expectedValuesTest2 = Arrays.asList(
						// Z = sunny, X = true, Y = true
						probFactory.valueOf(0.08),
						// Z = sunny, X = true, Y = false
						probFactory.valueOf(0.12),
						// Z = sunny, X = false, Y = true
						probFactory.valueOf(0.08),
						// Z = sunny, X = false, Y = false
						probFactory.valueOf(0.12),
						// Z = rainy, X = true, Y = true
						probFactory.valueOf(0.12),
						// Z = rainy, X = true, Y = false
						probFactory.valueOf(0.18),
						// Z = rainy, X = false, Y = true
						probFactory.valueOf(0.12),
						// Z = rainy, X = false, Y = false
						probFactory.valueOf(0.18));
				List<RandomVariable> posVarsTest3 = Arrays.asList(rv3, rv2, rv1);
				List<ProbabilityNumber> expectedValuesTest3 = Arrays.asList(
						// Z = sunny, Y = true, X = true
						probFactory.valueOf(0.08),
						// Z = sunny, Y = true, X = false
						probFactory.valueOf(0.08),
						// Z = sunny, Y = false, X = true
						probFactory.valueOf(0.12),
						// Z = sunny, Y = false, X = false
						probFactory.valueOf(0.12),
						// Z = rainy, Y = true, X = true
						probFactory.valueOf(0.12),
						// Z = rainy, Y = true, X = false
						probFactory.valueOf(0.12),
						// Z = rainy, Y = false, X = true
						probFactory.valueOf(0.18),
						// Z = rainy, Y = false, X = false
						probFactory.valueOf(0.18));
				testCases.add(new Object[] { vars1Test1, values1Test1, vars2Test1, values2Test1, posVarsTest1, clazz,
						expectedValuesTest1 });
				testCases.add(new Object[] { vars2Test1, values2Test1, vars1Test1, values1Test1, posVarsTest2, clazz,
						expectedValuesTest2 });
				testCases.add(new Object[] { vars1Test1, values1Test1, vars2Test1, values2Test1, posVarsTest3, clazz,
						expectedValuesTest3 });
			}
			return testCases;
		}

		@Test
		public void testPointwiseProduct() {
			ProbabilityTable factor1 = new ProbabilityTable(vars1, values1, clazz);
			ProbabilityTable factor2 = new ProbabilityTable(vars2, values2, clazz);
			ProbabilityTable result = factor1.pointwiseProductPOS(factor2, prodVarOrder);
			assertThat(result.getValues(), is(expected));
		}
	}
	
	// Testcase 10
	@RunWith(Parameterized.class)
	public static class DivideByTest {

		@Parameter
		public List<RandomVariable> vars1;

		@Parameter(1)
		public List<ProbabilityNumber> values1;

		@Parameter(2)
		public List<RandomVariable> vars2;

		@Parameter(3)
		public List<ProbabilityNumber> values2;

		@Parameter(4)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameter(5)
		public List<ProbabilityNumber> expected;

		@Parameters
		public static Collection<Object[]> data() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("Y", booleanDomain);
			RandomVariable rv3 = new RandVar("Z", booleanDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				List<RandomVariable> vars1Test1 = Arrays.asList(rv1, rv2, rv3);
				List<ProbabilityNumber> values1Test1 = Arrays.asList(
						// X = true, Y = true, Z = true
						probFactory.valueOf(0.2),
						// X = true, Y = true, Z = false
						probFactory.valueOf(0.2),
						// X = true, Y = false, Z = true
						probFactory.valueOf(0.1),
						// X = true, Y = false, Z = false
						probFactory.valueOf(0.1),
						// X = false, Y = true, Z = true
						probFactory.valueOf(0.1),
						// X = false, Y = true, Z = false
						probFactory.valueOf(0.1),
						// X = false, Y = false, Z = true
						probFactory.valueOf(0.1),
						// X = false, Y = false, Z = false
						probFactory.valueOf(0.1));
				List<RandomVariable> vars2Test1 = Arrays.asList(rv3);
				List<ProbabilityNumber> values2Test1 = Arrays.asList(
						// Z = true
						probFactory.valueOf(0.5),
						// Z = false
						probFactory.valueOf(0.5));
				List<ProbabilityNumber> expectedValuesTest1 = Arrays.asList(
						// X = true, Y = true, Z = true
						probFactory.valueOf(0.4),
						// X = true, Y = true, Z = false
						probFactory.valueOf(0.4),
						// X = true, Y = false, Z = true
						probFactory.valueOf(0.2),
						// X = true, Y = false, Z = false
						probFactory.valueOf(0.2),
						// X = false, Y = true, Z = true
						probFactory.valueOf(0.2),
						// X = false, Y = true, Z = false
						probFactory.valueOf(0.2),
						// X = false, Y = false, Z = true
						probFactory.valueOf(0.2),
						// X = false, Y = false, Z = false
						probFactory.valueOf(0.2));
				List<RandomVariable> vars1Test2 = Arrays.asList(rv2, rv3, rv1);
				List<ProbabilityNumber> values1Test2 = Arrays.asList(
						// Y = true, Z = true, X = true
						probFactory.valueOf(0.2),
						// Y = true, Z = true, X = false
						probFactory.valueOf(0.1),
						// Y = true, Z = false, X = true
						probFactory.valueOf(0.2),
						// Y = true, Z = false, X = false
						probFactory.valueOf(0.1),
						// Y = false, Z = true, X = true
						probFactory.valueOf(0.1),
						// Y = false, Z = true, X = false
						probFactory.valueOf(0.1),
						// Y = false, Z = false, X = true
						probFactory.valueOf(0.1),
						// Y = false, Z = false, X = false
						probFactory.valueOf(0.1));
				List<RandomVariable> vars2Test2 = Arrays.asList(rv3);
				List<ProbabilityNumber> values2Test2 = Arrays.asList(
						// Z = true
						probFactory.valueOf(0.5),
						// Z = false
						probFactory.valueOf(0.5));
				List<ProbabilityNumber> expectedValuesTest2 = Arrays.asList(
						// X = true, Y = true, Z = true
						probFactory.valueOf(0.4),
						// X = true, Y = true, Z = false
						probFactory.valueOf(0.2),
						// X = true, Y = false, Z = true
						probFactory.valueOf(0.4),
						// X = true, Y = false, Z = false
						probFactory.valueOf(0.2),
						// X = false, Y = true, Z = true
						probFactory.valueOf(0.2),
						// X = false, Y = true, Z = false
						probFactory.valueOf(0.2),
						// X = false, Y = false, Z = true
						probFactory.valueOf(0.2),
						// X = false, Y = false, Z = false
						probFactory.valueOf(0.2));
				List<RandomVariable> vars2Test3 = Arrays.asList(rv1);
				List<ProbabilityNumber> values2Test3 = Arrays.asList(
						// X = true
						probFactory.valueOf(0.5),
						// X = false
						probFactory.valueOf(0.5));
				testCases.add(new Object[] { vars1Test1, values1Test1, vars2Test1, values2Test1, clazz,
						expectedValuesTest1 });
				testCases.add(new Object[] { vars1Test2, values1Test2, vars2Test2, values2Test2, clazz,
						expectedValuesTest2 });
				testCases.add(new Object[] { vars1Test2, values1Test2, vars2Test3, values2Test3, clazz,
						expectedValuesTest2 });
			}
			return testCases;
		}
	
		@Test
		public void testDivideBy() {
			ProbabilityTable dividend = new ProbabilityTable(vars1, values1, clazz);
			ProbabilityTable divisor = new ProbabilityTable(vars2, values2, clazz);
			ProbabilityTable result = dividend.divideBy(divisor);
			assertThat(result.getValues(), is(expected));
		}
	}
}