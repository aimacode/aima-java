package aima.core.search.basic.adversarial;

import java.util.List;

import aima.core.search.api.Game;
import aima.core.search.api.SearchForAdversarialActionFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * 
 * <pre>
 * <code>
 * function ALPHA-BETA-SEARCH(state) returns an action
 *   v &larr; MAX-VALUE(state, &minus;&infin;, &plus;&infin;)
 *   return the action in ACTIONS(state) with value v
 * 
 * function MAX-VALUE(state, &alpha;, &beta;) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v &larr; &minus;&infin;
 *   for each a in ACTIONS(state) do
 *     v &larr; MAX(v, MIN-VALUE(RESULT(state, a), &alpha;, &beta;))
 *     if v &ge; &beta; then return v
 *     &alpha; &larr; MAX(&alpha;, v)
 *   return v
 * 
 * function MIN-VALUE(state, &alpha;, &beta;) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *     v &larr; &plus;&infin;
 *     for each a in ACTIONS(state) do
 *       v &larr; MIN(v, MAX-VALUE(RESULT(state, a), &alpha;, &beta;))
 *       if v &le; &alpha; then return v
 *       &beta; &larr; MIN(&beta;, v)
 *   return v
 * </code>
 * </pre>
 * 
 * Figure ?.? The alpha-beta search algorithm. Notice these routines are the
 * same as the MINIMAX functions in Figure ?.?, except for the lines in each of
 * MIN-VALUE and MAX-VALUE that maintain &alpha; and &beta; (and the bookkeeping
 * to pass these parameters along).
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
public class AlphaBetaSearch<S, A, P> implements SearchForAdversarialActionFunction<S, A> {

	// function ALPHA-BETA-SEARCH(state) returns an action
	public A alphaBetaSearch(S state) {
		// v <- MAX-VALUE(state, &minus;&infin;, &plus;&infin;)
		// return the action in ACTIONS(state) with value v
		A actionWithMaxValue = null;
		double alpha = Double.NEGATIVE_INFINITY;
		for (A a : actions(state)) {
			double v = minValue(result(state, a), alpha, Double.POSITIVE_INFINITY);
			if (v > alpha) {
				alpha = v;
				actionWithMaxValue = a;
			}
		}
		return actionWithMaxValue;
	}

	// function MAX-VALUE(state, &alpha;, &beta;) returns a utility value
	public double maxValue(S state, double alpha, double beta) {
		// if TERMINAL-TEST(state) then return UTILITY(state)
		if (terminalTest(state)) {
			return utility(state);
		}
		// v <- -&infin;
		double v = Double.NEGATIVE_INFINITY;
		// for each a in ACTIONS(state) do
		for (A a : actions(state)) {
			// v <- MAX(v, MIN-VALUE(RESULT(state, a)))
			v = Math.max(v, minValue(result(state, a), alpha, beta));
			// if v >= &beta; then return v
			if (v >= beta) {
				return v;
			}
			// &alpha; <- MAX(&alpha;, v)
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	// function MIN-VALUE(state, &alpha;, &beta;) returns a utility value
	public double minValue(S state, double alpha, double beta) {
		// if TERMINAL-TEST(state) then return UTILITY(state)
		if (terminalTest(state)) {
			return utility(state);
		}
		// v <- +&infin;
		double v = Double.POSITIVE_INFINITY;
		// for each a in ACTIONS(state) do
		for (A a : actions(state)) {
			// v <- MIN(v, MAX-VALUE(RESULT(state, a)))
			v = Math.min(v, maxValue(result(state, a), alpha, beta));
			// if v <= &alpha; then return v
			if (v <= alpha) {
				return v;
			}
			// &beta; <- MIN(&beta;, v)
			beta = Math.min(beta, v);
		}
		return v;
	}

	//
	// Supporting Code
	protected Game<S, A, P> game;

	public AlphaBetaSearch(Game<S, A, P> game) {
		this.game = game;
	}

	@Override
	public A apply(S state) {
		return alphaBetaSearch(state);
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