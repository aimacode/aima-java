package aima.test.core.unit.search.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.GraphSearch;
import aima.core.search.map.DynAttributeNames;
import aima.core.search.map.ExtendableMap;
import aima.core.search.map.MapAgent;
import aima.core.search.map.MapEnvironment;
import aima.core.search.map.MoveToAction;
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
		ma = new MapAgent(me, new UniformCostSearch(new GraphSearch()),
				new String[] { "A" });
	}

	@Test
	public void testAddAgent() {
		me.addAgent(ma, "E");
		Assert.assertEquals(me.getAgenetLocation(), "E");
	}

	@Test
	public void testExecuteAction() {
		me.addAgent(ma, "D");
		me.executeAction(ma, new MoveToAction("C"));
		Assert.assertEquals(me.getAgenetLocation(), "C");
	}

	@Test
	public void testPerceptSeenBy() {
		me.addAgent(ma, "D");
		DynamicPercept p = (DynamicPercept) me.getPerceptSeenBy(ma);
		Assert.assertEquals(p.getAttribute(DynAttributeNames.PERCEPT_IN), "D");
	}
}
