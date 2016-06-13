package aima.test.core.unit.search.uninformed;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.example.ExampleBreadthFirstSearch;
import aima.core.search.basic.queue.GraphGoalTestedFirstQueueSearch;
import aima.core.search.basic.uninformed.BreadthFirstQueueSearch;
import aima.test.core.unit.search.support.ProblemFactory;

@RunWith(Parameterized.class)
public class BreadthFirstGraphSearchTest {

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> implementations() {
		return Arrays.asList(new Object[][] { { new ExampleBreadthFirstSearch<String, String>() },
				{ new BreadthFirstQueueSearch<String, String>(new GraphGoalTestedFirstQueueSearch<>()) } });
	}

	@Parameter
	public SearchForActionsFunction<String, String> breadthFirstGraphSearch;

	@Test
	public void testReachableSimpleBinaryTreeGoals() {

		Assert.assertEquals(Arrays.asList((String) null),
				breadthFirstGraphSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "A")));

		Assert.assertEquals(Arrays.asList("B"),
				breadthFirstGraphSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "B")));

		Assert.assertEquals(Arrays.asList("C"),
				breadthFirstGraphSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "C")));

		Assert.assertEquals(Arrays.asList("B", "E"),
				breadthFirstGraphSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "E")));

		Assert.assertEquals(Arrays.asList("C", "F", "L"),
				breadthFirstGraphSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "L")));
	}

	@Test
	public void testUnreachableSimpleBinaryTreeGoals() {
		Assert.assertEquals(Collections.<String> emptyList(),
				breadthFirstGraphSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("B", "A")));
	}
}
