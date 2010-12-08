package aima.test.core.unit.search.uninformed;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.uninformed.DepthFirstSearch;

public class DepthFirstSearchTest {

	@Test
	public void testDepthFirstSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(8),
				NQueensFunctionFactory.getIActionsFunction(),
				NQueensFunctionFactory.getResultFunction(),
				new NQueensGoalTest());
		Search search = new DepthFirstSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);
		List<Action> actions = agent.getActions();
		assertCorrectPlacement(actions);
		Assert.assertEquals("113", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
	}

	@Test
	public void testDepthFirstUnSuccessfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(3),
				NQueensFunctionFactory.getIActionsFunction(),
				NQueensFunctionFactory.getResultFunction(),
				new NQueensGoalTest());
		Search search = new DepthFirstSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);
		List<Action> actions = agent.getActions();
		Assert.assertEquals(0, actions.size());
		Assert.assertEquals("6", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
	}

	//
	// PRIVATE METHODS
	//
	private void assertCorrectPlacement(List<Action> actions) {
		Assert.assertEquals(8, actions.size());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 0 , 7 ) ]", actions
						.get(0).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 1 , 3 ) ]", actions
						.get(1).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 2 , 0 ) ]", actions
						.get(2).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 3 , 2 ) ]", actions
						.get(3).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 4 , 5 ) ]", actions
						.get(4).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 5 , 1 ) ]", actions
						.get(5).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 6 , 6 ) ]", actions
						.get(6).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 7 , 4 ) ]", actions
						.get(7).toString());
	}
}
