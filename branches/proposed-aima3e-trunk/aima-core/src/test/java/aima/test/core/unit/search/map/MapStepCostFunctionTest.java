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
		Assert.assertEquals(new Double(5), mscf.cost("A",
				new MoveToAction("B"), "B"));
		Assert.assertEquals(new Double(6), mscf.cost("A",
				new MoveToAction("C"), "C"));
		Assert.assertEquals(new Double(4), mscf.cost("B",
				new MoveToAction("C"), "C"));
		Assert.assertEquals(new Double(7), mscf.cost("C",
				new MoveToAction("D"), "D"));
		Assert.assertEquals(new Double(14), mscf.cost("B",
				new MoveToAction("E"), "E"));
		//
		Assert.assertEquals(new Double(5), mscf.cost("B",
				new MoveToAction("A"), "A"));
		Assert.assertEquals(new Double(6), mscf.cost("C",
				new MoveToAction("A"), "A"));
		Assert.assertEquals(new Double(4), mscf.cost("C",
				new MoveToAction("B"), "B"));
		Assert.assertEquals(new Double(7), mscf.cost("D",
				new MoveToAction("C"), "C"));
		//
		Assert.assertEquals(new Double(1), mscf.cost("X",
				new MoveToAction("Z"), "Z"));
		Assert.assertEquals(new Double(1), mscf.cost("A",
				new MoveToAction("Z"), "Z"));
		Assert.assertEquals(new Double(1), mscf.cost("A",
				new MoveToAction("D"), "D"));
		Assert.assertEquals(new Double(1), mscf.cost("A",
				new MoveToAction("B"), "E"));
	}
}
