package aima.test.core.unit.search;

import aima.test.core.unit.search.csp.AssignmentTest;
import aima.test.core.unit.search.csp.CSPTest;
import aima.test.core.unit.search.csp.MapCSPTest;
import aima.test.core.unit.search.csp.TreeCSPSolverTest;
import aima.test.core.unit.search.framework.MetricsTest;
import aima.test.core.unit.search.framework.NodeTest;
import aima.test.core.unit.search.framework.SolutionCheckerTest;
import aima.test.core.unit.search.informed.AStarSearchTest;
import aima.test.core.unit.search.informed.GreedyBestFirstSearchTest;
import aima.test.core.unit.search.informed.RecursiveBestFirstSearchTest;
import aima.test.core.unit.search.local.SimulatedAnnealingSearchTest;
import aima.test.core.unit.search.nondeterministic.AndOrSearchTest;
import aima.test.core.unit.search.online.LRTAStarAgentTest;
import aima.test.core.unit.search.online.OnlineDFSAgentTest;
import aima.test.core.unit.search.uninformed.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AssignmentTest.class, CSPTest.class, MapCSPTest.class, MetricsTest.class, TreeCSPSolverTest.class,
		AStarSearchTest.class, GreedyBestFirstSearchTest.class, RecursiveBestFirstSearchTest.class,
		SimulatedAnnealingSearchTest.class, AndOrSearchTest.class, LRTAStarAgentTest.class, OnlineDFSAgentTest.class,
		BidirectionalSearchTest.class, BreadthFirstSearchTest.class, DepthFirstSearchTest.class,
		DepthLimitedSearchTest.class, IterativeDeepeningSearchTest.class, UniformCostSearchTest.class, NodeTest.class,
		SolutionCheckerTest.class })
public class SearchTestSuite {
}
