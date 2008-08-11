package aima.test.search.map;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import aima.search.map.Map;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapTest extends TestCase {

	Map aMap;

	@Override
	public void setUp() {
		aMap = new Map(new String[] { "A", "B", "C", "D", "E" });
		aMap.addBidirectionalLink("A", "B", 5);
		aMap.addBidirectionalLink("A", "C", 6);
		aMap.addBidirectionalLink("B", "C", 4);
		aMap.addBidirectionalLink("C", "D", 7);
		aMap.addUnidirectionalLink("B", "E", 14);
	}

	public void testLocationsLinkedTo() {
		ArrayList<String> locations = new ArrayList<String>();
		List<String> linkedTo;

		linkedTo = aMap.getLocationsLinkedTo("A");
		locations.clear();
		locations.add("B");
		locations.add("C");
		assertTrue(locations.containsAll(linkedTo) && linkedTo.size() == 2);

		linkedTo = aMap.getLocationsLinkedTo("B");
		locations.clear();
		locations.add("A");
		locations.add("C");
		locations.add("E");
		assertTrue(locations.containsAll(linkedTo) && linkedTo.size() == 3);

		linkedTo = aMap.getLocationsLinkedTo("C");
		locations.clear();
		locations.add("A");
		locations.add("B");
		locations.add("D");
		assertTrue(locations.containsAll(linkedTo) && linkedTo.size() == 3);

		linkedTo = aMap.getLocationsLinkedTo("D");
		locations.clear();
		locations.add("C");
		assertTrue(locations.containsAll(linkedTo) && linkedTo.size() == 1);

		linkedTo = aMap.getLocationsLinkedTo("E");
		assertTrue(linkedTo.size() == 0);
	}

	public void testDistances() {
		assertEquals(new Integer(5), aMap.getDistance("A", "B"));
		assertEquals(new Integer(6), aMap.getDistance("A", "C"));
		assertEquals(new Integer(4), aMap.getDistance("B", "C"));
		assertEquals(new Integer(7), aMap.getDistance("C", "D"));
		assertEquals(new Integer(14), aMap.getDistance("B", "E"));
		//
		assertEquals(new Integer(5), aMap.getDistance("B", "A"));
		assertEquals(new Integer(6), aMap.getDistance("C", "A"));
		assertEquals(new Integer(4), aMap.getDistance("C", "B"));
		assertEquals(new Integer(7), aMap.getDistance("D", "C"));

		// No distances should be returned if links not established or locations
		// do not exist
		assertNull(aMap.getDistance("X", "Z"));
		assertNull(aMap.getDistance("A", "Z"));
		assertNull(aMap.getDistance("A", "E"));
		// B->E is unidirectional so should not have opposite direction
		assertNull(aMap.getDistance("E", "B"));
	}

	public void testRandomGeneration() {
		ArrayList<String> locations = new ArrayList<String>();
		locations.add("A");
		locations.add("B");
		locations.add("C");
		locations.add("D");
		locations.add("E");

		for (int i = 0; i < locations.size(); i++) {
			assertTrue(locations.contains(aMap.randomlyGenerateDestination()));
		}
	}
}
