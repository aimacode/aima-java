package aima.test.search.map;

import junit.framework.TestCase;

import aima.search.map.Map;
import aima.search.map.MapStepCostFunction;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapStepCostFunctionTest extends TestCase {
	MapStepCostFunction mscf;

	@Override
	public void setUp() {
		Map aMap = new Map(new String[] { "A", "B", "C", "D", "E" });
		aMap.addBidirectionalLink("A", "B", 5);
		aMap.addBidirectionalLink("A", "C", 6);
		aMap.addBidirectionalLink("B", "C", 4);
		aMap.addBidirectionalLink("C", "D", 7);
		aMap.addUnidirectionalLink("B", "E", 14);

		mscf = new MapStepCostFunction(aMap);
	}

	public void testCosts() {
		assertEquals(new Double(6), mscf.calculateStepCost("A", "B", "Go"));
		assertEquals(new Double(7), mscf.calculateStepCost("A", "C", "Go"));
		assertEquals(new Double(5), mscf.calculateStepCost("B", "C", "Go"));
		assertEquals(new Double(8), mscf.calculateStepCost("C", "D", "Go"));
		assertEquals(new Double(15), mscf.calculateStepCost("B", "E", "Go"));
		//
		assertEquals(new Double(6), mscf.calculateStepCost("B", "A", "Go"));
		assertEquals(new Double(7), mscf.calculateStepCost("C", "A", "Go"));
		assertEquals(new Double(5), mscf.calculateStepCost("C", "B", "Go"));
		assertEquals(new Double(8), mscf.calculateStepCost("D", "C", "Go"));
		//
		assertEquals(new Double(1), mscf.calculateStepCost("X", "Z", "Go"));
		assertEquals(new Double(1), mscf.calculateStepCost("A", "Z", "Go"));
		assertEquals(new Double(1), mscf.calculateStepCost("A", "D", "Go"));
		assertEquals(new Double(1), mscf.calculateStepCost("A", "E", "Go"));
	}
}
