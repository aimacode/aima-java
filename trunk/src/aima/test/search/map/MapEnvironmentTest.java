package aima.test.search.map;

import junit.framework.TestCase;

import aima.basic.Percept;
import aima.search.framework.GraphSearch;
import aima.search.map.Map;
import aima.search.map.MapAgent;
import aima.search.map.MapEnvironment;
import aima.search.uninformed.UniformCostSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapEnvironmentTest extends TestCase {
	MapEnvironment me;

	MapAgent ma;

	@Override
	public void setUp() {
		Map aMap = new Map(new String[] { "A", "B", "C", "D", "E" });
		aMap.addBidirectionalLink("A", "B", 5);
		aMap.addBidirectionalLink("A", "C", 6);
		aMap.addBidirectionalLink("B", "C", 4);
		aMap.addBidirectionalLink("C", "D", 7);
		aMap.addUnidirectionalLink("B", "E", 14);

		me = new MapEnvironment(aMap);
		ma = new MapAgent(me, new UniformCostSearch(new GraphSearch()),
				new String[] { "A" });
	}

	public void testAddAgent() {
		me.addAgent(ma, "E");
		assertEquals(ma.getAttribute("location"), "E");
	}

	public void testExecuteAction() {
		me.addAgent(ma, "D");
		me.executeAction(ma, "C");
		assertEquals(ma.getAttribute("location"), "C");
	}

	public void testPerceptSeenBy() {
		me.addAgent(ma, "D");
		Percept p = me.getPerceptSeenBy(ma);
		assertEquals(p.getAttribute("In"), "D");
	}
}
