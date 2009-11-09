package aima.test.core.unit.search;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.search.csp.AssignmentTest;
import aima.test.core.unit.search.csp.DomainsTest;
import aima.test.core.unit.search.csp.MapCSPTest;
import aima.test.core.unit.search.eightpuzzle.EightPuzzleBoardMoveTest;
import aima.test.core.unit.search.eightpuzzle.EightPuzzleBoardTest;
import aima.test.core.unit.search.eightpuzzle.EightPuzzleFunctionFactoryTest;
import aima.test.core.unit.search.games.TicTacToeTest;
import aima.test.core.unit.search.informed.AStarSearchTest;
import aima.test.core.unit.search.informed.GreedyBestFirstSearchTest;
import aima.test.core.unit.search.informed.RecursiveBestFirstSearchTest;
import aima.test.core.unit.search.informed.SimulatedAnnealingSearchTest;
import aima.test.core.unit.search.map.MapAgentTest;
import aima.test.core.unit.search.map.MapEnvironmentTest;
import aima.test.core.unit.search.map.MapFunctionFactoryTest;
import aima.test.core.unit.search.map.MapStepCostFunctionTest;
import aima.test.core.unit.search.map.MapTest;
import aima.test.core.unit.search.nqueens.NQueensBoardTest;
import aima.test.core.unit.search.nqueens.NQueensFitnessFunctionTest;
import aima.test.core.unit.search.nqueens.NQueensFunctionFactoryTest;
import aima.test.core.unit.search.nqueens.NQueensGoalTestTest;
import aima.test.core.unit.search.online.LRTAStarAgentTest;
import aima.test.core.unit.search.online.OnlineDFSAgentTest;
import aima.test.core.unit.search.uninformed.BidirectionalSearchTest;
import aima.test.core.unit.search.uninformed.BreadthFirstSearchTest;
import aima.test.core.unit.search.uninformed.DepthFirstSearchTest;
import aima.test.core.unit.search.uninformed.DepthLimitedSearchTest;
import aima.test.core.unit.search.uninformed.IterativeDeepeningSearchTest;
import aima.test.core.unit.search.uninformed.UniformCostSearchTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { AssignmentTest.class, DomainsTest.class,
		MapCSPTest.class, EightPuzzleBoardMoveTest.class,
		EightPuzzleBoardTest.class, EightPuzzleFunctionFactoryTest.class,
		TicTacToeTest.class, AStarSearchTest.class,
		GreedyBestFirstSearchTest.class, RecursiveBestFirstSearchTest.class,
		SimulatedAnnealingSearchTest.class, MapAgentTest.class,
		MapEnvironmentTest.class, MapStepCostFunctionTest.class,
		MapFunctionFactoryTest.class, MapTest.class, NQueensBoardTest.class,
		NQueensFitnessFunctionTest.class, NQueensGoalTestTest.class,
		NQueensFunctionFactoryTest.class, LRTAStarAgentTest.class,
		OnlineDFSAgentTest.class, BidirectionalSearchTest.class,
		BreadthFirstSearchTest.class, DepthFirstSearchTest.class,
		DepthLimitedSearchTest.class, IterativeDeepeningSearchTest.class,
		UniformCostSearchTest.class, MisplacedTileHeuristicFunctionTest.class,
		NodeTest.class })
public class SearchTestSuite {
}
