package aima.core.environment.map;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.util.math.geom.shapes.Point2D;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class MapFunctionFactory {
	private static ResultFunction resultFunction;
	private static PerceptToStateFunction perceptToStateFunction;

	public static ActionsFunction getActionsFunction(Map map) {
		return new MapActionsFunction(map, false);
	}
	
	public static ActionsFunction getReverseActionsFunction(Map map) {
		return new MapActionsFunction(map, true);
	}

	public static ResultFunction getResultFunction() {
		if (null == resultFunction) {
			resultFunction = new MapResultFunction();
		}
		return resultFunction;
	}


	/** Returns an adaptable heuristic based on straight line distance computation. */
	public static HeuristicFunction getSLDHeuristicFunction(Object goal, Map map) {
		return new StraightLineDistanceHeuristicFunction(goal, map);
	}

	/** Returns a heuristic which always returns zero. More optimism is not possible. */
	public static HeuristicFunction getZeroHeuristicFunction() {
		return new ZeroHeuristicFunction();
	}


	private static class MapActionsFunction implements ActionsFunction {
		private Map map = null;
		private boolean reverseMode;

		public MapActionsFunction(Map map, boolean reverseMode) {
			this.map = map;
			this.reverseMode = reverseMode;
		}

		public Set<Action> actions(Object state) {
			Set<Action> actions = new LinkedHashSet<Action>();
			String location = state.toString();

			List<String> linkedLocations = reverseMode ? map.getPossiblePrevLocations(location)
					: map.getPossibleNextLocations(location);
			for (String linkLoc : linkedLocations) {
				actions.add(new MoveToAction(linkLoc));
			}
			return actions;
		}
	}


	public static PerceptToStateFunction getPerceptToStateFunction() {
		if (null == perceptToStateFunction) {
			perceptToStateFunction = new MapPerceptToStateFunction();
		}
		return perceptToStateFunction;
	}

	private static class MapResultFunction implements ResultFunction {
		public MapResultFunction() {
		}

		public Object result(Object s, Action a) {
			if (a instanceof MoveToAction) {
				MoveToAction mta = (MoveToAction) a;
				return mta.getToLocation();
			}
			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}


	private static class MapPerceptToStateFunction implements PerceptToStateFunction {
		public Object getState(Percept p) {
			return ((DynamicPercept) p).getAttribute(DynAttributeNames.PERCEPT_IN);
		}
	}


	private static class StraightLineDistanceHeuristicFunction implements HeuristicFunction {
		private Object goal;
		private Map map;

		public StraightLineDistanceHeuristicFunction(Object goal, Map map) {
			this.goal = goal;
			this.map = map;
		}

		public double h(Object state) {
			double result = 0.0;
			Point2D pt1 = map.getPosition((String) state);
			Point2D pt2 = map.getPosition((String) goal);
			if (pt1 != null && pt2 != null) {
				result = pt1.distance(pt2);
			}
			return result;
		}
	}

	private static class ZeroHeuristicFunction implements HeuristicFunction {

		public double h(Object state) {
			return 0.0;
		}
	}
}
