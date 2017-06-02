package aima.test.core.unit.environment.map;

import java.util.ArrayList;

import aima.core.environment.map.MapFunctions;
import aima.core.search.framework.problem.StepCostFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class MapFunctionsTest {
	private ActionsFunction<String, MoveToAction> actionsFn;
	private ResultFunction<String, MoveToAction> resultFn;
	private StepCostFunction<String, MoveToAction> stepCostFn;

	@Before
	public void setUp() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		actionsFn = MapFunctions.createActionsFunction(aMap);
		resultFn = MapFunctions.createResultFunction();
		stepCostFn = MapFunctions.createDistanceStepCostFunction(aMap);
	}

	@Test
	public void testSuccessors() {
		ArrayList<String> locations = new ArrayList<>();

		// A
		locations.clear();
		locations.add("B");
		locations.add("C");
		for (MoveToAction a : actionsFn.apply("A")) {
			Assert.assertTrue(locations.contains(a.getToLocation()));
			Assert.assertTrue(locations.contains(resultFn.apply("A", a)));
		}

		// B
		locations.clear();
		locations.add("A");
		locations.add("C");
		locations.add("E");
		for (MoveToAction a : actionsFn.apply("B")) {
			Assert.assertTrue(locations.contains(a.getToLocation()));
			Assert.assertTrue(locations.contains(resultFn.apply("B", a)));
		}

		// C
		locations.clear();
		locations.add("A");
		locations.add("B");
		locations.add("D");
		for (MoveToAction a : actionsFn.apply("C")) {
			Assert.assertTrue(locations.contains(a.getToLocation()));
			Assert.assertTrue(locations.contains(resultFn.apply("C", a)));
		}

		// D
		locations.clear();
		locations.add("C");
		for (MoveToAction a : actionsFn.apply("D")) {
			Assert.assertTrue(locations.contains(a.getToLocation()));
			Assert.assertTrue(locations.contains(resultFn.apply("D", a)));
		}
		// E
		locations.clear();
		Assert.assertTrue(0 == actionsFn.apply("E").size());
	}

	@Test
	public void testCosts() {
		Assert.assertEquals(5.0, stepCostFn.applyAsDouble("A", new MoveToAction("B"), "B"), 0.001);
		Assert.assertEquals(6.0, stepCostFn.applyAsDouble("A", new MoveToAction("C"), "C"), 0.001);
		Assert.assertEquals(4.0, stepCostFn.applyAsDouble("B", new MoveToAction("C"), "C"), 0.001);
		Assert.assertEquals(7.0, stepCostFn.applyAsDouble("C", new MoveToAction("D"), "D"), 0.001);
		Assert.assertEquals(14.0, stepCostFn.applyAsDouble("B", new MoveToAction("E"), "E"),
				0.001);
		//
		Assert.assertEquals(5.0, stepCostFn.applyAsDouble("B", new MoveToAction("A"), "A"), 0.001);
		Assert.assertEquals(6.0, stepCostFn.applyAsDouble("C", new MoveToAction("A"), "A"), 0.001);
		Assert.assertEquals(4.0, stepCostFn.applyAsDouble("C", new MoveToAction("B"), "B"), 0.001);
		Assert.assertEquals(7.0, stepCostFn.applyAsDouble("D", new MoveToAction("C"), "C"), 0.001);
		//
		Assert.assertEquals(1.0, stepCostFn.applyAsDouble("X", new MoveToAction("Z"), "Z"), 0.001);
		Assert.assertEquals(1.0, stepCostFn.applyAsDouble("A", new MoveToAction("Z"), "Z"), 0.001);
		Assert.assertEquals(1.0, stepCostFn.applyAsDouble("A", new MoveToAction("D"), "D"), 0.001);
		Assert.assertEquals(1.0, stepCostFn.applyAsDouble("A", new MoveToAction("B"), "E"), 0.001);
	}
}
