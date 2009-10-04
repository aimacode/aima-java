package aima.test.search.map;

import java.util.ArrayList;

import junit.framework.TestCase;
import aima.search.framework.Successor;
import aima.search.map.ExtendableMap;
import aima.search.map.MapSuccessorFunction;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapSuccessorFunctionTest extends TestCase {
	MapSuccessorFunction msf;

	@Override
	public void setUp() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		msf = new MapSuccessorFunction(aMap);
	}

	public void testSuccessors() {
		ArrayList<String> locations = new ArrayList<String>();

		// A
		locations.clear();
		locations.add("B");
		locations.add("C");
		for (Object o : msf.getSuccessors("A")) {
			Successor s = (Successor) o;
			assertTrue(locations.contains(s.getAction())
					&& locations.contains(s.getState()));
		}

		// B
		locations.clear();
		locations.add("A");
		locations.add("C");
		locations.add("E");
		for (Object o : msf.getSuccessors("B")) {
			Successor s = (Successor) o;
			assertTrue(locations.contains(s.getAction())
					&& locations.contains(s.getState()));
		}

		// C
		locations.clear();
		locations.add("A");
		locations.add("B");
		locations.add("D");
		for (Object o : msf.getSuccessors("C")) {
			Successor s = (Successor) o;
			assertTrue(locations.contains(s.getAction())
					&& locations.contains(s.getState()));
		}

		// D
		locations.clear();
		locations.add("C");
		for (Object o : msf.getSuccessors("D")) {
			Successor s = (Successor) o;
			assertTrue(locations.contains(s.getAction())
					&& locations.contains(s.getState()));
		}

		// E
		locations.clear();
		assertTrue(0 == msf.getSuccessors("E").size());
	}
}
