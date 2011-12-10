package aima.core.search.adversarial;

import aima.core.search.framework.Metrics;

/**
 * Implements a Minimax search with alpha-beta pruning.
 * @author Ruediger Lunde
 *
 * @param <STATE> Type which is used for states in the game.
 * @param <ACTION> Type which is used for actions in the game.
 * @param <PLAYER> Type which is used for players in the game.
 */
public class AlphaBetaSearch<STATE, ACTION, PLAYER> implements AdversarialSearch<STATE, ACTION> {

	Game<STATE, ACTION, PLAYER> game;
	private int expandedNodes;
	
	/** Creates a new search object for a given game. */
	public static <STATE, ACTION, PLAYER> AlphaBetaSearch<STATE, ACTION, PLAYER> createFor(
			Game<STATE, ACTION, PLAYER> game) {
		return new AlphaBetaSearch<STATE, ACTION, PLAYER>(game);
	}
	
	public AlphaBetaSearch(Game<STATE, ACTION, PLAYER> game) {
		this.game = game;
	}

	public ACTION makeDecision(STATE state) {
		expandedNodes = 0;
		ACTION result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		PLAYER player = game.getPlayer(state);
		for (ACTION action : game.getActions(state)) {
			double value = minValue(game.getResult(state, action), player,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (value > resultValue) {
				result = action;
				resultValue = value;
			}
		}
		return result;
	}

	public double maxValue(STATE state, PLAYER player, double alpha,
			double beta) { // returns an utility value
		expandedNodes++;
		if (game.isTerminal(state))
			return game.getUtility(state, player);
		double value = Double.NEGATIVE_INFINITY;
		for (ACTION action : game.getActions(state)) {
			value = Math
					.max(value,
							minValue(game.getResult(state, action), player,
									alpha, beta));
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(STATE state, PLAYER player, double alpha,
			double beta) { // returns an utility
		expandedNodes++;
		if (game.isTerminal(state))
			return game.getUtility(state, player);
		double value = Double.POSITIVE_INFINITY;
		for (ACTION action : game.getActions(state)) {
			value = Math
					.min(value,
							maxValue(game.getResult(state, action), player,
									alpha, beta));
			if (value <= alpha)
				return value;
			beta = Math.min(beta, value);
		}
		return value;
	}
	
	@Override
	public Metrics getMetrics() {
		Metrics result = new Metrics();
		result.set("expandedNodes", expandedNodes);
		return result;
	}
}
