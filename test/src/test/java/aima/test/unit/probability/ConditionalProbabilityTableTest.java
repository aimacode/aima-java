package aima.test.unit.probability;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
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
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import aima.extra.probability.BigDecimalProbabilityNumber;
import aima.extra.probability.DoubleProbabilityNumber;
import aima.extra.probability.LogProbabilityNumber;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandVar;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.RationalProbabilityNumber;
import aima.extra.probability.bayes.ConditionalProbabilityTable;
import aima.extra.probability.bayes.ProbabilityTable;
import aima.extra.probability.domain.Domain;
import aima.extra.probability.domain.FiniteArbitraryTokenDomain;
import aima.extra.probability.domain.FiniteBooleanDomain;
import aima.extra.probability.domain.FiniteOrdinalDomain;
import aima.extra.probability.factory.ProbabilityFactory;

/**
 * Suite of tests for the ConditionalProbabilityTable class.
 * 
 * @author Nagaraj Poti
 */
@RunWith(Enclosed.class)
public class ConditionalProbabilityTableTest {

	public static List<Class<? extends ProbabilityNumber>> clazzes = Arrays.asList(DoubleProbabilityNumber.class,
			BigDecimalProbabilityNumber.class, LogProbabilityNumber.class, RationalProbabilityNumber.class);

	// Test 1
	@RunWith(Parameterized.class)
	public static class InvalidConditionalProbabilityTableConstructionTest {

		@Parameter
		public RandomVariable on;

		@Parameter(1)
		public List<RandomVariable> parents;

		@Parameter(2)
		public List<ProbabilityNumber> values;

		@Parameter(3)
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
				RandomVariable test1on = rv1;
				List<RandomVariable> test1parents = Arrays.asList(rv2);
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
				RandomVariable test2on = rv1;
				List<RandomVariable> test2parents = Arrays.asList(rv3);
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

