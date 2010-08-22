package aima.test.core.unit.search;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.search.csp.AssignmentTest;
import aima.test.core.unit.search.csp.CSPTest;
import aima.test.core.unit.search.csp.MapCSPTest;
import aima.test.core.unit.search.framework.NodeTest;
import aima.test.core.unit.search.framework.SolutionCheckerTest;
import aima.test.core.unit.search.informed.AStarSearchTest;
import aima.test.core.unit.search.informed.GreedyBestFirstSearchTest;
import aima.test.core.unit.search.informed.RecursiveBestFirstSearchTest;
import aima.test.core.unit.search.local.SimulatedAnnealingSearchTest;
import aima.test.core.unit.search.online.LRTAStarAgentTest;
import aima.test.core.unit.search.online.OnlineDFSAgentTest;
import aima.test.core.unit.search.uninformed.BidirectionalSearchTest;
import aima.test.core.unit.search.uninformed.BreadthFirstSearchTest;
import aima.test.core.unit.search.uninformed.DepthFirstSearchTest;
import aima.test.core.unit.search.uninformed.DepthLimitedSearchTest;
import aima.test.core.unit.search.uninformed.IterativeDeepeningSearchTest;
import aima.test.core.unit.search.uninformed.UniformCostSearchTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { AssignmentTest.class, CSPTest.class, MapCSPTest.class,
		AStarSearchTest.class, GreedyBestFirstSearchTest.class,
		RecursiveBestFirstSearchTest.class, SimulatedAnnealingSearchTest.class,
		LRTAStarAgentTest.class, OnlineDFSAgentTest.class,
		BidirectionalSearchTest.class, BreadthFirstSearchTest.class,
		DepthFirstSearchTest.class, DepthLimitedSearchTest.class,
		IterativeDeepeningSearchTest.class, UniformCostSearchTest.class,
		NodeTest.class, SolutionCheckerTest.class })
public class SearchTestSuite {
}
