package aima.core.environment.map2d;

import java.util.stream.Collectors;

import aima.core.search.api.ActionsFunction;
import aima.core.search.api.GoalTestPredicate;
import aima.core.search.api.ResultFunction;
import aima.core.search.api.StepCostFunction;

/**
 * Utility/convenience class for creating Problem description functions for
 * Map2D environments.
 * 
 * @author Ciaran O'Reilly
 *
 */
public class Map2DFunctionFactory {
	public static ActionsFunction<GoAction, InState> getActionsFunction(Map2D map) {
		return (inState) -> {
			return map.getLocationsLinkedTo(inState.getLocation()).stream().map(GoAction::new)
					.collect(Collectors.toList());
		};
	}

	public static StepCostFunction<GoAction, InState> getStepCostFunction(Map2D map) {
		return (s, a, sPrime) -> {
			return map.getDistance(s.getLocation(), sPrime.getLocation());
		};
	}

	public static ResultFunction<GoAction, InState> getResultFunction(Map2D map) {
		return (state, action) -> new InState(action.getGoTo());
	}

	public static GoalTestPredicate<InState> getGoalTestPredicate(Map2D map, String... goalLocations) {
		return inState -> {
			for (String location : goalLocations) {
				if (location.equals(inState.getLocation())) {
					return true;
				}
			}
			return false;
		};
	}
}