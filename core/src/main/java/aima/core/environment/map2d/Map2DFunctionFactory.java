package aima.core.environment.map2d;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import aima.core.search.api.ActionsFunction;
import aima.core.search.api.GoalTestPredicate;
import aima.core.search.api.Node;
import aima.core.search.api.ResultFunction;
import aima.core.search.api.StepCostFunction;
import aima.core.util.datastructure.Point2D;

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
	
	public static class StraightLineDistanceHeuristic implements ToDoubleFunction<Node<GoAction, InState>> {

		private final Map2D map;
		private final String[] goals;

		public StraightLineDistanceHeuristic(Map2D map, String... goals) {
			this.map   = map;
			this.goals = goals;
		}

		@Override
		public double applyAsDouble(Node<GoAction, InState> node) {
			return h(node.state());
		}

		private double h(InState state) {
			return Arrays.stream(goals).map(goal -> {
				Point2D currentPosition = map.getPosition(state.getLocation());
				Point2D goalPosition = map.getPosition(goal);
				return distanceOf(currentPosition, goalPosition);
			}).min(Double::compareTo).orElse(Double.MAX_VALUE);
		}

		private double distanceOf(Point2D p1, Point2D p2) {
			return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
					+ (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
		}
	}
}