				testCases.add(new Object[] { test1on, test1parents, test1values, clazz });
				testCases.add(new Object[] { test2on, test2parents, test2values, clazz });
				testCases.add(new Object[] { null, null, test2values, clazz });
				testCases.add(new Object[] { null, test2parents, test2values, clazz });
				testCases.add(new Object[] { test2on, null, test2values, clazz });
				// testCases.add(new Object[] { test3on, test3parents,
				// test3values, clazz });
			}
			return testCases;
		}

		@Test(expected = Exception.class)
		public void testValidity() {
			new ConditionalProbabilityTable(on, values, parents, clazz);
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
				// Test cases with respect to ConditionalProbabilityTable
				// defined in the testContains() method
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
			RandomVariable rv3 = new RandVar("Z", booleanDomain);
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			RandomVariable on = rv1;
			List<RandomVariable> parents = Arrays.asList(rv2, rv3);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1, Z = true
					probFactory.valueOf(new BigDecimal("0.231")),
					// X = true, Y = 1, Z = false
					probFactory.valueOf(0.9),
					// X = true, Y = 2, Z = true
					probFactory.valueOf(0.1),
					// X = true, Y = 2, Z = false
					probFactory.valueOf(0.3),
					// X = true, Y = 3, Z = true
					probFactory.valueOf(0.11),
					// X = true, Y = 3, Z = false
					probFactory.valueOf(0.2),
					// X = false, Y = 1, Z = true
					probFactory.valueOf(new BigDecimal("0.769")),
					// X = false, Y = 1, Z = false
					probFactory.valueOf(0.1),
					// X = false, Y = 2, Z = true
					probFactory.valueOf(0.9),
					// X = false, Y = 2, Z = false
					probFactory.valueOf(0.7),
					// X = false, Y = 3, Z = true
					probFactory.valueOf(0.89),
					// X = false, Y = 3, Z = false
					probFactory.valueOf(0.8));
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
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
		public static Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(9, 8));
		public static RandomVariable rv1 = new RandVar("X", booleanDomain);
		public static RandomVariable rv2 = new RandVar("Y", arbitraryDomain);
		public static RandomVariable rv3 = new RandVar("Z", ordinalDomain);

		@Parameters
		public static Collection<Object[]> propositions() {
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				// Testcase 1
				Predicate<Map<RandomVariable, Object>> proposition1 = mp -> {
					return mp.get(rv1).equals(true) && mp.get(rv2).equals("windy") && mp.get(rv3).equals(9);
				};
				ProbabilityNumber expected1 = probFactory.valueOf(new BigDecimal("0.998"));
				// Testcase 2
				Predicate<Map<RandomVariable, Object>> proposition2 = mp -> {
					return mp.get(rv2).equals("rainy") && mp.get(rv3).equals(8) && mp.get(rv1).equals(false);
				};
				ProbabilityNumber expected2 = probFactory.valueOf(0.15);
				// Testcase 3
				Predicate<Map<RandomVariable, Object>> proposition3 = mp -> {
					return mp.get(rv1).equals(false) && mp.get(rv2).equals("windy") && mp.get(rv3).equals(1);
				};
				ProbabilityNumber expected3 = probFactory.valueOf(0.0);
				// Testcase 4 (Probability value does not reflect something
				// meaningful)
				Predicate<Map<RandomVariable, Object>> proposition4 = mp -> {
					return mp.get(rv1).equals(true) && mp.get(rv2).equals("rainy");
				};
				ProbabilityNumber expected4 = probFactory.valueOf(0.961);
				// Testcase 5 (Probability value does not reflect something
				// meaningful)
				Predicate<Map<RandomVariable, Object>> proposition5 = mp -> {
					return mp.get(rv1).equals(true);
				};
				ProbabilityNumber expected5 = probFactory.valueOf(2.309);
				// Testcase 6
				Predicate<Map<RandomVariable, Object>> proposition6 = mp -> {
					return !(mp.get(rv1).equals(true)) && mp.get(rv2).equals("windy") && mp.get(rv3).equals(9);
				};
				ProbabilityNumber expected6 = probFactory.valueOf(0.002);

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
			RandomVariable on = rv1;
			List<RandomVariable> parents = Arrays.asList(rv2, rv3);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = windy, Z = 8
					probFactory.valueOf(new BigDecimal("0.35")),
					// X = true, Y = windy, Z = 9
					probFactory.valueOf(new BigDecimal("0.998")),
					// X = true, Y = rainy, Z = 8
					probFactory.valueOf(new BigDecimal("0.85")),
					// X = true, Y = rainy, Z = 9
					probFactory.valueOf(new BigDecimal("0.111")),
					// X = false, Y = windy, Z = 8
					probFactory.valueOf(new BigDecimal("0.65")),
					// X = false, Y = windy, Z = 9
					probFactory.valueOf(new BigDecimal("0.002")),
					// X = false, Y = rainy, Z = 8
					probFactory.valueOf(0.15),
					// X = false, Y = rainy, Z = 9
					probFactory.valueOf(0.889));
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
			assertEquals(newTable.getValue(proposition), expected);
		}
	}

	// Test 4
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
				ProbabilityNumber expected2 = probFactory.valueOf(new BigDecimal("0.96597695769576957"));

				testCases.add(new Object[] { expected1, test1, test1Values, expectedIdx1, clazz });
				testCases.add(new Object[] { expected2, test2, test2Values, expectedIdx2, clazz });
			}
			return testCases;
		}

		@Test
		public void testGetValue() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			RandomVariable on = rv1;
			List<RandomVariable> parents = Arrays.asList(rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1
					probFactory.valueOf(new BigDecimal("0.00413938374738292")),
					// X = true, Y = 2
					probFactory.valueOf(new BigDecimal("0.03402304230423043")),
					// X = true, Y = 3
					probFactory.valueOf(new BigDecimal("0.32423423423423423")),
					// X = false, Y = 1
					probFactory.valueOf(new BigDecimal("0.99586061625261708")),
					// X = false, Y = 2
					probFactory.valueOf(new BigDecimal("0.96597695769576957")),
					// X = false, Y = 3
					probFactory.valueOf(new BigDecimal("0.67576576576576577")));
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
			assertEquals(newTable.getValue(event), expected);
			assertEquals(newTable.getValue(eventValues), expected);
		}

		@Test
		public void testGetIndex() {
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
			RandomVariable on = rv1;
			List<RandomVariable> parents = Arrays.asList(rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1
					probFactory.valueOf(new BigDecimal("0.00413938374738292")),
					// X = true, Y = 2
					probFactory.valueOf(new BigDecimal("0.03402304230423043")),
					// X = true, Y = 3
					probFactory.valueOf(new BigDecimal("0.32423423423423423")),
					// X = false, Y = 1
					probFactory.valueOf(new BigDecimal("0.99586061625261708")),
					// X = false, Y = 2
					probFactory.valueOf(new BigDecimal("0.96597695769576957")),
					// X = false, Y = 3
					probFactory.valueOf(new BigDecimal("0.67576576576576577")));
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
			assertEquals(newTable.getIndex(event), expectedIdx);
			assertEquals(newTable.getIndex(eventValues), expectedIdx);
		}
	}

	// Testcase 5
	@RunWith(Parameterized.class)
	public static class MarginalizeTest {

		@Parameter
		public List<ProbabilityNumber> expectedMarginalizedValues;

		@Parameter(1)
		public List<RandomVariable> varsToMarginalize;

		@Parameter(2)
		public RandomVariable on;

		@Parameter(3)
		public List<RandomVariable> parents;

		@Parameter(4)
		public List<ProbabilityNumber> values;

		@Parameter(5)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameters
		public static Collection<Object[]> data() {
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(9, 3));
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("summer", "winter"));
			RandomVariable rv1 = new RandVar("X", ordinalDomain);
			RandomVariable rv2 = new RandVar("Y", arbitraryDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				RandomVariable test1on = rv1;
				List<RandomVariable> test1parents = Arrays.asList(rv2);
				List<ProbabilityNumber> test1values = Arrays.asList(
						// X = 3, Y = summer
						probFactory.valueOf(0.772),
						// X = 3, Y = winter
						probFactory.valueOf(0.43242),
						// X = 9, Y = summer
						probFactory.valueOf(0.228),
						// X = 9, Y = winter
						probFactory.valueOf(0.56758));
				List<ProbabilityNumber> test1 = Arrays.asList(
						// Y = summer
						probFactory.valueOf(1.0),
						// Y = winter
						probFactory.valueOf(1.0));
				List<RandomVariable> varsToMarginalize1 = Arrays.asList(rv1);
				testCases.add(new Object[] { test1, varsToMarginalize1, test1on, test1parents, test1values, clazz });
			}
			return testCases;
		}

		@Test
		public void testMarginalize() {
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
			ProbabilityTable marginalized = newTable.marginalize(varsToMarginalize);
			assertThat(marginalized.getValues(), is(expectedMarginalizedValues));
		}
	}

	// Testcase 6
	@RunWith(Parameterized.class)
	public static class NormalizeTest {

		@Parameter
		public List<ProbabilityNumber> expectedNormalizedValues;

		@Parameter(1)
		public RandomVariable on;

		@Parameter(2)
		public List<RandomVariable> parents;

		@Parameter(3)
		public List<ProbabilityNumber> values;

		@Parameter(4)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameters
		public static Collection<Object[]> data() {
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(9, 3));
			Domain ordinalDomain2 = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("summer", "winter"));
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			RandomVariable rv1 = new RandVar("X", ordinalDomain);
			RandomVariable rv2 = new RandVar("Y", arbitraryDomain);
			RandomVariable rv3 = new RandVar("Z", booleanDomain);
			RandomVariable rv4 = new RandVar("A", ordinalDomain2);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				RandomVariable test1on = rv1;
				List<RandomVariable> test1parents = Arrays.asList(rv2);
				List<ProbabilityNumber> test1values = Arrays.asList(
						// X = 3, Y = summer
						probFactory.valueOf(new BigDecimal("0.93")),
						// X = 3, Y = winter
						probFactory.valueOf(0.80),
						// X = 9, Y = summer
						probFactory.valueOf(new BigDecimal("0.30")),
						// X = 9, Y = winter
						probFactory.valueOf(0.10));
				ProbabilityNumber normalizationFactor1 = probFactory.valueOf(1.23);
				ProbabilityNumber normalizationFactor2 = probFactory.valueOf(0.9);
				List<ProbabilityNumber> test1 = Arrays.asList(
						// X = 3, Y = summer
						probFactory.valueOf(0.93).divide(normalizationFactor1),
						// X = 3, Y = winter
						probFactory.valueOf(new BigDecimal("0.80")).divide(normalizationFactor2),
						// X = 9, Y = summer
						probFactory.valueOf(0.3).divide(normalizationFactor1),
						// X = 9, Y = winter
						probFactory.valueOf(new BigDecimal("0.10")).divide(normalizationFactor2));
				RandomVariable test2on = rv3;
				List<RandomVariable> test2parents = Arrays.asList(rv4);
				List<ProbabilityNumber> test2values = Arrays.asList(
						// Y = true, X = 1
						probFactory.valueOf(0.2),
						// Y = true, X = 2
						probFactory.valueOf(0.1),
						// Y = true, X = 3
						probFactory.valueOf(0.3),
						// Y = false, X = 1
						probFactory.valueOf(0.8),
						// Y = false, X = 2
						probFactory.valueOf(0.9),
						// Y = false, X = 3
						probFactory.valueOf(0.3));
				ProbabilityNumber normalizationFactor3 = probFactory.valueOf(0.6);
				List<ProbabilityNumber> test2 = Arrays.asList(
						// Y = true, X = 1
						probFactory.valueOf(0.2),
						// Y = true, X = 2
						probFactory.valueOf(0.1),
						// Y = true, X = 3
						probFactory.valueOf(0.3).divide(normalizationFactor3),
						// Y = false, X = 1
						probFactory.valueOf(0.8),
						// Y = false, X = 2
						probFactory.valueOf(0.9),
						// Y = false, X = 3
						probFactory.valueOf(0.3).divide(normalizationFactor3));
				testCases.add(new Object[] { test1, test1on, test1parents, test1values, clazz });
				testCases.add(new Object[] { test2, test2on, test2parents, test2values, clazz });
			}
			return testCases;
		}

		@Test
		public void testNormalize() {
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
			assertThat(newTable.getValues(), is(expectedNormalizedValues));
		}
	}

	// Testcase 7
	@RunWith(Parameterized.class)
	public static class GetConditioningCaseTest {

		@Parameter
		public List<ProbabilityNumber> expectedConditionedValues;

		@Parameter(1)
		public RandomVariable on;

		@Parameter(2)
		public List<RandomVariable> parents;

		@Parameter(3)
		public List<ProbabilityNumber> values;

		@Parameter(4)
		public Class<? extends ProbabilityNumber> clazz;

		@Parameter(5)
		public Map<RandomVariable, Object> parentWorld;

		@Parameter(6)
		public Predicate<Map<RandomVariable, Object>> proposition;

		@Parameters
		public static Collection<Object[]> data() {
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(9, 3));
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("happy", "sad"));
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			RandomVariable rv1 = new RandVar("X", arbitraryDomain);
			RandomVariable rv2 = new RandVar("Y", ordinalDomain);
			RandomVariable rv3 = new RandVar("Z", booleanDomain);
			Collection<Object[]> testCases = new ArrayList<>();
			for (Class<? extends ProbabilityNumber> clazz : clazzes) {
				ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
				RandomVariable teston = rv1;
				List<RandomVariable> testparents = Arrays.asList(rv2, rv3);
				List<ProbabilityNumber> testvalues = Arrays.asList(
						// X = happy, Y = 3, Z = true
						probFactory.valueOf(0.11),
						// X = happy, Y = 3, Z = false
						probFactory.valueOf(0.55),
						// X = happy, Y = 9, Z = true
						probFactory.valueOf(0.4),
						// X = happy, Y = 9, Z = false
						probFactory.valueOf(0.1),
						// X = sad, Y = 3, Z = true
						probFactory.valueOf(0.89),
						// X = sad, Y = 3, Z = false
						probFactory.valueOf(0.45),
						// X = sad, Y = 9, Z = true
						probFactory.valueOf(0.6),
						// X = sad, Y = 9, Z = false
						probFactory.valueOf(0.9));
				List<ProbabilityNumber> expectedTest1 = Arrays.asList(
						// X = happy
						probFactory.valueOf(0.11),
						// X = sad
						probFactory.valueOf(0.89));
				Map<RandomVariable, Object> test1 = new HashMap<RandomVariable, Object>();
				test1.put(rv2, 3);
				test1.put(rv3, true);
				Predicate<Map<RandomVariable, Object>> proposition1 = mp -> {
					return mp.get(rv2).equals(3) && mp.get(rv3).equals(true);
				};
				List<ProbabilityNumber> expectedTest2 = Arrays.asList(
						// X = happy
						probFactory.valueOf(0.1),
						// X = sad
						probFactory.valueOf(0.9));
				Map<RandomVariable, Object> test2 = new HashMap<RandomVariable, Object>();
				test2.put(rv2, 9);
				test2.put(rv3, false);
				Predicate<Map<RandomVariable, Object>> proposition2 = mp -> {
					return mp.get(rv2).equals(9) && mp.get(rv3).equals(false);
				};
				testCases.add(
						new Object[] { expectedTest1, teston, testparents, testvalues, clazz, test1, proposition1 });
				testCases.add(
						new Object[] { expectedTest2, teston, testparents, testvalues, clazz, test2, proposition2 });
			}
			return testCases;
		}

		@Test
		public void testGetConditioningCaseMap() {
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
			ProbabilityTable conditioned = newTable.getConditioningCase(parentWorld);
			assertThat(conditioned.getValues(), is(expectedConditionedValues));
		}

		@Test
		public void testGetConditioningCaseProposition() {
			ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(on, values, parents, clazz);
			ProbabilityTable conditioned = newTable.getConditioningCase(proposition);
			assertThat(conditioned.getValues(), is(expectedConditionedValues));
		}
	}
}
