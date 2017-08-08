package aima.test.unit.probability;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.Arrays;
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
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandVar;
import aima.extra.probability.RandomVariable;
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
		public static Object[][] testRandomVariables() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("X", ordinalDomain);
			RandomVariable rv3 = new RandVar("Y", booleanDomain);
			Class<? extends ProbabilityNumber> clazz = DoubleProbabilityNumber.class;
			ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
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
			List<RandomVariable> test2vars = Arrays.asList(rv1, rv3);
			// Invalid values size
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
			return new Object[][] { { test1vars, test1values, clazz }, { test2vars, test2values, clazz },
					{ null, null, clazz }, { null, test2values, clazz }, { test2vars, test2values, null } };
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
		public static Object[][] testRandomVariables() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("X", ordinalDomain);
			RandomVariable rv3 = new RandVar("Y", ordinalDomain);
			Class<? extends ProbabilityNumber> clazz = DoubleProbabilityNumber.class;
			return new Object[][] { { true, clazz, rv1 }, { false, clazz, rv2 }, { true, clazz, rv3 },
					{ false, clazz, null } };
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

		public static Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
		public static Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList("windy", "rainy"));
		public static RandomVariable rv1 = new RandVar("X", booleanDomain);
		public static RandomVariable rv2 = new RandVar("Y", arbitraryDomain);
		public static Class<? extends ProbabilityNumber> clazz = DoubleProbabilityNumber.class;
		public static ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);

		@Parameters
		public static Object[][] propositions() {
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
			return new Object[][] { { expected1, proposition1 }, { expected2, proposition2 },
					{ expected3, proposition3 }, { expected4, proposition4 }, { expected5, proposition5 },
					{ expected6, proposition6 } };
		}

		@Test
		public void testGetValue() {
			List<RandomVariable> vars = Arrays.asList(rv1, rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = windy
					probFactory.valueOf(new BigDecimal("0.35")),
					// X = true, Y = rainy
					probFactory.valueOf(new BigDecimal("0.32")),
					// X = false, Y = windy
					probFactory.valueOf(new BigDecimal(0.27)),
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

		public static Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
		public static Domain ordinalDomain = new FiniteOrdinalDomain<Integer>(Arrays.asList(1, 3, 2, 9));
		public static RandomVariable rv1 = new RandVar("X", booleanDomain);
		public static RandomVariable rv2 = new RandVar("Y", ordinalDomain);
		public static Class<? extends ProbabilityNumber> clazz = BigDecimalProbabilityNumber.class;
		public static ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);

		@Parameters
		public static Object[][] propositions() {
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
			return new Object[][] { { expected1, proposition1 }, { expected2, proposition2 },
					{ expected3, proposition3 }, { expected4, proposition4 }, { expected5, proposition5 },
					{ expected6, proposition6 } };
		}

		@Test
		public void testGetValue() {
			List<RandomVariable> vars = Arrays.asList(rv1, rv2);
			List<ProbabilityNumber> values = Arrays.asList(
					// X = true, Y = 1
					probFactory.valueOf(new BigDecimal("0.01")),
					// X = true, Y = 2
					probFactory.valueOf(new BigDecimal("0.07")),
					// X = true, Y = 3
					probFactory.valueOf(new BigDecimal(0.27)),
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

		public static Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
		public static Domain ordinalDomain = new FiniteOrdinalDomain<Integer>(Arrays.asList(3, 2, 1));
		public static RandomVariable rv1 = new RandVar("X", booleanDomain);
		public static RandomVariable rv2 = new RandVar("Y", ordinalDomain);
		public static Class<? extends ProbabilityNumber> clazz = BigDecimalProbabilityNumber.class;
		public static ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
		public static List<RandomVariable> vars = Arrays.asList(rv1, rv2);
		public static List<ProbabilityNumber> values = Arrays.asList(
				// X = true, Y = 1
				probFactory.valueOf(new BigDecimal("0.0041393837473829292473892")),
				// X = true, Y = 2
				probFactory.valueOf(new BigDecimal("0.0340230423042304324023400")),
				// X = true, Y = 3
				probFactory.valueOf(new BigDecimal("0.32423423423423423432423")),
				// X = false, Y = 1
				probFactory.valueOf(new BigDecimal("0.2342343444343434")),
				// X = false, Y = 2
				probFactory.valueOf(new BigDecimal("0.1111111111111111")),
				// X = false, Y = 3
				probFactory.valueOf(new BigDecimal("0.292257884168698")));
		public static ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
	
		
		@Parameters
		public static Object[][] eventValues() {
			// Testcase 1
			// Map<RandomVariable, Object> argument
			Map<RandomVariable, Object> test1 = new HashMap<RandomVariable, Object>();
			test1.put(rv1, true);
			test1.put(rv2, 3);
			// Object[] argument
			Object[] test1Values = new Object[] { true, 3 };
			ProbabilityNumber expected1 = probFactory.valueOf(new BigDecimal("0.32423423423423423432423"));
			int expectedIdx1 = 2;
			// Testcase 2
			// Map<RandomVariable, Object> argument
			Map<RandomVariable, Object> test2 = new HashMap<RandomVariable, Object>();
			test2.put(rv2, 2);
			test2.put(rv1, false);
			// Object[] argument
			Object[] test2Values = new Object[] { false, 2 };
			int expectedIdx2 = 4;
			ProbabilityNumber expected2 = probFactory.valueOf(new BigDecimal("0.1111111111111111"));
			return new Object[][] { { expected1, test1, test1Values, expectedIdx1 }, { expected2, test2, test2Values, expectedIdx2 } };
		}

		@Test
		public void testGetValue() {
			assertEquals(newTable.getValue(event), expected);
			assertEquals(newTable.getValue(eventValues), expected);
		}
		
		@Test
		public void testGetIndex() {
			assertEquals(newTable.getIndex(event), expectedIdx);
			assertEquals(newTable.getIndex(eventValues), expectedIdx);
		}
	}
}