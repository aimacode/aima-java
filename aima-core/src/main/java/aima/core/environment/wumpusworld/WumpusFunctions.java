package aima.core.environment.wumpusworld;

import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for constructing functions for use in the Wumpus World environment.
 *
 * @author Ruediger Lunde
 */
public class WumpusFunctions {

	public static ActionsFunction<AgentPosition, WumpusAction> createActionsFunction(WumpusCave cave) {
		return new WumpusActionsFunction(cave);
	}

	public static ResultFunction<AgentPosition, WumpusAction> createResultFunction(WumpusCave cave) {
		return new WumpusResultFunction(cave);
	}


	private static class WumpusActionsFunction implements ActionsFunction<AgentPosition, WumpusAction> {
		private WumpusCave cave;

		private WumpusActionsFunction(WumpusCave cave) {
			this.cave = cave;
		}

		@Override
		public List<WumpusAction> apply(AgentPosition state) {
			List<WumpusAction> actions = new ArrayList<>();
			
			AgentPosition pos = cave.moveForward(state);
			if (!pos.equals(state))
				actions.add(WumpusAction.FORWARD);
			actions.add(WumpusAction.TURN_LEFT);
			actions.add(WumpusAction.TURN_RIGHT);

			return actions;
		}
	}

	private static class WumpusResultFunction implements ResultFunction<AgentPosition, WumpusAction> {
		private WumpusCave cave;

		private WumpusResultFunction(WumpusCave cave) {
			this.cave = cave;
		}

		@Override
		public AgentPosition apply(AgentPosition state, WumpusAction action) {
			AgentPosition result = state;
			switch (action) {
				case FORWARD: result = cave.moveForward(state); break;
				case TURN_LEFT: result = cave.turnLeft(state); break;
				case TURN_RIGHT: result = cave.turnRight(state); break;
			}
			return result;
		}
	}
}
