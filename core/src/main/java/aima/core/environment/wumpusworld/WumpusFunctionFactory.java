package aima.core.environment.wumpusworld;

import java.util.LinkedList;
import java.util.List;

import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.TurnRight;
import aima.core.environment.wumpusworld.action.WWAction;
import aima.core.search.api.ActionsFunction;
import aima.core.search.api.ResultFunction;

/**
 * Factory class for constructing functions for use in the Wumpus World
 * environment.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class WumpusFunctionFactory {
	private static final ResultFunction<WWAction, AgentPosition> _resultFunction = new WumpusResultFunction();;

	public static ActionsFunction<WWAction, AgentPosition> getActionsFunction(WumpusCave cave) {
		return new WumpusActionsFunction(cave);
	}

	public static ResultFunction<WWAction, AgentPosition> getResultFunction() {
		return _resultFunction;
	}

	private static class WumpusActionsFunction implements ActionsFunction<WWAction, AgentPosition> {
		private WumpusCave cave;

		public WumpusActionsFunction(WumpusCave cave) {
			this.cave = cave;
		}

		@Override
		public List<WWAction> actions(AgentPosition state) {
			List<WWAction> actions = new LinkedList<>();

			List<AgentPosition> linkedPositions = cave.getLocationsLinkedTo(state);
			for (AgentPosition linkPos : linkedPositions) {
				if (linkPos.getX() != state.getX() || linkPos.getY() != state.getY()) {
					actions.add(new Forward(state));
				}
			}
			actions.add(new TurnLeft(state.getOrientation()));
			actions.add(new TurnRight(state.getOrientation()));

			return actions;
		}
	}

	private static class WumpusResultFunction implements ResultFunction<WWAction, AgentPosition> {
		@Override
		public AgentPosition result(AgentPosition s, WWAction a) {

			if (a instanceof Forward) {
				Forward fa = (Forward) a;

				return fa.getToPosition();
			} else if (a instanceof TurnLeft) {
				TurnLeft tLeft = (TurnLeft) a;
				AgentPosition res = s;

				return new AgentPosition(res.getX(), res.getY(), tLeft.getToOrientation());
			} else if (a instanceof TurnRight) {
				TurnRight tRight = (TurnRight) a;
				AgentPosition res = s;

				return new AgentPosition(res.getX(), res.getY(), tRight.getToOrientation());
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}
