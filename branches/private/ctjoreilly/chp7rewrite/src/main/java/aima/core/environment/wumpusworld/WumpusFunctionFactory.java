package aima.core.environment.wumpusworld;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * @author Federico Baron
 * @author Alessandro Daniele
 * 
 */
public class WumpusFunctionFactory {
	private static ResultFunction resultFunction = null;

	public static ActionsFunction getActionsFunction(WumpusCave field) {
		return new WumpusActionsFunction(field);
	}

	public static ResultFunction getResultFunction() {
		if (null == resultFunction) {
			resultFunction = new WumpusResultFunction();
		}
		return resultFunction;
	}

	private static class WumpusActionsFunction implements ActionsFunction {
		private WumpusCave field;

		public WumpusActionsFunction(WumpusCave field) {
			this.field = field;
		}

		@Override
		public Set<Action> actions(Object state) {
			Set<Action> actions = new LinkedHashSet<Action>();
			WumpusPosition position = null;
			try {
				position = (WumpusPosition) state;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<WumpusPosition> linkedPositions = field.getLocationsLinkedTo(position);
			for (WumpusPosition linkPos : linkedPositions) {
				if (linkPos.getLocation().getX() != position.getLocation().getX() || linkPos.getLocation().getY() != position.getLocation().getY())
					actions.add(new ForwardAction(position));
			}
			actions.add(new TurnAction(position.getOrientation(),TurnAction.DIRECTION_LEFT));
			actions.add(new TurnAction(position.getOrientation(),TurnAction.DIRECTION_RIGHT));

			return actions;
		}
	}

	private static class WumpusResultFunction implements ResultFunction {
		public WumpusResultFunction() {
		}

		@Override
		public Object result(Object s, Action a) {

			if (a instanceof ForwardAction) {
				ForwardAction fa = (ForwardAction) a;
				
				return fa.getToPosition();
			}
			
			if (a instanceof TurnAction) {
				TurnAction ta = (TurnAction) a;
				WumpusPosition res = (WumpusPosition) s;

				return new WumpusPosition((int)res.getLocation().getX(), (int)res.getLocation().getY(), ta.getToOrientation());
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}
