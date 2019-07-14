package aima.test.core.unit.search.uninformed;

import aima.core.agent.Action;
import aima.core.environment.map.*;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.agent.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.QueueSearch;
import aima.core.search.uninformed.UniformCostSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class UniformCostSearchTest {

	@Test
	public void testUniformCostSuccesfulSearch() throws Exception {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(new NQueensBoard(8),
				NQueensFunctions::getIFActions, NQueensFunctions::getResult, NQueensFunctions::testGoal);
		SearchForActions<NQueensBoard, QueenAction> search = new UniformCostSearch<>();
		SearchAgent<Object, NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);

		List<QueenAction> actions = agent.getActions();

		Assert.assertEquals(8, actions.size());

		Assert.assertEquals("1965", agent.getInstrumentation().getProperty("nodesExpanded"));

		Assert.assertEquals("8.0", agent.getInstrumentation().getProperty("pathCost"));
	}

	@Test
	public void testUniformCostUnSuccesfulSearch() throws Exception {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(new NQueensBoard(3),
				NQueensFunctions::getIFActions, NQueensFunctions::getResult, NQueensFunctions::testGoal);
		SearchForActions<NQueensBoard, QueenAction> search = new UniformCostSearch<>();
		SearchAgent<Object, NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);

		List<QueenAction> actions = agent.getActions();

		Assert.assertEquals(0, actions.size());

		Assert.assertEquals("6", agent.getInstrumentation().getProperty("nodesExpanded"));

		// Will be 0 as did not reach goal state.
		Assert.assertEquals("0", agent.getInstrumentation().getProperty("pathCost"));
	}

	@Test
	public void testAIMA3eFigure3_15() throws Exception {
		Map romaniaMap = new SimplifiedRoadMapOfRomania();
		Problem<String, MoveToAction> problem = new GeneralProblem<>(SimplifiedRoadMapOfRomania.SIBIU,
				MapFunctions.createActionsFunction(romaniaMap), MapFunctions.createResultFunction(),
				Predicate.isEqual(SimplifiedRoadMapOfRomania.BUCHAREST),
				MapFunctions.createDistanceStepCostFunction(romaniaMap));

		SearchForActions<String, MoveToAction> search = new UniformCostSearch<>();
		SearchAgent<Object, String, MoveToAction> agent = new SearchAgent<>(problem, search);

		List<MoveToAction> actions = agent.getActions();

		Assert.assertEquals(
				"[Action[name=moveTo, location=RimnicuVilcea], Action[name=moveTo, location=Pitesti], Action[name=moveTo, location=Bucharest]]",
				actions.toString());
		Assert.assertEquals("278.0", search.getMetrics().get(QueueSearch.METRIC_PATH_COST));
	}

	@Test
	public void testCheckFrontierPathCost() throws Exception {
		ExtendableMap map = new ExtendableMap();
		map.addBidirectionalLink("start", "b", 2.5);
		map.addBidirectionalLink("start", "c", 1.0);
		map.addBidirectionalLink("b", "d", 2.0);
		map.addBidirectionalLink("c", "d", 4.0);
		map.addBidirectionalLink("c", "e", 1.0);
		map.addBidirectionalLink("d", "goal", 1.0);
		map.addBidirectionalLink("e", "goal", 5.0);
		Problem<String, MoveToAction> problem = new GeneralProblem<>("start", MapFunctions.createActionsFunction(map),
				MapFunctions.createResultFunction(), Predicate.isEqual("goal"),
				MapFunctions.createDistanceStepCostFunction(map));

		SearchForActions<String, MoveToAction> search = new UniformCostSearch<>();
		SearchAgent<Action, String, MoveToAction> agent = new SearchAgent<>(problem, search);

		List<MoveToAction> actions = agent.getActions();

		Assert.assertEquals(
				"[Action[name=moveTo, location=b], Action[name=moveTo, location=d], Action[name=moveTo, location=goal]]",
				actions.toString());
		Assert.assertEquals("5.5", search.getMetrics().get(QueueSearch.METRIC_PATH_COST));
	}
}
