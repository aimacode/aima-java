package aima.test.core.unit.search.uninformed;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.nqueens.NQueensBoard;
import aima.core.search.nqueens.NQueensGoalTest;
import aima.core.search.nqueens.NQueensSuccessorFunction;
import aima.core.search.uninformed.UniformCostSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class UniformCostSearchTest {

	@Test
	public void testUniformCostSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(8),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new UniformCostSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);

		List actions = agent.getActions();

		Assert.assertEquals(8, actions.size());

		Assert.assertEquals("1965", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

		Assert.assertEquals("8.0", agent.getInstrumentation().getProperty(
				"pathCost"));
	}

	public void testUniformCostUnSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(3),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new UniformCostSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);

		List actions = agent.getActions();

		Assert.assertEquals(0, actions.size());

		Assert.assertEquals("6", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

		// Will be 0 as did not reach goal state.
		Assert.assertEquals("0", agent.getInstrumentation().getProperty(
				"pathCost"));
	}
}
