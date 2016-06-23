package aima.test.core.unit.search.local;

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
import aima.core.search.basic.example.ExampleHillClimbingSearch;
import aima.core.search.basic.local.HillClimbingSearch;

/**
 * @author Ciaran O'Reilly
 * @author Paul Anton
 */
@RunWith(Parameterized.class)
public class HillClimbingSearchTest {
	
	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> implementations() {
		return Arrays.asList(new Object[][] { { "ExampleHillClimbingSearch" },
				{ "HillClimbingSearch" }});
	}
	
	@Parameter
	public String searchForStateFunctionName;

	// The state value function will be represented by the ascii value of the
	// first character in the state name.
	ToDoubleFunction<String> asciiChar0StateValueFn = state -> {
		return (double) state.charAt(0);
	};

	public <A, S> S searchForState(Problem<A, S> problem, ToDoubleFunction<S> stateValuFn, boolean isSteepestAscentVersion) {
		SearchForStateFunction<A, S> searchForStateFunction;
		
		if ("ExampleHillClimbingSearch".equals(searchForStateFunctionName)) {
			searchForStateFunction = new ExampleHillClimbingSearch<A, S>(stateValuFn, isSteepestAscentVersion);
		}
		else {
			searchForStateFunction = new HillClimbingSearch<A, S>(stateValuFn, isSteepestAscentVersion);
		}
		return searchForStateFunction.apply(problem);
	}

	@Test
	public void testReachableGlobalMaximum() {
		Assert.assertEquals("Z", searchForState(ProblemFactory.getSimpleBinaryTreeProblem("F", "Z"), asciiChar0StateValueFn, true));
	}

	@Test
	public void testReachableLocalMaximum() {
		Assert.assertEquals("O", searchForState(ProblemFactory.getSimpleBinaryTreeProblem("A", "Z"), asciiChar0StateValueFn, true));
	}

	@Test
	public void testNoSuccessors() {
		Assert.assertEquals("P", searchForState(ProblemFactory.getSimpleBinaryTreeProblem("P", "Z"), asciiChar0StateValueFn, true));
		Assert.assertEquals("F", searchForState(ProblemFactory.getSimpleBinaryTreeProblem("F", "Z"), asciiChar0StateValueFn, false));
	}
}
