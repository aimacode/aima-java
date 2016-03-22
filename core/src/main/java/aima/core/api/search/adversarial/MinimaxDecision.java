package aima.core.api.search.adversarial;

import aima.core.api.agent.Action;

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
 * the whole problem tree, all the way to the leaves, to determine the backed-up
 * value of a state. The notation argmax_[a in S] f(a) computes the element a of
 * set S that has the maximum value of f(a).
 *
 *
 * @author Ruediger Lunde
 * @author Minh Tuan Tran
 *
 * @param <S>
 *            Type which is used for states in the problem.
 */
public interface MinimaxDecision<S> extends AdversarialSearch<S> {

    /**
     * function MINIMAX-DECISION(state) returns an action
     *   return argmax_[a in ACTIONS(s)] MIN-VALUE(RESULT(state, a))
     * @param state
     * @return an action
     */
    @Override
    default Action execute(S state) {
        Action action = null;
        double value = Double.NEGATIVE_INFINITY;
        for (Action a : problem().actions(state)) {
            double v = minValue(problem().result(state, a));
            if (value < v) {
                action = a;
                value = v;
            }
        }
        return action;
    }

    /**
     * function MAX-VALUE(state) returns a utility value
     *   if TERMINAL-TEST(state) then return UTILITY(state)
     *   v = -infinity
     *   for each a in ACTIONS(state) do
     *     v = MAX(v, MIN-VALUE(RESULT(s, a)))
     *   return v
     * @param state
     * @return a utilityy value
     */
    default double maxValue(S state) {
        if (problem().terminalTest(state))
            return problem().utility(state);
        double value = Double.NEGATIVE_INFINITY;
        for (Action a : problem().actions(state)) {
            double v = minValue(problem().result(state, a));
            if (value > v)
                value = v;
        }
        return value;
    }

    /**
     * function MIN-VALUE(state) returns a utility value
     *   if TERMINAL-TEST(state) then return UTILITY(state)
     *     v = infinity
     *     for each a in ACTIONS(state) do
     *       v  = MIN(v, MAX-VALUE(RESULT(s, a)))
     *   return v
     * @param state
     * @return a utility value
     */
    default double minValue(S state) {
        if (problem().terminalTest(state))
            return problem().utility(state);
        double value = Double.POSITIVE_INFINITY;
        for (Action a : problem().actions(state)) {
            double v = maxValue(problem().result(state, a));
            if (value > v)
                value = v;
        }
        return value;
    }

    Problem<S> problem();
}
