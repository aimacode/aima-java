package aima.test.search.searches;

import java.util.List;

import junit.framework.TestCase;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.nqueens.NQueensBoard;
import aima.search.nqueens.NQueensGoalTest;
import aima.search.nqueens.NQueensSuccessorFunction;
import aima.search.uninformed.IterativeDeepeningSearch;

public class IterativeDeepeningSearchTest extends TestCase {
	public void testIterativeDeepeningSearch() {
		try {
			Problem problem = new Problem(new NQueensBoard(8),
					new NQueensSuccessorFunction(), new NQueensGoalTest());
			Search search = new IterativeDeepeningSearch();
			SearchAgent agent = new SearchAgent(problem, search);
			List actions = agent.getActions();
			assertCorrectPlacement(actions);
			assertEquals("3656", agent.getInstrumentation().getProperty(
					"nodesExpanded"));

		} catch (Exception e) {
			fail("Exception should not occur");
		}
	}

	private void assertCorrectPlacement(List actions) {
		assertEquals(8, actions.size());
		assertEquals("placeQueenAt 0  0", actions.get(0));
		assertEquals("placeQueenAt 1  4", actions.get(1));
		assertEquals("placeQueenAt 2  7", actions.get(2));
		assertEquals("placeQueenAt 3  5", actions.get(3));
		assertEquals("placeQueenAt 4  2", actions.get(4));
		assertEquals("placeQueenAt 5  6", actions.get(5));
		assertEquals("placeQueenAt 6  1", actions.get(6));
		assertEquals("placeQueenAt 7  3", actions.get(7));
	}
}