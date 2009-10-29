package aima.test.core.unit.search.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.map.ExtendableMap;
import aima.core.search.map.MapStepCostFunction;
import aima.core.search.map.MoveToAction;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapStepCostFunctionTest {
	MapStepCostFunction mscf;

	@Before
	public void setUp() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		mscf = new MapStepCostFunction(aMap);
	}

	@Test
	public void testCosts() {
		Assert.assertEquals(new Double(5), mscf.cost("A", new MoveToAction("B", 5.0), "B"));
		Assert.assertEquals(new Double(6), mscf.cost("A", new MoveToAction("C", 6.0), "C"));
		Assert.assertEquals(new Double(4), mscf.cost("B", new MoveToAction("C", 4.0), "C"));
		Assert.assertEquals(new Double(7), mscf.cost("C", new MoveToAction("D", 7.0), "D"));
		Assert.assertEquals(new Double(14), mscf.cost("B", new MoveToAction("E", 14.0), "E"));
		//
		Assert.assertEquals(new Double(5), mscf.cost("B", new MoveToAction("A", 5.0), "A"));
		Assert.assertEquals(new Double(6), mscf.cost("C", new MoveToAction("A", 6.0), "A"));
		Assert.assertEquals(new Double(4), mscf.cost("C", new MoveToAction("B", 4.0), "B"));
		Assert.assertEquals(new Double(7), mscf.cost("D", new MoveToAction("C", 7.0), "C"));
		//
		Assert.assertEquals(new Double(1), mscf.cost("X", new MoveToAction("Z", 0.0), "Z"));
		Assert.assertEquals(new Double(1), mscf.cost("A", new MoveToAction("Z", 0.0), "Z"));
		Assert.assertEquals(new Double(1), mscf.cost("A", new MoveToAction("D", 0.0), "D"));
		Assert.assertEquals(new Double(1), mscf.cost("A", new MoveToAction("B", 0.0), "E"));
	}
}
