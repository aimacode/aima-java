package aima.core.environment.map;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.search.framework.ResultFunction;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapFunctionFactory {
	private static ResultFunction _resultFunction = null;
	private static PerceptToStateFunction _perceptToStateFunction = null;

	public static ActionsFunction getActionsFunction(Map aMap) {
		return new MapActionsFunction(aMap);
	}

	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new MapResultFunction();
		}
		return _resultFunction;
	}

	private static class MapActionsFunction implements ActionsFunction {
		private Map map = null;

		public MapActionsFunction(Map aMap) {
			map = aMap;
		}

		public Set<Action> actions(Object state) {
			Set<Action> actions = new LinkedHashSet<Action>();
			String location = state.toString();

			List<String> linkedLocations = map.getLocationsLinkedTo(location);
			for (String linkLoc : linkedLocations) {
				actions.add(new MoveToAction(linkLoc));
			}

			return actions;
		}
	}

	public static PerceptToStateFunction getPerceptToStateFunction() {
		if (null == _perceptToStateFunction) {
			_perceptToStateFunction = new MapPerceptToStateFunction();
		}
		return _perceptToStateFunction;
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

	private static class MapPerceptToStateFunction implements
			PerceptToStateFunction {
		public Object getState(Percept p) {
			return ((DynamicPercept) p)
					.getAttribute(DynAttributeNames.PERCEPT_IN);
		}
	}
}
