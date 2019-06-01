package aima.test.core.unit.environment.map;

import aima.core.agent.*;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MoveToAction;
import aima.core.environment.map.SimpleMapAgent;
import aima.core.search.framework.qsearch.GraphSearchReducedFrontier;
import aima.core.search.uninformed.UniformCostSearch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class MapAgentTest {

	private ExtendableMap aMap;

	private StringBuffer envChanges;

	@Before
	public void setUp() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		envChanges = new StringBuffer();
	}

	@Test
	public void testAlreadyAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), new UniformCostSearch<>(), "A").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(A):Search{maxQueueSize=1, nodesExpanded=0, pathCost=0.0, queueSize=0}:",
				envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), new UniformCostSearch<>(),"D").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(D):Search{maxQueueSize=3, nodesExpanded=3, pathCost=13.0, queueSize=1}:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:",
				envChanges.toString());
	}

	@Test
	public void testNormalSearchGraphSearchMinFrontier() {
		MapEnvironment me = new MapEnvironment(aMap);
		UniformCostSearch<String, MoveToAction> ucSearch = new UniformCostSearch<>(new GraphSearchReducedFrontier<>());

		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), ucSearch, "D").setNotifier(me);

		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(D):Search{maxQueueSize=2, nodesExpanded=3, pathCost=13.0, queueSize=1}:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:",
				envChanges.toString());
	}

	@Test
	public void testNoPath() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), new UniformCostSearch<>(), "A").setNotifier(me);
		me.addAgent(ma, "E");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(E), Goal=In(A):Search{maxQueueSize=1, nodesExpanded=1, pathCost=0, queueSize=0}:",
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
