package aima.test.core.unit.search.uninformed;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.map.Map;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.MapStepCostFunction;
import aima.core.environment.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.search.framework.DefaultGoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.uninformed.UniformCostSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class UniformCostSearchTest {

	@Test
	public void testUniformCostSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(8),
				NQueensFunctionFactory.getIActionsFunction(),
				NQueensFunctionFactory.getResultFunction(),
				new NQueensGoalTest());
		Search search = new UniformCostSearch();
		SearchAgent agent = new SearchAgent(problem, search);

		List<Action> actions = agent.getActions();

		Assert.assertEquals(8, actions.size());

		Assert.assertEquals("1965", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

		Assert.assertEquals("8.0", agent.getInstrumentation().getProperty(
				"pathCost"));
	}

	@Test
	public void testUniformCostUnSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(3),
				NQueensFunctionFactory.getIActionsFunction(),
				NQueensFunctionFactory.getResultFunction(),
				new NQueensGoalTest());
		Search search = new UniformCostSearch();
		SearchAgent agent = new SearchAgent(problem, search);

		List<Action> actions = agent.getActions();

		Assert.assertEquals(0, actions.size());

		Assert.assertEquals("6", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

		// Will be 0 as did not reach goal state.
		Assert.assertEquals("0", agent.getInstrumentation().getProperty(
				"pathCost"));
	}

	@Test
	public void testAIMA3eFigure3_15() throws Exception {
		Map romaniaMap = new SimplifiedRoadMapOfPartOfRomania();
		Problem problem = new Problem(SimplifiedRoadMapOfPartOfRomania.SIBIU,
				MapFunctionFactory.getActionsFunction(romaniaMap),
				MapFunctionFactory.getResultFunction(), new DefaultGoalTest(
						SimplifiedRoadMapOfPartOfRomania.BUCHAREST),
				new MapStepCostFunction(romaniaMap));

		Search search = new UniformCostSearch();
		SearchAgent agent = new SearchAgent(problem, search);

		List<Action> actions = agent.getActions();

		Assert
				.assertEquals(
						"[Action[name==moveTo, location==RimnicuVilcea], Action[name==moveTo, location==Pitesti], Action[name==moveTo, location==Bucharest]]",
						actions.toString());
		Assert.assertEquals("278.0", search.getMetrics().get(
				QueueSearch.METRIC_PATH_COST));
	}
}
