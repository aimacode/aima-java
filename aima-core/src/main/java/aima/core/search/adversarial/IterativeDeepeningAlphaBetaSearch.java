package aima.core.search.adversarial;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.framework.Metrics;

/**
 * Implements an iterative deepening Minimax search with alpha-beta pruning and
 * action ordering. Maximal computation time is specified in seconds. The
 * algorithm is implemented as template method and can be configured and tuned
 * by subclassing.
 * 
 * @author Ruediger Lunde
 * 
 * @param <STATE>
 *            Type which is used for states in the game.
 * @param <ACTION>
 *            Type which is used for actions in the game.
 * @param <PLAYER>
 *            Type which is used for players in the game.
 */
public class IterativeDeepeningAlphaBetaSearch<STATE, ACTION, PLAYER> implements AdversarialSearch<STATE, ACTION> {

	public final static String METRICS_NODES_EXPANDED = "nodesExpanded";
	public final static String METRICS_MAX_DEPTH = "maxDepth";

	protected Game<STATE, ACTION, PLAYER> game;
	protected double utilMax;
	protected double utilMin;
	protected int currDepthLimit;
	private boolean heuristicEvaluationUsed; // indicates that non-terminal
												// nodes
												// have been evaluated.
	private Timer timer;
	private boolean logEnabled;

	private Metrics metrics = new Metrics();

	/**
	 * Creates a new search object for a given game.
	 * 
	 * @param game
	 *            The game.
	 * @param utilMin
	 *            Utility value of worst state for this player. Supports
	 *            evaluation of non-terminal states and early termination in
	 *            situations with a safe winner.
	 * @param utilMax
	 *            Utility value of best state for this player. Supports
	 *            evaluation of non-terminal states and early termination in
	 *            situations with a safe winner.
	 * @param time
	 *            Maximal computation time in seconds.
	 */
	public static <STATE, ACTION, PLAYER> IterativeDeepeningAlphaBetaSearch<STATE, ACTION, PLAYER> createFor(
			Game<STATE, ACTION, PLAYER> game, double utilMin, double utilMax, int time) {
		return new IterativeDeepeningAlphaBetaSearch<STATE, ACTION, PLAYER>(game, utilMin, utilMax, time);
	}

	/**
	 * Creates a new search object for a given game.
	 * 
	 * @param game
	 *            The game.
	 * @param utilMin
	 *            Utility value of worst state for this player. Supports
	 *            evaluation of non-terminal states and early termination in
	 *            situations with a safe winner.
	 * @param utilMax
	 *            Utility value of best state for this player. Supports
	 *            evaluation of non-terminal states and early termination in
	 *            situations with a safe winner.
	 * @param time
	 *            Maximal computation time in seconds.
	 */
	public IterativeDeepeningAlphaBetaSearch(Game<STATE, ACTION, PLAYER> game, double utilMin, double utilMax,
			int time) {
		this.game = game;
		this.utilMin = utilMin;
		this.utilMax = utilMax;
		this.timer = new Timer(time);
	}

	public void setLogEnabled(boolean b) {
		logEnabled = b;
	}

	/**
	 * Template method controlling the search. It is based on iterative
	 * deepening and tries to make to a good decision in limited time. Credit
	 * goes to Behi Monsio who had the idea of ordering actions by utility in
	 * subsequent depth-limited search runs.
	 */
	@Override
	public ACTION makeDecision(STATE state) {
		metrics = new Metrics();
		StringBuffer logText = null;
		PLAYER player = game.getPlayer(state);
		List<ACTION> results = orderActions(state, game.getActions(state), player, 0);
		timer.start();
		currDepthLimit = 0;
		do {
			incrementDepthLimit();
			if (logEnabled)
				logText = new StringBuffer("depth " + currDepthLimit + ": ");
			heuristicEvaluationUsed = false;
			ActionStore<ACTION> newResults = new ActionStore<ACTION>();
			for (ACTION action : results) {
				double value = minValue(game.getResult(state, action), player, Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY, 1);
				if (timer.timeOutOccured())
					break; // exit from action loop
				newResults.add(action, value);
				if (logEnabled)
					logText.append(action + "->" + value + " ");
			}
			if (logEnabled)
				System.out.println(logText);
			if (newResults.size() > 0) {
				results = newResults.actions;
				if (!timer.timeOutOccured()) {
					if (hasSafeWinner(newResults.utilValues.get(0)))
						break; // exit from iterative deepening loop
					else if (newResults.size() > 1
							&& isSignificantlyBetter(newResults.utilValues.get(0), newResults.utilValues.get(1)))
						break; // exit from iterative deepening loop
				}
			}
		} while (!timer.timeOutOccured() && heuristicEvaluationUsed);
		return results.get(0);
	}

