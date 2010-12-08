package aima.test.core.unit.search.online;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.MapStepCostFunction;
import aima.core.search.framework.DefaultGoalTest;
import aima.core.search.online.OnlineDFSAgent;
import aima.core.search.online.OnlineSearchProblem;

public class OnlineDFSAgentTest {

	ExtendableMap aMap;

	StringBuffer envChanges;

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
		OnlineDFSAgent agent = new OnlineDFSAgent(new OnlineSearchProblem(
				MapFunctionFactory.getActionsFunction(aMap),
				new DefaultGoalTest("A"), new MapStepCostFunction(aMap)),
				MapFunctionFactory.getPerceptToStateFunction());
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals("Action[name==NoOp]->", envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineDFSAgent agent = new OnlineDFSAgent(new OnlineSearchProblem(
				MapFunctionFactory.getActionsFunction(aMap),
				new DefaultGoalTest("G"), new MapStepCostFunction(aMap)),
				MapFunctionFactory.getPerceptToStateFunction());
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert
				.assertEquals(
						"Action[name==moveTo, location==B]->Action[name==moveTo, location==A]->Action[name==moveTo, location==C]->Action[name==moveTo, location==A]->Action[name==moveTo, location==C]->Action[name==moveTo, location==A]->Action[name==moveTo, location==B]->Action[name==moveTo, location==D]->Action[name==moveTo, location==B]->Action[name==moveTo, location==E]->Action[name==moveTo, location==B]->Action[name==moveTo, location==E]->Action[name==moveTo, location==B]->Action[name==moveTo, location==D]->Action[name==moveTo, location==F]->Action[name==moveTo, location==D]->Action[name==moveTo, location==G]->Action[name==NoOp]->",
						envChanges.toString());
	}

	@Test
	public void testNoPath() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 1.0);
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineDFSAgent agent = new OnlineDFSAgent(new OnlineSearchProblem(
				MapFunctionFactory.getActionsFunction(aMap),
				new DefaultGoalTest("X"), new MapStepCostFunction(aMap)),
				MapFunctionFactory.getPerceptToStateFunction());
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());

		me.stepUntilDone();

		Assert
				.assertEquals(
						"Action[name==moveTo, location==B]->Action[name==moveTo, location==A]->Action[name==moveTo, location==B]->Action[name==moveTo, location==A]->Action[name==NoOp]->",
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
		OnlineDFSAgent agent = new OnlineDFSAgent(new OnlineSearchProblem(
				MapFunctionFactory.getActionsFunction(aMap),
				new DefaultGoalTest("3,3"), new MapStepCostFunction(aMap)),
				MapFunctionFactory.getPerceptToStateFunction());
		me.addAgent(agent, "1,1");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert
				.assertEquals(
						"Action[name==moveTo, location==1,2]->Action[name==moveTo, location==1,1]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==1,1]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==2,2]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==3,1]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==3,1]->Action[name==moveTo, location==3,2]->Action[name==moveTo, location==3,1]->Action[name==moveTo, location==3,2]->Action[name==moveTo, location==3,3]->Action[name==NoOp]->",
						envChanges.toString());
	}

	private class TestEnvironmentView implements EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append("->");
		}

		public void agentAdded(Agent agent, EnvironmentState state) {
			// Nothing.
		}

		public void agentActed(Agent agent, Action action,
				EnvironmentState state) {
			envChanges.append(action).append("->");
		}
	}
}
