package aima.core.search.basic.adversarial;

import java.util.Comparator;
import java.util.List;

import aima.core.search.api.Game;
import aima.core.search.api.SearchForAdversarialActionFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * 
 * <pre>
 * <code>
 * function MINIMAX-DECISION(state) returns an action
 *   return argmax<sub>a &isin; ACTIONS(state)</sub> MIN-VALUE(RESULT(state, a))
 * 
 * function MAX-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v &larr; -&infin;
 *   for each a in ACTIONS(state) do
 *     v &larr; MAX(v, MIN-VALUE(RESULT(state, a)))
 *   return v
 * 
 * function MIN-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *     v &larr; &infin;
 *     for each a in ACTIONS(state) do
 *       v &larr; MIN(v, MAX-VALUE(RESULT(state, a)))
 *   return v
 * </code>
 * </pre>
 * 
 * Figure ?.? An algorithm for calculating minimax decisions. It returns the
 * action corresponding to the best possible move, that is, the move that leads
 * to the outcome with the best utility, under the assumption that the opponent
 * plays to minimize utility. The functions MAX-VALUE and MIN-VALUE go through
 * the whole game tree, all the way to the leaves, to determine the backed-up
 * value of a state. The notation argmax_[a in S] f(a) computes the element a of
 * set S that has the maximum value of f(a).
 * 
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 * @param <S>
 *            Type which is used for states in the game.
 * @param <A>
 *            Type which is used for actions in the game.
 * @param <P>
 *            Type which is used for players in the game.
 */
public class MinimaxDecision<S, A, P> implements SearchForAdversarialActionFunction<S, A> {

	// function MINIMAX-DECISION(state) returns an action
	public A minimaxDecision(S state) {
		// return argmax_[a &isin; ACTIONS(state)] MIN-VALUE(RESULT(state, a))
		return actions(state).stream().max(Comparator.comparingDouble(a -> minValue(result(state, a)))).get();
	}

	// function MAX-VALUE(state) returns a utility value
	public double maxValue(S state) {
		// if TERMINAL-TEST(state) then return UTILITY(state)
		if (terminalTest(state)) {
			return utility(state);
		}
		// v <- -&infin;
		double v = Double.NEGATIVE_INFINITY;
		// for each a in ACTIONS(state) do
		for (A a : actions(state)) {
			// v <- MAX(v, MIN-VALUE(RESULT(state, a)))
			v = Math.max(v, minValue(result(state, a)));
		}
		return v;
	}

	// function MIN-VALUE(state) returns a utility value
	public double minValue(S state) {
		// if TERMINAL-TEST(state) then return UTILITY(state)
		if (terminalTest(state)) {
			return utility(state);
		}
		// v <- &infin;
		double v = Double.POSITIVE_INFINITY;
		// for each a in ACTIONS(state) do
		for (A a : actions(state)) {
			// v <- MIN(v, MAX-VALUE(RESULT(state, a)))
			v = Math.min(v, maxValue(result(state, a)));
		}
		return v;
	}

	//
	// Supporting Code
	protected Game<S, A, P> game;

	public MinimaxDecision(Game<S, A, P> game) {
		this.game = game;
	}

	@Override
	public A apply(S state) {
		return minimaxDecision(state);
	}

	public List<A> actions(S state) {
		return game.actions(state);
	}

	public S result(S s, A a) {
		return game.result(s, a);
	}
	
	public boolean terminalTest(S s) {
		return game.isTerminalState(s);
	}

	public double utility(S state) {
		return game.utility(state, game.player(state));
	}
}