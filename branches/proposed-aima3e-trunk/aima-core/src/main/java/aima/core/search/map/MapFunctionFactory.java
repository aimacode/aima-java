package aima.core.search.map;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * Implementation of SuccessorFunction interface that derives successors based on the
 * locations linked/traversible to the current location.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapFunctionFactory {
	private static ResultFunction _resultFunction = null;
	
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
			
			// TODO-Why are we doing this?
			if (state instanceof DynamicPercept) {
				location = (String) ((DynamicPercept) state)
						.getAttribute(DynAttributeNames.PERCEPT_IN);
			}

			List<String> linkedLocations = map.getLocationsLinkedTo(location);
			for (String linkLoc : linkedLocations) {
				actions.add(new MoveToAction(linkLoc, map.getDistance(location,
						linkLoc)));
			}

			return actions;
		}
	}

	private static class MapResultFunction implements ResultFunction {
		public MapResultFunction() {
		}

		public Object result(Object s, Action a) {
			
			if (a instanceof MoveToAction) {
				MoveToAction mta = (MoveToAction) a;
				
				Object newLocation = mta.getToLocation();
				
				// TODO-Due to the code above with DynamicPercept
				// I'm being forced to create and turn one here, why?
				if (s instanceof DynamicPercept) {
					newLocation = ((DynamicPercept)s).copy();
					((DynamicPercept)newLocation).setAttribute(DynAttributeNames.PERCEPT_IN, mta.getToLocation());
				}
				
				return newLocation;
			}
				
			// The Action is not understood or is a NoOp
			// the result will be the current state.	
			return s;
		}
	}
}
