package aima.core.search.adversarial;

import aima.core.search.framework.Metrics;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Page 173.<br>
 * <p>
 * <pre>
 * <code>
 * function ALPHA-BETA-SEARCH(state) returns an action
 *   v = MAX-VALUE(state, -infinity, +infinity)
 *   return the action in ACTIONS(state) with value v
 *
 * function MAX-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a), alpha, beta))
 *     if v >= beta then return v
 *     alpha = MAX(alpha, v)
 *   return v
 *
 * function MIN-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = infinity
 *   for each a in ACTIONS(state) do
 *     v = MIN(v, MAX-VALUE(RESULT(s,a), alpha, beta))
 *     if v <= alpha then return v
 *     beta = MIN(beta, v)
 *   return v
 * </code>
 * </pre>
 * <p>
 * Figure 5.7 The alpha-beta search algorithm. Notice that these routines are
 * the same as the MINIMAX functions in Figure 5.3, except for the two lines in
 * each of MIN-VALUE and MAX-VALUE that maintain alpha and beta (and the
 * bookkeeping to pass these parameters along).
 *
 * @param <S> Type which is used for states in the game.
 * @param <A> Type which is used for actions in the game.
 * @param <P> Type which is used for players in the game.
 * @author Ruediger Lunde
 */
public class AlphaBetaSearch<S, A, P> implements AdversarialSearch<S, A> {

    public final static String METRICS_NODES_EXPANDED = "nodesExpanded";

    Game<S, A, P> game;
    private Metrics metrics = new Metrics();

    /**
     * Creates a new search object for a given game.
     */
    public static <STATE, ACTION, PLAYER> AlphaBetaSearch<STATE, ACTION, PLAYER> createFor(
            Game<STATE, ACTION, PLAYER> game) {
        return new AlphaBetaSearch<STATE, ACTION, PLAYER>(game);
    }

    public AlphaBetaSearch(Game<S, A, P> game) {
        this.game = game;
    }

    @Override
    public A makeDecision(S state) {
        metrics = new Metrics();
        A result = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        P player = game.getPlayer(state);
        for (A action : game.getActions(state)) {
            double value = minValue(game.getResult(state, action), player,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
    }

    public double maxValue(S state, P player, double alpha, double beta) {
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (game.isTerminal(state))
            return game.getUtility(state, player);
        double value = Double.NEGATIVE_INFINITY;
        for (A action : game.getActions(state)) {
            value = Math.max(value, minValue( //
                    game.getResult(state, action), player, alpha, beta));
            if (value >= beta)
                return value;
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    public double minValue(S state, P player, double alpha, double beta) {
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (game.isTerminal(state))
            return game.getUtility(state, player);
        double value = Double.POSITIVE_INFINITY;
        for (A action : game.getActions(state)) {
            value = Math.min(value, maxValue( //
                    game.getResult(state, action), player, alpha, beta));
            if (value <= alpha)
                return value;
            beta = Math.min(beta, value);
        }
        return value;
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }
}
