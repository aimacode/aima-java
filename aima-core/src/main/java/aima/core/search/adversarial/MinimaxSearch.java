package aima.core.search.adversarial;

import aima.core.search.framework.Metrics;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 169.<br>
 * <p>
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
 * <p>
 * Figure 5.3 An algorithm for calculating minimax decisions. It returns the
 * action corresponding to the best possible move, that is, the move that leads
 * to the outcome with the best utility, under the assumption that the opponent
 * plays to minimize utility. The functions MAX-VALUE and MIN-VALUE go through
 * the whole game tree, all the way to the leaves, to determine the backed-up
 * value of a state. The notation argmax_[a in S] f(a) computes the element a of
 * set S that has the maximum value of f(a).
 *
 * @param <S> Type which is used for states in the game.
 * @param <A> Type which is used for actions in the game.
 * @param <P> Type which is used for players in the game.
 * @author Ruediger Lunde
 */
public class MinimaxSearch<S, A, P> implements AdversarialSearch<S, A> {

    public final static String METRICS_NODES_EXPANDED = "nodesExpanded";

    private Game<S, A, P> game;
    private Metrics metrics = new Metrics();

    /**
     * Creates a new search object for a given game.
     */
    public static <S, A, P> MinimaxSearch<S, A, P> createFor(Game<S, A, P> game) {
        return new MinimaxSearch<>(game);
    }

    public MinimaxSearch(Game<S, A, P> game) {
        this.game = game;
    }

    @Override
    public A makeDecision(S state) {
        metrics = new Metrics();
        A result = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        P player = game.getPlayer(state);
        for (A action : game.getActions(state)) {
            double value = minValue(game.getResult(state, action), player);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
    }

    public double maxValue(S state, P player) { // returns an utility
        // value
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (game.isTerminal(state))
            return game.getUtility(state, player);
        double value = Double.NEGATIVE_INFINITY;
        for (A action : game.getActions(state))
            value = Math.max(value,
                    minValue(game.getResult(state, action), player));
        return value;
    }

    public double minValue(S state, P player) { // returns an utility
        // value
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (game.isTerminal(state))
            return game.getUtility(state, player);
        double value = Double.POSITIVE_INFINITY;
        for (A action : game.getActions(state))
            value = Math.min(value,
                    maxValue(game.getResult(state, action), player));
        return value;
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }
}
