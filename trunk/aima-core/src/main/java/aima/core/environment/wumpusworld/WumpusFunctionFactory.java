package aima.core.environment.wumpusworld;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.TurnRight;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * Factory class for constructing functions for use in the Wumpus World environment.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 */
public class WumpusFunctionFactory {
	private static ResultFunction resultFunction = null;

	public static ActionsFunction getActionsFunction(WumpusCave cave) {
		return new WumpusActionsFunction(cave);
	}

	public static ResultFunction getResultFunction() {
		if (null == resultFunction) {
			resultFunction = new WumpusResultFunction();
		}
		return resultFunction;
	}

	private static class WumpusActionsFunction implements ActionsFunction {
		private WumpusCave cave;

		public WumpusActionsFunction(WumpusCave cave) {
			this.cave = cave;
		}

		@Override
		public Set<Action> actions(Object state) {
			Set<Action> actions = new LinkedHashSet<Action>();
			AgentPosition position = null;
			try {
				position = (AgentPosition) state;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<AgentPosition> linkedPositions = cave.getLocationsLinkedTo(position);
			for (AgentPosition linkPos : linkedPositions) {
				if (linkPos.getX() != position.getX() || linkPos.getY() != position.getY()) {
					actions.add(new Forward(position));
				}
			}
			actions.add(new TurnLeft(position.getOrientation()));
			actions.add(new TurnRight(position.getOrientation()));

			return actions;
		}
	}

	private static class WumpusResultFunction implements ResultFunction {
		public WumpusResultFunction() {
		}

		@Override
		public Object result(Object s, Action a) {

			if (a instanceof Forward) {
				Forward fa = (Forward) a;
				
				return fa.getToPosition();
			}
			else if (a instanceof TurnLeft) {
				TurnLeft tLeft = (TurnLeft) a;
				AgentPosition res = (AgentPosition) s;

				return new AgentPosition(res.getX(), res.getY(), tLeft.getToOrientation());
			}
			else if (a instanceof TurnRight) {
				TurnRight tRight = (TurnRight) a;
				AgentPosition res = (AgentPosition) s;

				return new AgentPosition(res.getX(), res.getY(), tRight.getToOrientation());
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}
