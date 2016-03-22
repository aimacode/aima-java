package aima.core.api.search.adversarial;

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
 * @author Ruediger Lunde
 * @author Minh Tuan Tran
 *
 * @param <STATE>
 *            Type which is used for states in the game.
 * @param <ACTION>
 *            Type which is used for actions in the game.
 * @param <PLAYER>
 *            Type which is used for players in the game.
 */
public interface MinimaxSearch<STATE, ACTION, PLAYER> extends AdversarialSearch<STATE, ACTION> {

    @Override
    default ACTION makeDecision(STATE state) {
        ACTION action = null;
        double value = Double.NEGATIVE_INFINITY;
        PLAYER player = game().getPlayer(state);
        for (ACTION a : game().getActions(state)) {
            double v = minValue(game().getResult(state, a), player);
            if (v > value) {
                action = a;
                value = v;
            }
        }
        return action;
    }

    default double minValue(STATE state, PLAYER player) {
        if (game().isTerminal(state))
            return game().getUtility(state, player);
        double value = Double.POSITIVE_INFINITY;
        for (ACTION a : game().getActions(state))
            value = Math.min(value, maxValue(game().getResult(state, a), player));
        return value;

    }

    default double maxValue(STATE state, PLAYER player) {
        if (game().isTerminal(state))
            return game().getUtility(state, player);
        double value = Double.NEGATIVE_INFINITY;
        for (ACTION a : game().getActions(state))
            value = Math.max(value, minValue(game().getResult(state, a), player));
        return value;
    }

    Game<STATE, ACTION, PLAYER> game();
}
