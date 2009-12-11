/*
 * Created on Sep 28, 2005
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
import aima.search.informed.AStarSearch;

public class AStarSearchTest extends TestCase {
	public void testAStarSearch() {
		// added to narrow down bug report filed by L.N.Sudarshan of
		// Thoughtworks and Xin Lu of UCI
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
			Search search = new AStarSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			assertEquals(23, agent.getActions().size());
			assertEquals("906", agent.getInstrumentation().getProperty(
					"nodesExpanded"));
			assertEquals("914", agent.getInstrumentation().getProperty(
					"queueSize"));
			assertEquals("920", agent.getInstrumentation().getProperty(
					"maxQueueSize"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
