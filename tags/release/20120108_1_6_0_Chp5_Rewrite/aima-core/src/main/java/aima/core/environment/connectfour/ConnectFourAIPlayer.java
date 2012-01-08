package aima.core.environment.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;

/**
 * Implements an iterative deepening Minimax search with alpha-beta pruning and
 * a special action ordering optimized for the Connect Four game.
 * 
 * @author Ruediger Lunde
 */
public class ConnectFourAIPlayer extends
		IterativeDeepeningAlphaBetaSearch<ConnectFourState, Integer, String> {

	public ConnectFourAIPlayer(Game<ConnectFourState, Integer, String> game,
			int time) {
		super(game, 0.0, 1.0, time);
	}

	@Override
	protected boolean isSignificantlyBetter(double newUtility, double utility) {
		return newUtility - utility > (utilMax - utilMin) * 0.4;
	}

	@Override
	protected boolean hasSafeWinner(double resultUtility) {
		return Math.abs(resultUtility - (utilMin + utilMax) / 2) > 0.4
				* utilMax - utilMin;
	}

	/**
	 * Modifies the super implementation by making safe winner values even more
	 * attractive if depth is small.
	 */
	@Override
	protected double eval(ConnectFourState state, String player) {
		double value = super.eval(state, player);
		if (hasSafeWinner(value)) {
			if (value > (utilMin + utilMax) / 2)
				value -= state.getMoves() / 1000.0;
			else
				value += state.getMoves() / 1000.0;
		}
		return value;
	}

	/**
	 * Orders actions with respect to the number of potential win positions
	 * which profit from the action.
	 */
	@Override
	public List<Integer> orderActions(ConnectFourState state,
			List<Integer> actions, String player, int depth) {
		List<Integer> result = actions;
		if (depth == 0) {
			List<ActionValuePair<Integer>> actionEstimates = new ArrayList<ActionValuePair<Integer>>(
					actions.size());
			for (Integer action : actions)
				actionEstimates.add(ActionValuePair.createFor(action,
						state.analyzePotentialWinPositions(action)));
			Collections.sort(actionEstimates);
			result = new ArrayList<Integer>();
			for (ActionValuePair<Integer> pair : actionEstimates)
				result.add(pair.getAction());
		}
		return result;
	}
}