	// returns an utility value
	public double maxValue(STATE state, PLAYER player, double alpha, double beta, int depth) {
		updateMetrics(depth);
		if (game.isTerminal(state) || depth >= currDepthLimit || timer.timeOutOccured()) {
			return eval(state, player);
		} else {
			double value = Double.NEGATIVE_INFINITY;
			for (ACTION action : orderActions(state, game.getActions(state), player, depth)) {
				value = Math.max(value, minValue(game.getResult(state, action), //
						player, alpha, beta, depth + 1));
				if (value >= beta)
					return value;
				alpha = Math.max(alpha, value);
			}
			return value;
		}
	}

	// returns an utility value
	public double minValue(STATE state, PLAYER player, double alpha, double beta, int depth) {
		updateMetrics(depth);
		if (game.isTerminal(state) || depth >= currDepthLimit || timer.timeOutOccured()) {
			return eval(state, player);
		} else {
			double value = Double.POSITIVE_INFINITY;
			for (ACTION action : orderActions(state, game.getActions(state), player, depth)) {
				value = Math.min(value, maxValue(game.getResult(state, action), //
						player, alpha, beta, depth + 1));
				if (value <= alpha)
					return value;
				beta = Math.min(beta, value);
			}
			return value;
		}
	}

	private void updateMetrics(int depth) {
		metrics.incrementInt(METRICS_NODES_EXPANDED);
		metrics.set(METRICS_MAX_DEPTH, Math.max(metrics.getInt(METRICS_MAX_DEPTH), depth));
	}

	/** Returns some statistic data from the last search. */
	@Override
	public Metrics getMetrics() {
		return metrics;
	}

	/**
	 * Primitive operation which is called at the beginning of one depth limited
	 * search step. This implementation increments the current depth limit by
	 * one.
	 */
	protected void incrementDepthLimit() {
		currDepthLimit++;
	}

	/**
	 * Primitive operation which is used to stop iterative deepening search in
	 * situations where a clear best action exists. This implementation returns
	 * always false.
	 */
	protected boolean isSignificantlyBetter(double newUtility, double utility) {
		return false;
	}

	/**
	 * Primitive operation which is used to stop iterative deepening search in
	 * situations where a safe winner has been identified. This implementation
	 * returns true if the given value (for the currently preferred action
	 * result) is the highest or lowest utility value possible.
	 */
	protected boolean hasSafeWinner(double resultUtility) {
		return resultUtility <= utilMin || resultUtility >= utilMax;
	}

	/**
	 * Primitive operation, which estimates the value for (not necessarily
	 * terminal) states. This implementation returns the utility value for
	 * terminal states and <code>(utilMin + utilMax) / 2</code> for non-terminal
	 * states. When overriding, first call the super implementation!
	 */
	protected double eval(STATE state, PLAYER player) {
		if (game.isTerminal(state)) {
			return game.getUtility(state, player);
		} else {
			heuristicEvaluationUsed = true;
			return (utilMin + utilMax) / 2;
		}
	}

	/**
	 * Primitive operation for action ordering. This implementation preserves
	 * the original order (provided by the game).
	 */
	public List<ACTION> orderActions(STATE state, List<ACTION> actions, PLAYER player, int depth) {
		return actions;
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// nested helper classes

	private static class Timer {
		private long duration;
		private long startTime;

		Timer(int maxSeconds) {
			this.duration = 1000l * maxSeconds;
		}

		void start() {
			startTime = System.currentTimeMillis();
		}

		boolean timeOutOccured() {
			return System.currentTimeMillis() > startTime + duration;
		}
	}

	/** Orders actions by utility. */
	private static class ActionStore<ACTION> {
		private List<ACTION> actions = new ArrayList<ACTION>();
		private List<Double> utilValues = new ArrayList<Double>();

		void add(ACTION action, double utilValue) {
			int idx;
			for (idx = 0; idx < actions.size() && utilValue <= utilValues.get(idx); idx++)
				;
			actions.add(idx, action);
			utilValues.add(idx, utilValue);
		}

		int size() {
			return actions.size();
		}
	}
}
