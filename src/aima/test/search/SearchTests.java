package aima.test.search;

import junit.framework.Test;
import junit.framework.TestSuite;
import aima.test.search.csp.AssignmentTest;
import aima.test.search.csp.DomainsTest;
import aima.test.search.csp.MapCSPTest;
import aima.test.search.eightpuzzle.EightPuzzleBoardMoveTest;
import aima.test.search.eightpuzzle.EightPuzzleBoardTest;
import aima.test.search.eightpuzzle.EightPuzzleSuccessorFunctionTest;
import aima.test.search.map.MapAgentTest;
import aima.test.search.map.MapEnvironmentTest;
import aima.test.search.map.MapStepCostFunctionTest;
import aima.test.search.map.MapSuccessorFunctionTest;
import aima.test.search.map.MapTest;
import aima.test.search.nqueens.NQueensBoardTest;
import aima.test.search.nqueens.NQueensFitnessFunctionTest;
import aima.test.search.nqueens.NQueensGoalTestTest;
import aima.test.search.nqueens.NQueensSuccessorFunctionTest;
import aima.test.search.online.OnlineDFSAgentTest;
import aima.test.search.searches.AStarSearchTest;
import aima.test.search.searches.BidirectionalSearchTest;
import aima.test.search.searches.BreadthFirstSearchTest;
import aima.test.search.searches.DepthFirstSearchTest;
import aima.test.search.searches.DepthLimitedSearchTest;
import aima.test.search.searches.GreedyBestFirstSearchTest;
import aima.test.search.searches.IterativeDeepeningSearchTest;
import aima.test.search.searches.RecursiveBestFirstSearchTest;
import aima.test.search.searches.SimulatedAnnealingSearchTest;
import aima.test.search.searches.UniformCostSearchTest;

/**
 * @author Ravi Mohan
 * 
 */

public class SearchTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(AssignmentTest.class));
		suite.addTest(new TestSuite(AStarSearchTest.class));
		suite.addTest(new TestSuite(BidirectionalSearchTest.class));
		suite.addTest(new TestSuite(BreadthFirstSearchTest.class));
		suite.addTest(new TestSuite(DomainsTest.class));
		suite.addTest(new TestSuite(DepthFirstSearchTest.class));
		suite.addTest(new TestSuite(DepthLimitedSearchTest.class));
		suite.addTest(new TestSuite(EightPuzzleBoardTest.class));
		suite.addTest(new TestSuite(EightPuzzleBoardMoveTest.class));
		suite.addTest(new TestSuite(EightPuzzleSuccessorFunctionTest.class));
		suite.addTest(new TestSuite(FIFOQueueTest.class));
		suite.addTest(new TestSuite(GreedyBestFirstSearchTest.class));
		suite.addTest(new TestSuite(IterativeDeepeningSearchTest.class));
		suite.addTest(new TestSuite(LIFOQueueTest.class));
		suite.addTest(new TestSuite(MapAgentTest.class));
		suite.addTest(new TestSuite(MapCSPTest.class));
		suite.addTest(new TestSuite(MapEnvironmentTest.class));
		suite.addTest(new TestSuite(MapStepCostFunctionTest.class));
		suite.addTest(new TestSuite(MapSuccessorFunctionTest.class));
		suite.addTest(new TestSuite(MapTest.class));
		suite.addTest(new TestSuite(MisplacedTileHeuristicFunctionTest.class));
		suite.addTest(new TestSuite(NQueensBoardTest.class));
		suite.addTest(new TestSuite(NQueensFitnessFunctionTest.class));
		suite.addTest(new TestSuite(NQueensGoalTestTest.class));
		suite.addTest(new TestSuite(NQueensSuccessorFunctionTest.class));
		suite.addTest(new TestSuite(NodeTest.class));
		suite.addTest(new TestSuite(OnlineDFSAgentTest.class));
		suite.addTest(new TestSuite(QueueTest.class));
		suite.addTest(new TestSuite(RecursiveBestFirstSearchTest.class));
		suite.addTest(new TestSuite(SimulatedAnnealingSearchTest.class));
		suite.addTest(new TestSuite(UniformCostSearchTest.class));

		return suite;
	}
}
