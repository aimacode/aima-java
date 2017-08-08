package aima.test.unit.probability;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;
import org.junit.experimental.runners.Enclosed;
import aima.extra.probability.RandVar;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.constructs.ProbabilityUtilities;
import aima.extra.probability.domain.Domain;
import aima.extra.probability.domain.FiniteArbitraryTokenDomain;
import aima.extra.probability.domain.FiniteBooleanDomain;
import aima.extra.probability.domain.FiniteOrdinalDomain;

/**
 * Tests for the ProbabilityUtilities class.
 * 
 * @author Nagaraj Poti
 */
@RunWith(Enclosed.class)
public class ProbabilityUtilitiesTest {

	// Test 1
	@RunWith(Parameterized.class)
	public static class RandomVariableNameTest1 {

		@Parameter
		public String RVName;

		@Parameters
		public static Object[] invalidRandomVariableNames() {
			return new Object[] { null, "a ", " b", "_A1", "B\ta\t\nf", "B___\n?", "Aa \tb c d e", "12asb", "33",
					"-A\t\b", "-_-" };
		}

		@Test(expected = IllegalArgumentException.class)
		public void testValidity() {
			ProbabilityUtilities.checkValidRandomVariableName(RVName);
		}
	}

	// Test 2
	@RunWith(Parameterized.class)
	public static class RandomVariableNameTest2 {

		@Parameter
		public String RVName;

		@Parameters
		public static Object[] validRandomVariableNames() {
			return new Object[] { "A", "A1", "A1_2", "A_a" };
		}

		@Test
		public void testValidity() {
			assertTrue(ProbabilityUtilities.checkValidRandomVariableName(RVName));
		}
	}

	// Test 3
	@RunWith(Parameterized.class)
	public static class ProbabilityTableSizeTest {

		@Parameter
		public int expectedSize;

		@Parameter(1)
		public List<RandomVariable> vars;

		@Parameters
		public static Object[][] data() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList(1, "sunny", 3.5));
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("Y", booleanDomain);
			RandomVariable rv3 = new RandVar("Z", booleanDomain);
			RandomVariable rv4 = new RandVar("A", arbitraryDomain);
			RandomVariable rv5 = new RandVar("B", ordinalDomain);
			List<RandomVariable> test1 = Arrays.asList(rv1, rv2, rv3);
			List<RandomVariable> test2 = Arrays.asList(rv3, rv4);
			List<RandomVariable> test3 = Arrays.asList(rv4, rv5);
			return new Object[][] { { 8, test1 }, { 6, test2 }, { 9, test3 } };
		}

		@Test
		public void testValidity() {
			assertEquals(ProbabilityUtilities.expectedSizeofProbabilityTable(vars), expectedSize);
		}
	}

	// Test 4
	@RunWith(Parameterized.class)
	public static class CheckFinitenessOfRandomVariables {

		@Parameter
		public boolean expected;

		@Parameter(1)
		public List<RandomVariable> vars;

		@Parameters
		public static Object[][] data() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain arbitraryDomain = new FiniteArbitraryTokenDomain(Arrays.asList(1, "windy", 3.5, 44));
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("Y", arbitraryDomain);
			RandomVariable rv3 = new RandVar("Z", ordinalDomain);
			List<RandomVariable> test1 = Arrays.asList(rv1, rv2, rv3);
			List<RandomVariable> test2 = Arrays.asList(rv3, null);
			return new Object[][] { { true, test1 }, { false, test2 } };
		}

		@Test
		public void testValidity() {
			assertEquals(ProbabilityUtilities.checkIfFiniteRandomVariables(vars), expected);
		}
	}
	
	// Test 5
	@RunWith(Parameterized.class)
	public static class CheckUniquenessOfRandomVariables {

		@Parameter
		public boolean expected;

		@Parameter(1)
		public List<RandomVariable> vars;

		@Parameters
		public static Object[][] testRandomVariables() {
			Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
			Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
			RandomVariable rv1 = new RandVar("X", booleanDomain);
			RandomVariable rv2 = new RandVar("X", ordinalDomain);
			RandomVariable rv3 = new RandVar("Y", ordinalDomain);
			List<RandomVariable> test1 = Arrays.asList(rv1, rv2, rv3);
			List<RandomVariable> test2 = Arrays.asList(rv1, rv2);
			List<RandomVariable> test3 = Arrays.asList(rv2, rv3);
			return new Object[][] { { false, test1 }, { false, test2 }, { true, test3 },
					{ true, null } };
		}

		@Test
		public void testValidity() {
			assertEquals(ProbabilityUtilities.checkIfRandomVariablesAreUnique(vars), expected);
		}
	}
}
