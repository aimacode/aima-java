package aima.core.search.adversarial;

import aima.core.search.framework.Metrics;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 169.<br>
 * 
 * <pre>
 * <code>
 * function MINIMAX-DECISION(state) returns an action
 *   return argmax_[a in ACTIONS(s)] MIN-VALUE(RESULT(state, a))
 * 
 * function MAX-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a)))
 *   return v
 * 
 * function MIN-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *     v = infinity
 *     for each a in ACTIONS(state) do
 *       v  = MIN(v, MAX-VALUE(RESULT(s, a)))
 *   return v
 * </code>
 * </pre>
 * 
 * Figure 5.3 An algorithm for calculating minimax decisions. It returns the
 * action corresponding to the best possible move, that is, the move that leads
 * to the outcome with the best utility, under the assumption that the opponent
 * plays to minimize utility. The functions MAX-VALUE and MIN-VALUE go through
 * the whole game tree, all the way to the leaves, to determine the backed-up
 * value of a state. The notation argmax_[a in S] f(a) computes the element a of
 * set S that has the maximum value of f(a).
 * 
 * 
 * @author Subham Mishra
 * @author Ruediger Lunde
 * 
 * @param <S>
 *            Type which is used for states in the game.
 * @param <A>
 *            Type which is used for actions in the game.
 * @param <PLAYER>
 *            Type which is used for players in the game.
 */
public class MinimaxSearch<S, A, PLAYER> implements
		AdversarialSearch<S, A> {

	private Game<S, A, PLAYER> game;
	private int expandedNodes;

	/**
	 *  
	 *  Creates a new search object for a given game.
	 *  
	 **/
	public static <S, A, PLAYER> MinimaxSearch<S, A, PLAYER> createFor(
			Game<S, A, PLAYER> game) {
		return new MinimaxSearch<S, A, PLAYER>(game);
	}
/**
 * 
 * Used to initialize game object
 * 
 * @param game
 *  Game current state , allowed actions and Players list is passed
 *  
 */
	public MinimaxSearch(Game<S, A, PLAYER> game) {
		this.game = game;
	}

	@Override
	public A makeDecision(S state) {
		expandedNodes = 0;
		A result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		PLAYER player = game.getPlayer(state);
		for (A action : game.getActions(state)) {
			double value = minValue(game.getResult(state, action), player);
			if (value > resultValue) { 
				result = action;
				resultValue = value;
			}
		}
		return result;
	}
/**
 * 
 * 
 * @param state
 * @param player
 * @return
 */
	public double maxValue(S state, PLAYER player) { // returns an utility
															// value
		expandedNodes++;
		if (game.isTerminal(state))
			return game.getUtility(state, player); //if terminal state is reached get utility value for each player
		double value = Double.NEGATIVE_INFINITY;
		for (A action : game.getActions(state)) //scan through each action 
			value = Math.max(value,
					minValue(game.getResult(state, action), player)); //store 
		return value;
	}
	
/**
 * 
 * used to find action with minimum node value
 * 
 * @param state
 * 
 * @param player
 * 
 * @return
 *     an utility value
 */
	public double minValue(S state, PLAYER player) {  
															
		expandedNodes++;
		if (game.isTerminal(state)) //if terminal state is reached get utility value for each player
			return game.getUtility(state, player);
		double value = Double.POSITIVE_INFINITY;
		for (A action : game.getActions(state))
			value = Math.min(value,
					maxValue(game.getResult(state, action), player));
		return value;
	}

	@Override
	public Metrics getMetrics() {
		Metrics result = new Metrics();
		result.set("expandedNodes", expandedNodes);
		return result;
	}
}
