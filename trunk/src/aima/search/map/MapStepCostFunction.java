package aima.search.map;

import aima.basic.Percept;
import aima.search.framework.StepCostFunction;

/**
 * Implementation of StepCostFunction interface that uses the distance between locations
 * to calculate the cost in addition to a constant cost, so that it may be used
 * in conjunction with a Uniform-cost search.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapStepCostFunction implements StepCostFunction {
	private Map map = null;

	//
	// Used by Uniform-cost search to ensure every step is greater than or equal
	// to some small positive constant
	private static double constantCost = 1.0;

	public MapStepCostFunction(Map aMap) {
		this.map = aMap;
	}

	public Double calculateStepCost(Object fromCurrentState,
			Object toNextState, String action) {

		String fromLoc = fromCurrentState.toString();
		String toLoc = toNextState.toString();
		if (fromCurrentState instanceof Percept) {
			fromLoc = (String) ((Percept) fromCurrentState)
					.getAttribute(MapEnvironment.STATE_IN);
			toLoc = (String) ((Percept) toNextState)
					.getAttribute(MapEnvironment.STATE_IN);
		}

		Integer distance = map.getDistance(fromLoc, toLoc);

		if (null == distance || distance < 0) {
			return constantCost;
		}

		return constantCost + new Double(distance);
	}
}
