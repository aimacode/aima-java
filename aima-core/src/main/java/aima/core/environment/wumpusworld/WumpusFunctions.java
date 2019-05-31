package aima.core.environment.wumpusworld;

import aima.core.search.framework.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Factory class for constructing functions for use in the Wumpus World environment.
 *
 * @author Ruediger Lunde
 */
public class WumpusFunctions {

	public static Function<AgentPosition, List<WumpusAction>> createActionsFunction(WumpusCave cave) {
		return state -> {
			List<WumpusAction> actions = new ArrayList<>();
			AgentPosition pos = cave.moveForward(state);
			if (!pos.equals(state))
				actions.add(WumpusAction.FORWARD);
			actions.add(WumpusAction.TURN_LEFT);
			actions.add(WumpusAction.TURN_RIGHT);
			return actions;
		};
	}

	public static BiFunction<AgentPosition, WumpusAction, AgentPosition> createResultFunction(WumpusCave cave) {
		return (state, action) -> {
			AgentPosition result = state;
			switch (action) {
				case FORWARD:
					result = cave.moveForward(state);
					break;
				case TURN_LEFT:
					result = cave.turnLeft(state);
					break;
				case TURN_RIGHT:
					result = cave.turnRight(state);
					break;
			}
			return result;
		};
	}

	public static ToDoubleFunction<Node<AgentPosition, WumpusAction>> createManhattanDistanceFunction
			(Set<AgentPosition> goals) {
		return node -> {
			AgentPosition curr = node.getState();
			return goals.stream().
					mapToInt(goal -> Math.abs(goal.getX() - curr.getX()) + Math.abs(goal.getY() - curr.getY())).min().
					orElse(Integer.MAX_VALUE);
		};
	}
}
