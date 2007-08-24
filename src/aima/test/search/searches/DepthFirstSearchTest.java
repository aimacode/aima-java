package aima.test.search.searches;

import java.util.List;

import junit.framework.TestCase;
import aima.search.framework.GraphSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.nqueens.NQueensBoard;
import aima.search.nqueens.NQueensGoalTest;
import aima.search.nqueens.NQueensSuccessorFunction;
import aima.search.uninformed.DepthFirstSearch;

public class DepthFirstSearchTest extends TestCase {

	public void testDepthFirstSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(8),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new DepthFirstSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);
		List actions = agent.getActions();
		assertCorrectPlacement(actions);
		assertEquals("113", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

	}

	public void testDepthFirstUnSuccessfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(3),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new DepthFirstSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);
		List actions = agent.getActions();
		assertEquals(0, actions.size());
		assertEquals("6", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
	}

	private void assertCorrectPlacement(List actions) {
		assertEquals(8, actions.size());
		assertEquals("placeQueenAt 0  7", actions.get(0));
		assertEquals("placeQueenAt 1  3", actions.get(1));
		assertEquals("placeQueenAt 2  0", actions.get(2));
		assertEquals("placeQueenAt 3  2", actions.get(3));
		assertEquals("placeQueenAt 4  5", actions.get(4));
		assertEquals("placeQueenAt 5  1", actions.get(5));
		assertEquals("placeQueenAt 6  6", actions.get(6));
		assertEquals("placeQueenAt 7  4", actions.get(7));
	}

}
