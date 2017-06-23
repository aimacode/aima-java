package aima.test.core.unit.search.online;

import aima.core.agent.*;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctions;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.OnlineSearchProblem;
import aima.core.search.online.OnlineDFSAgent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OnlineDFSAgentTest {

	private ExtendableMap aMap;

	private StringBuffer envChanges;

	@Before
	public void setUp() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "D", 4.0);
		aMap.addBidirectionalLink("B", "E", 7.0);
		aMap.addBidirectionalLink("D", "F", 4.0);
		aMap.addBidirectionalLink("D", "G", 8.0);

		envChanges = new StringBuffer();
	}

	@Test
	public void testAlreadyAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineSearchProblem<String, MoveToAction> problem = new GeneralProblem<>(null,
				MapFunctions.createActionsFunction(aMap), null, GoalTest.isEqual("A"),
				MapFunctions.createDistanceStepCostFunction(aMap));
		OnlineDFSAgent<String, MoveToAction> agent = new OnlineDFSAgent<>
				(problem, MapFunctions.createPerceptToStateFunction());

		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals("Action[name=NoOp]->", envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineSearchProblem<String, MoveToAction> problem = new GeneralProblem<>(null,
				MapFunctions.createActionsFunction(aMap), null, GoalTest.isEqual("G"),
				MapFunctions.createDistanceStepCostFunction(aMap));
		OnlineDFSAgent<String, MoveToAction> agent = new OnlineDFSAgent<>
				(problem, MapFunctions.createPerceptToStateFunction());

		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"Action[name=moveTo, location=B]->Action[name=moveTo, location=A]->Action[name=moveTo, location=C]->Action[name=moveTo, location=A]->Action[name=moveTo, location=C]->Action[name=moveTo, location=A]->Action[name=moveTo, location=B]->Action[name=moveTo, location=D]->Action[name=moveTo, location=B]->Action[name=moveTo, location=E]->Action[name=moveTo, location=B]->Action[name=moveTo, location=E]->Action[name=moveTo, location=B]->Action[name=moveTo, location=D]->Action[name=moveTo, location=F]->Action[name=moveTo, location=D]->Action[name=moveTo, location=G]->Action[name=NoOp]->",
				envChanges.toString());
	}

	@Test
	public void testNoPath() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 1.0);
		MapEnvironment me = new MapEnvironment(aMap);

		OnlineSearchProblem<String, MoveToAction> problem = new GeneralProblem<>(null,
				MapFunctions.createActionsFunction(aMap), null, GoalTest.isEqual("X"),
				MapFunctions.createDistanceStepCostFunction(aMap));
		OnlineDFSAgent<String, MoveToAction> agent = new OnlineDFSAgent<>
				(problem, MapFunctions.createPerceptToStateFunction());

		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());

		me.stepUntilDone();

		Assert.assertEquals(
				"Action[name=moveTo, location=B]->Action[name=moveTo, location=A]->Action[name=moveTo, location=B]->Action[name=moveTo, location=A]->Action[name=NoOp]->",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eFig4_19() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("1,1", "1,2", 1.0);
		aMap.addBidirectionalLink("1,1", "2,1", 1.0);
		aMap.addBidirectionalLink("2,1", "3,1", 1.0);
		aMap.addBidirectionalLink("2,1", "2,2", 1.0);
		aMap.addBidirectionalLink("3,1", "3,2", 1.0);
		aMap.addBidirectionalLink("2,2", "2,3", 1.0);
		aMap.addBidirectionalLink("3,2", "3,3", 1.0);
		aMap.addBidirectionalLink("2,3", "1,3", 1.0);

		MapEnvironment me = new MapEnvironment(aMap);
		OnlineSearchProblem<String, MoveToAction> problem = new GeneralProblem<>(null,
				MapFunctions.createActionsFunction(aMap), null, GoalTest.isEqual("3,3"),
				MapFunctions.createDistanceStepCostFunction(aMap));
		OnlineDFSAgent<String, MoveToAction> agent = new OnlineDFSAgent<>
				(problem, MapFunctions.createPerceptToStateFunction());

		me.addAgent(agent, "1,1");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"Action[name=moveTo, location=1,2]->Action[name=moveTo, location=1,1]->Action[name=moveTo, location=2,1]->Action[name=moveTo, location=1,1]->Action[name=moveTo, location=2,1]->Action[name=moveTo, location=2,2]->Action[name=moveTo, location=2,1]->Action[name=moveTo, location=3,1]->Action[name=moveTo, location=2,1]->Action[name=moveTo, location=3,1]->Action[name=moveTo, location=3,2]->Action[name=moveTo, location=3,1]->Action[name=moveTo, location=3,2]->Action[name=moveTo, location=3,3]->Action[name=NoOp]->",
				envChanges.toString());
	}

	private class TestEnvironmentView implements EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append("->");
		}

		public void agentAdded(Agent agent, Environment source) {
			// Nothing.
		}

		public void agentActed(Agent agent, Percept percept, Action action, Environment source) {
			envChanges.append(action).append("->");
		}
	}
}
