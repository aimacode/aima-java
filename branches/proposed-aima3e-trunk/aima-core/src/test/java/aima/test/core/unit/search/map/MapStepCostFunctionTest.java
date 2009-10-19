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
		Assert.assertEquals(new Double(5), mscf.calculateStepCost("A", "B",
				new MoveToAction("B", 5.0)));
		Assert.assertEquals(new Double(6), mscf.calculateStepCost("A", "C",
				new MoveToAction("C", 6.0)));
		Assert.assertEquals(new Double(4), mscf.calculateStepCost("B", "C",
				new MoveToAction("C", 4.0)));
		Assert.assertEquals(new Double(7), mscf.calculateStepCost("C", "D",
				new MoveToAction("D", 7.0)));
		Assert.assertEquals(new Double(14), mscf.calculateStepCost("B", "E",
				new MoveToAction("E", 14.0)));
		//
		Assert.assertEquals(new Double(5), mscf.calculateStepCost("B", "A",
				new MoveToAction("A", 5.0)));
		Assert.assertEquals(new Double(6), mscf.calculateStepCost("C", "A",
				new MoveToAction("A", 6.0)));
		Assert.assertEquals(new Double(4), mscf.calculateStepCost("C", "B",
				new MoveToAction("B", 4.0)));
		Assert.assertEquals(new Double(7), mscf.calculateStepCost("D", "C",
				new MoveToAction("C", 7.0)));
		//
		Assert.assertEquals(new Double(1), mscf.calculateStepCost("X", "Z",
				new MoveToAction("Z", 0.0)));
		Assert.assertEquals(new Double(1), mscf.calculateStepCost("A", "Z",
				new MoveToAction("Z", 0.0)));
		Assert.assertEquals(new Double(1), mscf.calculateStepCost("A", "D",
				new MoveToAction("D", 0.0)));
		Assert.assertEquals(new Double(1), mscf.calculateStepCost("A", "E",
				new MoveToAction("B", 0.0)));
	}
}
