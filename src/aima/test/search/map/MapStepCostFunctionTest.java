package aima.test.search.map;

import junit.framework.TestCase;
import aima.search.map.ExtendableMap;
import aima.search.map.MapStepCostFunction;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapStepCostFunctionTest extends TestCase {
	MapStepCostFunction mscf;

	@Override
	public void setUp() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		mscf = new MapStepCostFunction(aMap);
	}

	public void testCosts() {
		assertEquals(new Double(5), mscf.calculateStepCost("A", "B", "Go"));
		assertEquals(new Double(6), mscf.calculateStepCost("A", "C", "Go"));
		assertEquals(new Double(4), mscf.calculateStepCost("B", "C", "Go"));
		assertEquals(new Double(7), mscf.calculateStepCost("C", "D", "Go"));
		assertEquals(new Double(14), mscf.calculateStepCost("B", "E", "Go"));
		//
		assertEquals(new Double(5), mscf.calculateStepCost("B", "A", "Go"));
		assertEquals(new Double(6), mscf.calculateStepCost("C", "A", "Go"));
		assertEquals(new Double(4), mscf.calculateStepCost("C", "B", "Go"));
		assertEquals(new Double(7), mscf.calculateStepCost("D", "C", "Go"));
		//
		assertEquals(new Double(1), mscf.calculateStepCost("X", "Z", "Go"));
		assertEquals(new Double(1), mscf.calculateStepCost("A", "Z", "Go"));
		assertEquals(new Double(1), mscf.calculateStepCost("A", "D", "Go"));
		assertEquals(new Double(1), mscf.calculateStepCost("A", "E", "Go"));
	}
}
