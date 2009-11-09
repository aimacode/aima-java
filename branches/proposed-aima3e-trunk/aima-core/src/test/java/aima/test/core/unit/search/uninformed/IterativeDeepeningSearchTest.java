package aima.test.core.unit.search.uninformed;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.nqueens.NQueensBoard;
import aima.core.search.nqueens.NQueensFunctionFactory;
import aima.core.search.nqueens.NQueensGoalTest;
import aima.core.search.uninformed.IterativeDeepeningSearch;

public class IterativeDeepeningSearchTest {

	@Test
	public void testIterativeDeepeningSearch() {
		try {
			Problem problem = new Problem(new NQueensBoard(8),
					NQueensFunctionFactory.getActionsFunction(),
					NQueensFunctionFactory.getResultFunction(),
					new NQueensGoalTest());
			Search search = new IterativeDeepeningSearch();
			SearchAgent agent = new SearchAgent(problem, search);
			List<Action> actions = agent.getActions();
			assertCorrectPlacement(actions);
			Assert.assertEquals("3656", agent.getInstrumentation().getProperty(
					"nodesExpanded"));

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception should not occur");
		}
	}

	private void assertCorrectPlacement(List<Action> actions) {
		Assert.assertEquals(8, actions.size());
		Assert.assertEquals("Action[name==placeQueenAt, x==0, y==0]", actions
				.get(0).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==1, y==4]", actions
				.get(1).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==2, y==7]", actions
				.get(2).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==3, y==5]", actions
				.get(3).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==4, y==2]", actions
				.get(4).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==5, y==6]", actions
				.get(5).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==6, y==1]", actions
				.get(6).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==7, y==3]", actions
				.get(7).toString());
	}
}