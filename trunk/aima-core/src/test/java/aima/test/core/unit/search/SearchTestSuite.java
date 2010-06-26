package aima.test.core.unit.search;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.search.csp.AssignmentTest;
import aima.test.core.unit.search.csp.DomainsTest;
import aima.test.core.unit.search.csp.MapCSPTest;
import aima.test.core.unit.search.framework.MultiGoalProblemTest;
import aima.test.core.unit.search.framework.NodeTest;
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
@Suite.SuiteClasses( { AssignmentTest.class, DomainsTest.class,
		MapCSPTest.class, AStarSearchTest.class,
		GreedyBestFirstSearchTest.class, RecursiveBestFirstSearchTest.class,
		SimulatedAnnealingSearchTest.class, LRTAStarAgentTest.class,
		OnlineDFSAgentTest.class, BidirectionalSearchTest.class,
		BreadthFirstSearchTest.class, DepthFirstSearchTest.class,
		DepthLimitedSearchTest.class, IterativeDeepeningSearchTest.class,
		UniformCostSearchTest.class, NodeTest.class, MultiGoalProblemTest.class })
public class SearchTestSuite {
}
