package aima.core.environment.wumpusworld;

import aima.core.agent.Action;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.TurnRight;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory class for constructing functions for use in the Wumpus World environment.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class WumpusFunctionFunctions {

	public static ActionsFunction<AgentPosition, Action> createActionsFunction(WumpusCave cave) {
		return new WumpusActionsFunction(cave);
	}

	public static ResultFunction<AgentPosition, Action> createResultFunction() {
		return new WumpusResultFunction();
	}

	private static class WumpusActionsFunction implements ActionsFunction<AgentPosition, Action> {
		private WumpusCave cave;

		private WumpusActionsFunction(WumpusCave cave) {
			this.cave = cave;
		}

		@Override
		public List<Action> apply(AgentPosition state) {
			List<Action> actions = new ArrayList<>();
			
			List<AgentPosition> linkedPositions = cave.getLocationsLinkedTo(state);
			actions.addAll(linkedPositions.stream().filter
					(linkPos -> linkPos.getX() != state.getX() || linkPos.getY() != state.getY()).map
					(linkPos -> new Forward(state)).collect(Collectors.toList()));
			actions.add(new TurnLeft(state.getOrientation()));
			actions.add(new TurnRight(state.getOrientation()));

			return actions;
		}
	}

	private static class WumpusResultFunction implements ResultFunction<AgentPosition, Action> {

		@Override
		public AgentPosition apply(AgentPosition state, Action action) {

			if (action instanceof Forward) {
				Forward fa = (Forward) action;
				
				return fa.getToPosition();
			}
			else if (action instanceof TurnLeft) {
				TurnLeft tLeft = (TurnLeft) action;
				return new AgentPosition(state.getX(), state.getY(), tLeft.getToOrientation());
			}
			else if (action instanceof TurnRight) {
				TurnRight tRight = (TurnRight) action;
				return new AgentPosition(state.getX(), state.getY(), tRight.getToOrientation());
			}
			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return state;
		}
	}
}
