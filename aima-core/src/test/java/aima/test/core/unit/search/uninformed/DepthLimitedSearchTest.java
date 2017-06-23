package aima.test.core.unit.search.uninformed;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.uninformed.DepthLimitedSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

/**
 * Tests depth-limited search.
 * @author Ruediger Lunde
 */
public class DepthLimitedSearchTest {

	@Test
	public void testSuccessfulDepthLimitedSearch() throws Exception {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(new NQueensBoard(8),
				NQueensFunctions::getIFActions, NQueensFunctions::getResult, NQueensFunctions::testGoal);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthLimitedSearch<>(8);
		Optional<List<QueenAction>> actions = search.findActions(problem);
		Assert.assertTrue(actions.isPresent());
		assertCorrectPlacement(actions.get());
		Assert.assertEquals("113", search.getMetrics().get("nodesExpanded"));
	}

	@Test
	public void testCutOff() throws Exception {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(new NQueensBoard(8),
				NQueensFunctions::getIFActions, NQueensFunctions::getResult, NQueensFunctions::testGoal);
		DepthLimitedSearch<NQueensBoard, QueenAction> search = new DepthLimitedSearch<>(1);
		Optional<Node<NQueensBoard, QueenAction>> result = search.findNode(problem);
		Assert.assertEquals(true, search.isCutoffResult(result));
	}

	@Test
	public void testFailure() throws Exception {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(new NQueensBoard(3),
				NQueensFunctions::getIFActions, NQueensFunctions::getResult, NQueensFunctions::testGoal);
		DepthLimitedSearch<NQueensBoard, QueenAction> search = new DepthLimitedSearch<>(5);
		Optional<List<QueenAction>> actions = search.findActions(problem);
		Assert.assertFalse(actions.isPresent()); // failure
	}

	//
	// PRIVATE METHODS
	//
	private void assertCorrectPlacement(List<QueenAction> actions) {
		Assert.assertEquals(8, actions.size());
		Assert.assertEquals("Action[name=placeQueenAt, location=(0, 0)]", actions.get(0).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(1, 4)]", actions.get(1).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(2, 7)]", actions.get(2).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(3, 5)]", actions.get(3).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(4, 2)]", actions.get(4).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(5, 6)]", actions.get(5).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(6, 1)]", actions.get(6).toString());
		Assert.assertEquals("Action[name=placeQueenAt, location=(7, 3)]", actions.get(7).toString());
	}
}