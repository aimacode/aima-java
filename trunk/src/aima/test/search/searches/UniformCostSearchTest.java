package aima.test.search.searches;

import java.util.List;

import junit.framework.TestCase;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.GraphSearch;
import aima.search.nqueens.NQueensBoard;
import aima.search.nqueens.NQueensGoalTest;
import aima.search.nqueens.NQueensSuccessorFunction;
import aima.search.uninformed.UniformCostSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class UniformCostSearchTest extends TestCase {

	public void testUniformCostSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(8),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new UniformCostSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);

		List actions = agent.getActions();

		assertEquals(8, actions.size());

		assertEquals("1965", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

		assertEquals("8.0", agent.getInstrumentation().getProperty("pathCost"));
	}

	public void testUniformCostUnSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(3),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new UniformCostSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);

		List actions = agent.getActions();

		assertEquals(0, actions.size());

		assertEquals("6", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

		// Will be 0 as did not reach goal state.
		assertEquals("0", agent.getInstrumentation().getProperty("pathCost"));
	}
}
