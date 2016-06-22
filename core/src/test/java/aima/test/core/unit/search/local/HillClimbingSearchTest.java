package aima.test.core.unit.search.local;

import java.util.function.ToDoubleFunction;

import org.junit.Assert;
import org.junit.Test;

import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;
import aima.core.search.basic.local.HillClimbingSearch;

/**
 * @author Ciaran O'Reilly
 * @author Paul Anton
 */
public class HillClimbingSearchTest {

	// The state value function will be represented by the ascii value of the
	// first character in the state name.
	ToDoubleFunction<String> asciiChar0StateValueFn = state -> {
		return (double) state.charAt(0);
	};

	public <A, S> S searchForState(Problem<A, S> problem, ToDoubleFunction<S> stateValuFn) {
		SearchForStateFunction<A, S> searchForStateFunction = new HillClimbingSearch<A, S>(stateValuFn);
		return searchForStateFunction.apply(problem);
	}

	@Test
	public void testReachableGlobalMaximum() {
		Assert.assertEquals("Z", searchForState(ProblemFactory.getSimpleBinaryTreeProblem("F", "Z"), asciiChar0StateValueFn));
	}

	@Test
	public void testReachableLocalMaximum() {
		Assert.assertEquals("O", searchForState(ProblemFactory.getSimpleBinaryTreeProblem("A", "Z"), asciiChar0StateValueFn));
	}

	@Test
	public void testNoSuccessors() {
		Assert.assertEquals("P", searchForState(ProblemFactory.getSimpleBinaryTreeProblem("P", "Z"), asciiChar0StateValueFn));
	}
}
