package aima.test.search.map;

import junit.framework.TestCase;
import aima.basic.Percept;
import aima.search.framework.GraphSearch;
import aima.search.map.ExtendableMap;
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
