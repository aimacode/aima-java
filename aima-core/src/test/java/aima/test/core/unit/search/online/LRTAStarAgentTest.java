package aima.test.core.unit.search.online;

import aima.core.agent.*;
import aima.core.agent.impl.DynamicPercept;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctions;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.OnlineSearchProblem;
import aima.core.search.online.LRTAStarAgent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class LRTAStarAgentTest {
	private ExtendableMap aMap;
	private StringBuffer envChanges;
	private ToDoubleFunction<String> h;

	@Before
	public void setUp() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 4.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 4.0);
		aMap.addBidirectionalLink("D", "E", 4.0);
		aMap.addBidirectionalLink("E", "F", 4.0);
		h = (state) -> 1.0;

		envChanges = new StringBuffer();
	}

	@Test
	public void testAlreadyAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineSearchProblem<String, MoveToAction> problem = new GeneralProblem<>(null,
				MapFunctions.createActionsFunction(aMap), null, Predicate.isEqual("A"),
				MapFunctions.createDistanceStepCostFunction(aMap));
		LRTAStarAgent<DynamicPercept, String, MoveToAction> agent = new LRTAStarAgent<>
				(problem, MapFunctions.createPerceptToStateFunction(), h);

		me.addAgent(agent, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals("", envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineSearchProblem<String, MoveToAction> problem = new GeneralProblem<>(null,
				MapFunctions.createActionsFunction(aMap), null, Predicate.isEqual("F"),
				MapFunctions.createDistanceStepCostFunction(aMap));
		LRTAStarAgent<DynamicPercept, String, MoveToAction> agent = new LRTAStarAgent<>
				(problem, MapFunctions.createPerceptToStateFunction(), h);

		me.addAgent(agent, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"Action[name=moveTo, location=B]:Action[name=moveTo, location=A]:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:Action[name=moveTo, location=F]:",
				envChanges.toString());
	}

	@Test
	public void testNoPath() {
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineSearchProblem<String, MoveToAction> problem = new GeneralProblem<>(null,
				MapFunctions.createActionsFunction(aMap), null, Predicate.isEqual("G"),
				MapFunctions.createDistanceStepCostFunction(aMap));
		LRTAStarAgent<DynamicPercept, String, MoveToAction> agent = new LRTAStarAgent<>
				(problem, MapFunctions.createPerceptToStateFunction(), h);

		me.addAgent(agent, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		// Note: Will search forever if no path is possible,
		// Therefore restrict the number of steps to something
		// reasonablbe, against which to test.
		me.step(14);

		Assert.assertEquals(
				"Action[name=moveTo, location=B]:Action[name=moveTo, location=A]:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:Action[name=moveTo, location=F]:Action[name=moveTo, location=E]:",
				envChanges.toString());
	}

	private class TestEnvironmentView implements EnvironmentListener<Object, Object> {
		public void notify(String msg) {
			envChanges.append(msg).append(":");
		}

		public void agentAdded(Agent agent, Environment source) {
			// Nothing
		}

		public void agentActed(Agent agent, Object percept, Object action, Environment source) {
			envChanges.append(action).append(":");
		}
	}
}
