package aima.test.core.unit.environment.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.DynamicPercept;
import aima.core.environment.map.DynAttributeNames;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MoveToAction;
import aima.core.search.uninformed.UniformCostSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapEnvironmentTest {
	MapEnvironment me;

	MapAgent ma;

	@Before
	public void setUp() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		me = new MapEnvironment(aMap);
		ma = new MapAgent(me.getMap(), me, new UniformCostSearch(),
				new String[] { "A" });
	}

	@Test
	public void testAddAgent() {
		me.addAgent(ma, "E");
		Assert.assertEquals(me.getAgentLocation(ma), "E");
	}

	@Test
	public void testExecuteAction() {
		me.addAgent(ma, "D");
		me.executeAction(ma, new MoveToAction("C"));
		Assert.assertEquals(me.getAgentLocation(ma), "C");
	}

	@Test
	public void testPerceptSeenBy() {
		me.addAgent(ma, "D");
		DynamicPercept p = (DynamicPercept) me.getPerceptSeenBy(ma);
		Assert.assertEquals(p.getAttribute(DynAttributeNames.PERCEPT_IN), "D");
	}

	@Test
	public void testTwoAgentsSupported() {
		MapAgent ma1 = new MapAgent(me.getMap(), me, new UniformCostSearch(),
				new String[] { "A" });
		MapAgent ma2 = new MapAgent(me.getMap(), me, new UniformCostSearch(),
				new String[] { "A" });

		me.addAgent(ma1, "A");
		me.addAgent(ma2, "A");
		me.executeAction(ma1, new MoveToAction("B"));
		me.executeAction(ma2, new MoveToAction("C"));

		Assert.assertEquals(me.getAgentLocation(ma1), "B");
		Assert.assertEquals(me.getAgentLocation(ma2), "C");
	}
}
