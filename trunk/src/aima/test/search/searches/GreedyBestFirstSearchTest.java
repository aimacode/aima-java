/*
 * Created on Sep 29, 2005
 *
 */
package aima.test.search.searches;

import junit.framework.TestCase;
import aima.search.eightpuzzle.EightPuzzleBoard;
import aima.search.eightpuzzle.EightPuzzleGoalTest;
import aima.search.eightpuzzle.EightPuzzleSuccessorFunction;
import aima.search.eightpuzzle.ManhattanHeuristicFunction;
import aima.search.framework.GraphSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.GreedyBestFirstSearch;

public class GreedyBestFirstSearchTest extends TestCase {
	public void testGreedyBestFirstSearch() {
		try {
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {2,0,5,6,4,8,3,7,1});
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {0,8,7,6,5,4,3,2,1});
			EightPuzzleBoard board = new EightPuzzleBoard(new int[] { 7, 1, 8,
					0, 4, 6, 2, 3, 5 });

			Problem problem = new Problem(board,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest(), new ManhattanHeuristicFunction());
			Search search = new GreedyBestFirstSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			assertEquals(81, agent.getActions().size());
			assertEquals("304", agent.getInstrumentation().getProperty(
					"nodesExpanded"));
			assertEquals("268", agent.getInstrumentation().getProperty(
					"queueSize"));
			assertEquals("269", agent.getInstrumentation().getProperty(
					"maxQueueSize"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
