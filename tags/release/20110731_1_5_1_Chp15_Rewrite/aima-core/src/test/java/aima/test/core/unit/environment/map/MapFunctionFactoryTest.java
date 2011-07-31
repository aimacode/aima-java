package aima.test.core.unit.environment.map;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapFunctionFactoryTest {
	ActionsFunction af;
	ResultFunction rf;

	@Before
	public void setUp() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		af = MapFunctionFactory.getActionsFunction(aMap);
		rf = MapFunctionFactory.getResultFunction();
	}

	@Test
	public void testSuccessors() {
		ArrayList<String> locations = new ArrayList<String>();

		// A
		locations.clear();
		locations.add("B");
		locations.add("C");
		for (Action a : af.actions("A")) {
			Assert.assertTrue(locations.contains(((MoveToAction) a)
					.getToLocation()));
			Assert.assertTrue(locations.contains(rf.result("A", a)));
		}

		// B
		locations.clear();
		locations.add("A");
		locations.add("C");
		locations.add("E");
		for (Action a : af.actions("B")) {
			Assert.assertTrue(locations.contains(((MoveToAction) a)
					.getToLocation()));
			Assert.assertTrue(locations.contains(rf.result("B", a)));
		}

		// C
		locations.clear();
		locations.add("A");
		locations.add("B");
		locations.add("D");
		for (Action a : af.actions("C")) {
			Assert.assertTrue(locations.contains(((MoveToAction) a)
					.getToLocation()));
			Assert.assertTrue(locations.contains(rf.result("C", a)));
		}

		// D
		locations.clear();
		locations.add("C");
		for (Action a : af.actions("D")) {
			Assert.assertTrue(locations.contains(((MoveToAction) a)
					.getToLocation()));
			Assert.assertTrue(locations.contains(rf.result("D", a)));
		}
		// E
		locations.clear();
		Assert.assertTrue(0 == af.actions("E").size());
	}
}
