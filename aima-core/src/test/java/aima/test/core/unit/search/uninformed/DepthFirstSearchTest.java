package aima.test.core.unit.search.uninformed;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.agent.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class DepthFirstSearchTest {

	@Test
	public void testDepthFirstSuccesfulSearch() throws Exception {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(new NQueensBoard(8),
				NQueensFunctions::getIFActions, NQueensFunctions::getResult, NQueensFunctions::testGoal);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthFirstSearch<>(new GraphSearch<>());
		Optional<List<QueenAction>> actions = search.findActions(problem);
		Assert.assertTrue(actions.isPresent());
		assertCorrectPlacement(actions.get());
		Assert.assertEquals("113", search.getMetrics().get("nodesExpanded"));
	}

	@Test
	public void testDepthFirstUnSuccessfulSearch() throws Exception {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(new NQueensBoard(3),
				NQueensFunctions::getIFActions, NQueensFunctions::getResult, NQueensFunctions::testGoal);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthFirstSearch<>(new GraphSearch<>());
		SearchAgent<Object, NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);
		List<QueenAction> actions = agent.getActions();
		Assert.assertEquals(0, actions.size());
		Assert.assertEquals("6", agent.getInstrumentation().getProperty("nodesExpanded"));
	}

	//
	// PRIVATE METHODS
	//
	private void assertCorrectPlacement(List<QueenAction> actions) {
		Assert.assertEquals(8, actions.size());
		Assert.assertEquals("Action[name=placeQueenAt, location=(0, 7)]", actions.get(0).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(1, 3)]", actions.get(1).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(2, 0)]", actions.get(2).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(3, 2)]", actions.get(3).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(4, 5)]", actions.get(4).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(5, 1)]", actions.get(5).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(6, 6)]", actions.get(6).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(7, 4)]", actions.get(7).toString());
	}
}
