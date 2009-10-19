package aima.test.core.unit.search.map;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.framework.Successor;
import aima.core.search.map.ExtendableMap;
import aima.core.search.map.MapSuccessorFunction;
import aima.core.search.map.MoveToAction;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapSuccessorFunctionTest {
	MapSuccessorFunction msf;

	@Before
	public void setUp() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		msf = new MapSuccessorFunction(aMap);
	}

	@Test
	public void testSuccessors() {
		ArrayList<String> locations = new ArrayList<String>();

		// A
		locations.clear();
		locations.add("B");
		locations.add("C");
		for (Object o : msf.getSuccessors("A")) {
			Successor s = (Successor) o;
			Assert.assertTrue(locations.contains(((MoveToAction)s.getAction()).getToLocation())
					&& locations.contains(s.getState()));
		}

		// B
		locations.clear();
		locations.add("A");
		locations.add("C");
		locations.add("E");
		for (Object o : msf.getSuccessors("B")) {
			Successor s = (Successor) o;
			Assert.assertTrue(locations.contains(((MoveToAction)s.getAction()).getToLocation())
					&& locations.contains(s.getState()));
		}

		// C
		locations.clear();
		locations.add("A");
		locations.add("B");
		locations.add("D");
		for (Object o : msf.getSuccessors("C")) {
			Successor s = (Successor) o;
			Assert.assertTrue(locations.contains(((MoveToAction)s.getAction()).getToLocation())
					&& locations.contains(s.getState()));
		}

		// D
		locations.clear();
		locations.add("C");
		for (Object o : msf.getSuccessors("D")) {
			Successor s = (Successor) o;
			Assert.assertTrue(locations.contains(((MoveToAction)s.getAction()).getToLocation())
					&& locations.contains(s.getState()));
		}

		// E
		locations.clear();
		Assert.assertTrue(0 == msf.getSuccessors("E").size());
	}
}
