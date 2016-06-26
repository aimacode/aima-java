package aima.core.environment.wumpusworld;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.TurnRight;
import aima.core.environment.wumpusworld.action.WWAction;

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
	private static BiFunction<AgentPosition, WWAction, AgentPosition> resultFunction = null;

	public static Function<AgentPosition, List<WWAction>> getActionsFunction(WumpusCave cave) {
		return new WumpusActionsFunction(cave);
	}

	public static BiFunction<AgentPosition, WWAction, AgentPosition> getResultFunction() {
		if (null == resultFunction) {
			resultFunction = new WumpusResultFunction();
		}
		return resultFunction;
	}

	private static class WumpusActionsFunction implements Function<AgentPosition, List<WWAction>> {
		private WumpusCave cave;

		public WumpusActionsFunction(WumpusCave cave) {
			this.cave = cave;
		}

		@Override
		public List<WWAction> apply(AgentPosition t) {
			return actions(t);
		}

		private List<WWAction> actions(AgentPosition state) {
			List<WWAction> actions = new LinkedList<>();
			AgentPosition position = null;
			try {
				position = (AgentPosition) state;
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<AgentPosition> linkedPositions = cave.getLocationsLinkedTo(position);
			for (AgentPosition linkPos : linkedPositions) {
				if (linkPos.getX() != position.getX() || linkPos.getY() != position.getY()) {
					actions.add((WWAction) new Forward(position));
				}
			}
			actions.add((WWAction) new TurnLeft(position.getOrientation()));
			actions.add((WWAction) new TurnRight(position.getOrientation()));

			return actions;
		}
	}

	private static class WumpusResultFunction implements BiFunction<AgentPosition, WWAction, AgentPosition> {

		@Override
		public AgentPosition apply(AgentPosition t, WWAction u) {
			return result(t, u);
		}

		private AgentPosition result(AgentPosition s, WWAction a) {

			if (a instanceof Forward) {
				Forward fa = (Forward) a;

				return fa.getToPosition();
			} else if (a instanceof TurnLeft) {
				TurnLeft tLeft = (TurnLeft) a;
				AgentPosition res = (AgentPosition) s;

				return new AgentPosition(res.getX(), res.getY(), tLeft.getToOrientation());
			} else if (a instanceof TurnRight) {
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
