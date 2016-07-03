package aima.test.unit.search.local;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.ToDoubleFunction;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;
import aima.core.search.basic.local.HillClimbingSearch;
import aima.core.util.datastructure.Pair;
import aima.extra.search.local.HillClimbingSearchWithSidewaysMoves;

/**
 * @author Ciaran O'Reilly
 * @author Paul Anton
 */
@RunWith(Parameterized.class)
public class HillClimbingSearchTest {

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> implementations() {
		return Arrays.asList(new Object[][] { { "HillClimbingSearch" }, { "HillClimbingSearchWithSidewaysMoves" } });
	}

	@Parameter
	public String searchForStateFunctionName;

	// The state value function will be represented by the ascii value of the
	// first character in the state name.
	ToDoubleFunction<String> asciiChar0StateValueFn = state -> {
		return (double) state.charAt(0);
	};

	ToDoubleFunction<Pair<Integer, Integer>> y_valueFn = x_y -> x_y.getSecond().doubleValue();

	public <A, S> S searchForState(Problem<A, S> problem, ToDoubleFunction<S> stateValueFn,
			boolean isSteepestAscentVersion) {
		SearchForStateFunction<A, S> searchForStateFunction;

		if ("HillClimbingSearch".equals(searchForStateFunctionName)) {
			searchForStateFunction = new HillClimbingSearch<A, S>(stateValueFn, isSteepestAscentVersion);
		} else {
			searchForStateFunction = new HillClimbingSearchWithSidewaysMoves<A, S>(stateValueFn, isSteepestAscentVersion);
		}
		return searchForStateFunction.apply(problem);
	}

	@Test
	public void testReachableGlobalMaximum() {
		Assert.assertEquals("Z",
				searchForState(ProblemFactory.getSimpleBinaryTreeProblem("F", "Z"), asciiChar0StateValueFn, true));
		
		Assert.assertEquals(ProblemFactory.DEFAULT_DISCRETE_FUNCTION_GLOBAL_MAXIMIM,
				searchForState(ProblemFactory.getDefaultSimpleDiscreteFunctionProblem(6, true), y_valueFn, true)
						.getSecond().intValue());
		
		Assert.assertEquals(ProblemFactory.DEFAULT_DISCRETE_FUNCTION_GLOBAL_MAXIMIM,
				searchForState(ProblemFactory.getDefaultSimpleDiscreteFunctionProblem(12, true), y_valueFn, true)
						.getSecond().intValue());
	}

	@Test
	public void testReachableLocalMaximum() {
		Assert.assertEquals("O",
				searchForState(ProblemFactory.getSimpleBinaryTreeProblem("A", "Z"), asciiChar0StateValueFn, true));
		
		Assert.assertEquals(ProblemFactory.DEFAULT_DISCRETE_FUNCTION_LOCAL_MAXIMUM_VALUE,
				searchForState(ProblemFactory.getDefaultSimpleDiscreteFunctionProblem(14, true), y_valueFn, true)
						.getSecond().intValue());
	}

	@Test
	public void testNoSuccessors() {
		Assert.assertEquals("P",
				searchForState(ProblemFactory.getSimpleBinaryTreeProblem("P", "Z"), asciiChar0StateValueFn, true));
		Assert.assertEquals("F",
				searchForState(ProblemFactory.getSimpleBinaryTreeProblem("F", "Z"), asciiChar0StateValueFn, false));
		
		Assert.assertEquals(ProblemFactory.DEFAULT_DISCRETE_FUNCTION_LOCAL_MAXIMUM_VALUE,
				searchForState(ProblemFactory.getDefaultSimpleDiscreteFunctionProblem(16, true), y_valueFn, true)
						.getSecond().intValue());
	}
	
	@Test
	public void testSidewaysMoves() {
		if ("HillClimbingSearch".equals(searchForStateFunctionName)) {
			// Not supported by simple example implementation
			Assert.assertEquals(ProblemFactory.DEFAULT_DISCRETE_FUNCTION_SHOULDER_VALUE,
					searchForState(ProblemFactory.getDefaultSimpleDiscreteFunctionProblem(3, true), y_valueFn, true)
							.getSecond().intValue());
		}
		// Supported by alternative HillClimbing search implementation
		else {
			Assert.assertEquals(ProblemFactory.DEFAULT_DISCRETE_FUNCTION_GLOBAL_MAXIMIM,
					searchForState(ProblemFactory.getDefaultSimpleDiscreteFunctionProblem(3, true), y_valueFn, true)
							.getSecond().intValue());
		}
	}
}
