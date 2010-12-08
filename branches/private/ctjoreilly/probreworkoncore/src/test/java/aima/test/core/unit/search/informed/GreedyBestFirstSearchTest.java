package aima.test.core.unit.search.informed;

import org.junit.Assert;
import org.junit.Test;

import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.environment.eightpuzzle.EightPuzzleGoalTest;
import aima.core.environment.eightpuzzle.ManhattanHeuristicFunction;
import aima.core.environment.map.Map;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.MapStepCostFunction;
import aima.core.environment.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.map.StraightLineDistanceHeuristicFunction;
import aima.core.search.framework.DefaultGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
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

			Problem problem = new Problem(board, EightPuzzleFunctionFactory
					.getActionsFunction(), EightPuzzleFunctionFactory
					.getResultFunction(), new EightPuzzleGoalTest());
			Search search = new GreedyBestFirstSearch(new GraphSearch(),
					new ManhattanHeuristicFunction());
			SearchAgent agent = new SearchAgent(problem, search);
			Assert.assertEquals(49, agent.getActions().size());
			Assert.assertEquals("197", agent.getInstrumentation().getProperty(
					"nodesExpanded"));
			Assert.assertEquals("140", agent.getInstrumentation().getProperty(
					"queueSize"));
			Assert.assertEquals("141", agent.getInstrumentation().getProperty(
					"maxQueueSize"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception thrown.");
		}
	}

	@Test
	public void testAIMA3eFigure3_23() throws Exception {
		Map romaniaMap = new SimplifiedRoadMapOfPartOfRomania();
		Problem problem = new Problem(SimplifiedRoadMapOfPartOfRomania.ARAD,
				MapFunctionFactory.getActionsFunction(romaniaMap),
				MapFunctionFactory.getResultFunction(), new DefaultGoalTest(
						SimplifiedRoadMapOfPartOfRomania.BUCHAREST),
				new MapStepCostFunction(romaniaMap));

		Search search = new GreedyBestFirstSearch(new TreeSearch(),
				new StraightLineDistanceHeuristicFunction(
						SimplifiedRoadMapOfPartOfRomania.BUCHAREST, romaniaMap));
		SearchAgent agent = new SearchAgent(problem, search);
		Assert
				.assertEquals(
						"[Action[name==moveTo, location==Sibiu], Action[name==moveTo, location==Fagaras], Action[name==moveTo, location==Bucharest]]",
						agent.getActions().toString());
		Assert.assertEquals(3, agent.getActions().size());
		Assert.assertEquals("3", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
		Assert.assertEquals("6", agent.getInstrumentation().getProperty(
				"queueSize"));
		Assert.assertEquals("7", agent.getInstrumentation().getProperty(
				"maxQueueSize"));
	}

	@Test
	public void testAIMA3eFigure3_23_using_GraphSearch() throws Exception {
		Map romaniaMap = new SimplifiedRoadMapOfPartOfRomania();
		Problem problem = new Problem(SimplifiedRoadMapOfPartOfRomania.ARAD,
				MapFunctionFactory.getActionsFunction(romaniaMap),
				MapFunctionFactory.getResultFunction(), new DefaultGoalTest(
						SimplifiedRoadMapOfPartOfRomania.BUCHAREST),
				new MapStepCostFunction(romaniaMap));

		Search search = new GreedyBestFirstSearch(new GraphSearch(),
				new StraightLineDistanceHeuristicFunction(
						SimplifiedRoadMapOfPartOfRomania.BUCHAREST, romaniaMap));
		SearchAgent agent = new SearchAgent(problem, search);
		Assert
				.assertEquals(
						"[Action[name==moveTo, location==Sibiu], Action[name==moveTo, location==Fagaras], Action[name==moveTo, location==Bucharest]]",
						agent.getActions().toString());
		Assert.assertEquals(3, agent.getActions().size());
		Assert.assertEquals("3", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
		Assert.assertEquals("4", agent.getInstrumentation().getProperty(
				"queueSize"));
		Assert.assertEquals("5", agent.getInstrumentation().getProperty(
				"maxQueueSize"));
	}
}
