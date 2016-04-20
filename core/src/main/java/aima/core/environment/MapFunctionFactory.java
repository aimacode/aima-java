package aima.core.environment.map;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.search.framework.ResultFunction;

/**
 * 
 * @author Subham Mishra
 * @author Ciaran O'Reilly
 * 
 */
public class MapFunctionFactory<A,P> {
	private static ResultFunction _resultFunction = null;
	private static PerceptToStateFunction _perceptToStateFunction = null;

	@SuppressWarnings("rawtypes")
	public static ActionsFunction getActionsFunction(Map map) {
		return (ActionsFunction) new MapActionsFunction(map);
	}

	public static <A> ResultFunction<A> getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction =  new MapResultFunction<A>();
		}
		return _resultFunction;
	}

	private static class MapActionsFunction<A,P> implements ActionsFunction<A> {
		private Map map = null;

		public MapActionsFunction(Map map) {
			this.map = map;
		}
		@SuppressWarnings("unchecked")
		public Set<A> actions(Object state) {
			Set<A> actions = new LinkedHashSet<A>();
			String location = state.toString();

			List<String> linkedLocations = map.getLocationsLinkedTo(location);
			for (String linkLoc : linkedLocations) {
				actions.add((A) new MoveToAction(linkLoc));
			}

			return actions;
		}
	}

	@SuppressWarnings("rawtypes")
	public static PerceptToStateFunction getPerceptToStateFunction() {
		if (null == _perceptToStateFunction) {
			_perceptToStateFunction = new MapPerceptToStateFunction();
		}
		return _perceptToStateFunction;
	}

	private static class MapResultFunction<A> implements ResultFunction<A> {
		public MapResultFunction() {
		}

		public Object result(Object s, A a) {

			if (a instanceof MoveToAction) {
				MoveToAction mta = (MoveToAction) a;

				return mta.getToLocation();
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}

	private static class MapPerceptToStateFunction<P> implements
			PerceptToStateFunction<P> {
		
		public  MapPerceptToStateFunction() {
		}
		@SuppressWarnings("unchecked")
		public Object getState(P p) {
			return ((DynamicPercept<P>) p)
					.getAttribute(DynAttributeNames.PERCEPT_IN);
		}
	}
}
