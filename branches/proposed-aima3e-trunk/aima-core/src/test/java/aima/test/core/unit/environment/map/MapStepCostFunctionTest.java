package aima.test.core.unit.environment.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapStepCostFunction;
import aima.core.environment.map.MoveToAction;

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
		Assert
				.assertEquals(5.0, mscf.c("A", new MoveToAction("B"), "B"),
						0.001);
		Assert
				.assertEquals(6.0, mscf.c("A", new MoveToAction("C"), "C"),
						0.001);
		Assert
				.assertEquals(4.0, mscf.c("B", new MoveToAction("C"), "C"),
						0.001);
		Assert
				.assertEquals(7.0, mscf.c("C", new MoveToAction("D"), "D"),
						0.001);
		Assert.assertEquals(14.0, mscf.c("B", new MoveToAction("E"), "E"),
				0.001);
		//
		Assert
				.assertEquals(5.0, mscf.c("B", new MoveToAction("A"), "A"),
						0.001);
		Assert
				.assertEquals(6.0, mscf.c("C", new MoveToAction("A"), "A"),
						0.001);
		Assert
				.assertEquals(4.0, mscf.c("C", new MoveToAction("B"), "B"),
						0.001);
		Assert
				.assertEquals(7.0, mscf.c("D", new MoveToAction("C"), "C"),
						0.001);
		//
		Assert
				.assertEquals(1.0, mscf.c("X", new MoveToAction("Z"), "Z"),
						0.001);
		Assert
				.assertEquals(1.0, mscf.c("A", new MoveToAction("Z"), "Z"),
						0.001);
		Assert
				.assertEquals(1.0, mscf.c("A", new MoveToAction("D"), "D"),
						0.001);
		Assert
				.assertEquals(1.0, mscf.c("A", new MoveToAction("B"), "E"),
						0.001);
	}
}
