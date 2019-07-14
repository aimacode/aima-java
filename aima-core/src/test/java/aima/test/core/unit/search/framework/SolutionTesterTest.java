package aima.test.core.unit.search.framework;

import aima.core.environment.map.Map;
import aima.core.environment.map.MapFunctions;
import aima.core.environment.map.MoveToAction;
import aima.core.environment.map.SimplifiedRoadMapOfRomania;
import aima.core.search.framework.Node;
import aima.core.search.agent.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.UniformCostSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;


/**
 * @author RuedigerLunde
 */
public class SolutionTesterTest {

	@Test
	public void testMultiGoalProblem() throws Exception {
		Map romaniaMap = new SimplifiedRoadMapOfRomania();

		Problem<String, MoveToAction> problem = new GeneralProblem<String, MoveToAction>
				(SimplifiedRoadMapOfRomania.ARAD,
				MapFunctions.createActionsFunction(romaniaMap), MapFunctions.createResultFunction(),
				Predicate.<String>isEqual(SimplifiedRoadMapOfRomania.BUCHAREST).or
						(Predicate.isEqual(SimplifiedRoadMapOfRomania.HIRSOVA)),
				MapFunctions.createDistanceStepCostFunction(romaniaMap)) {
			@Override
			public boolean testSolution(Node<String, MoveToAction> node) {
				return testGoal(node.getState()) && node.getPathCost() > 550;
				// accept paths to goal only if their costs are above 550
			}
		};

		SearchForActions<String, MoveToAction> search = new UniformCostSearch<>(new GraphSearch<>());

		SearchAgent<Object, String, MoveToAction> agent = new SearchAgent<>(problem, search);
		Assert.assertEquals(
				"[Action[name=moveTo, location=Sibiu], Action[name=moveTo, location=RimnicuVilcea], Action[name=moveTo, location=Pitesti], Action[name=moveTo, location=Bucharest], Action[name=moveTo, location=Urziceni], Action[name=moveTo, location=Hirsova]]",
				agent.getActions().toString());
		Assert.assertEquals(6, agent.getActions().size());
		Assert.assertEquals("15", agent.getInstrumentation().getProperty("nodesExpanded"));
		Assert.assertEquals("1", agent.getInstrumentation().getProperty("queueSize"));
		Assert.assertEquals("5", agent.getInstrumentation().getProperty("maxQueueSize"));
	}
}
