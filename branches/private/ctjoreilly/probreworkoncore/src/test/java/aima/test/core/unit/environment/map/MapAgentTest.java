package aima.test.core.unit.environment.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.search.uninformed.UniformCostSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapAgentTest {

	ExtendableMap aMap;

	StringBuffer envChanges;

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
		MapAgent ma = new MapAgent(me.getMap(), me, new UniformCostSearch(),
				new String[] { "A" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert
				.assertEquals(
						"CurrentLocation=In(A), Goal=In(A):Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=1:METRIC[queueSize]=0:METRIC[nodesExpanded]=0:Action[name==NoOp]:",
						envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me.getMap(), me, new UniformCostSearch(),
				new String[] { "D" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert
				.assertEquals(
						"CurrentLocation=In(A), Goal=In(D):Action[name==moveTo, location==C]:Action[name==moveTo, location==D]:METRIC[pathCost]=13.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=3:Action[name==NoOp]:",
						envChanges.toString());
	}

	@Test
	public void testNoPath() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me.getMap(), me, new UniformCostSearch(),
				new String[] { "A" });
		me.addAgent(ma, "E");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert
				.assertEquals(
						"CurrentLocation=In(E), Goal=In(A):Action[name==NoOp]:METRIC[pathCost]=0:METRIC[maxQueueSize]=1:METRIC[queueSize]=0:METRIC[nodesExpanded]=1:Action[name==NoOp]:",
						envChanges.toString());
	}

	private class TestEnvironmentView implements EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append(":");
		}

		public void agentAdded(Agent agent, EnvironmentState state) {
			// Nothing
		}

		public void agentActed(Agent agent, Action action,
				EnvironmentState state) {
			envChanges.append(action).append(":");
		}
	}
}
