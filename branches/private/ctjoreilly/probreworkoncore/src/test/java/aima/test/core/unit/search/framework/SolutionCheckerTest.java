package aima.test.core.unit.search.framework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.map.Map;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.MapStepCostFunction;
import aima.core.environment.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.SolutionChecker;
import aima.core.search.uninformed.BreadthFirstSearch;

public class SolutionCheckerTest {

	@Test
	public void testMultiGoalProblem() throws Exception {
		Map romaniaMap = new SimplifiedRoadMapOfPartOfRomania();
		Problem problem = new Problem(SimplifiedRoadMapOfPartOfRomania.ARAD,
				MapFunctionFactory.getActionsFunction(romaniaMap),
				MapFunctionFactory.getResultFunction(), new DualMapGoalTest(
						SimplifiedRoadMapOfPartOfRomania.BUCHAREST,
						SimplifiedRoadMapOfPartOfRomania.HIRSOVA),
				new MapStepCostFunction(romaniaMap));

		Search search = new BreadthFirstSearch(new GraphSearch());

		SearchAgent agent = new SearchAgent(problem, search);
		Assert
				.assertEquals(
						"[Action[name==moveTo, location==Sibiu], Action[name==moveTo, location==Fagaras], Action[name==moveTo, location==Bucharest], Action[name==moveTo, location==Urziceni], Action[name==moveTo, location==Hirsova]]",
						agent.getActions().toString());
		Assert.assertEquals(5, agent.getActions().size());
		Assert.assertEquals("14", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
		Assert.assertEquals("1", agent.getInstrumentation().getProperty(
				"queueSize"));
		Assert.assertEquals("5", agent.getInstrumentation().getProperty(
				"maxQueueSize"));
	}

	class DualMapGoalTest implements SolutionChecker {
		public String goalState1 = null;
		public String goalState2 = null;

		private Set<String> goals = new HashSet<String>();

		public DualMapGoalTest(String goalState1, String goalState2) {
			this.goalState1 = goalState1;
			this.goalState2 = goalState2;
			goals.add(goalState1);
			goals.add(goalState2);
		}

		public boolean isGoalState(Object state) {
			return goalState1.equals(state) || goalState2.equals(state);
		}

		public boolean isAcceptableSolution(List<Action> actions, Object goal) {
			goals.remove(goal);
			return goals.isEmpty();
		}
	}
}
