package aima.test.core.unit.search.informed;

import org.junit.Assert;
import org.junit.Test;

import aima.core.search.eightpuzzle.EightPuzzleBoard;
import aima.core.search.eightpuzzle.EightPuzzleGoalTest;
import aima.core.search.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.search.eightpuzzle.ManhattanHeuristicFunction;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.GreedyBestFirstSearch;

public class GreedyBestFirstSearchTest {

	@Test
	public void testGreedyBestFirstSearch() {
		try {
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {2,0,5,6,4,8,3,7,1});
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {0,8,7,6,5,4,3,2,1});
			EightPuzzleBoard board = new EightPuzzleBoard(new int[] { 7, 1, 8,
					0, 4, 6, 2, 3, 5 });

			Problem problem = new Problem(board,
					EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(),
					new EightPuzzleGoalTest(), new ManhattanHeuristicFunction());
			Search search = new GreedyBestFirstSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			Assert.assertEquals(81, agent.getActions().size());
			Assert.assertEquals("304", agent.getInstrumentation().getProperty(
					"nodesExpanded"));
			Assert.assertEquals("268", agent.getInstrumentation().getProperty(
					"queueSize"));
			Assert.assertEquals("269", agent.getInstrumentation().getProperty(
					"maxQueueSize"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception thrown.");
		}
	}
}
