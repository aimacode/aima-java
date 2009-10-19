package aima.test.core.unit.search.uninformed;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.nqueens.NQueensBoard;
import aima.core.search.nqueens.NQueensGoalTest;
import aima.core.search.nqueens.NQueensSuccessorFunction;
import aima.core.search.uninformed.BreadthFirstSearch;

public class BreadthFirstSearchTest {

	@Test
	public void testBreadthFirstSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(8),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new BreadthFirstSearch(new TreeSearch());
		SearchAgent agent = new SearchAgent(problem, search);
		List<Action> actions = agent.getActions();
		assertCorrectPlacement(actions);
		Assert.assertEquals("1965", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

		problem = new Problem(new NQueensBoard(3),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		agent = new SearchAgent(problem, search);
		actions = agent.getActions();
		Assert.assertEquals(0, actions.size());
		Assert.assertEquals("6", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
	}

	@Test
	public void testBreadthFirstUnSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(3),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new BreadthFirstSearch(new TreeSearch());
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
		Assert.assertEquals("Action[name==placeQueenAt, row==0, col==0]", actions.get(0).toString());
		Assert.assertEquals("Action[name==placeQueenAt, row==1, col==4]", actions.get(1).toString());
		Assert.assertEquals("Action[name==placeQueenAt, row==2, col==7]", actions.get(2).toString());
		Assert.assertEquals("Action[name==placeQueenAt, row==3, col==5]", actions.get(3).toString());
		Assert.assertEquals("Action[name==placeQueenAt, row==4, col==2]", actions.get(4).toString());
		Assert.assertEquals("Action[name==placeQueenAt, row==5, col==6]", actions.get(5).toString());
		Assert.assertEquals("Action[name==placeQueenAt, row==6, col==1]", actions.get(6).toString());
		Assert.assertEquals("Action[name==placeQueenAt, row==7, col==3]", actions.get(7).toString());
	}
}
