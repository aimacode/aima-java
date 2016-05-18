package aima.core.api.search.adversarial;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
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
 * Figure ??  An algorithm for calculating minimax decisions. It returns the
 * action corresponding to the best possible move, that is, the move that leads
 * to the outcome with the best utility, under the assumption that the opponent
 * plays to minimize utility. The functions MAX-VALUE and MIN-VALUE go through
 * the whole problem tree, all the way to the leaves, to determine the backed-up
 * value of a state. The notation argmax_[a in S] f(a) computes the element a of
 * set S that has the maximum value of f(a).<br>
 *
 * @param <S> the type of state representation used in the problem.
 * @param <A> the type of action carried out.
 *
 * @author Ruediger Lunde
 * @author Minh Tuan Tran
 */
public interface MinimaxDecision<A,S> extends AdversarialSearch<A,S> {

    /**
     * function MINIMAX-DECISION(s) returns an action
     *   return argmax_[a in ACTIONS(s)] MIN-VALUE(RESULT(s, a))
     * @param state the current state of the problem
     * @return an action
     */
    @Override
    default A execute(S state) {
        A action = null;
        double value = Double.NEGATIVE_INFINITY;
        for (A a : problem().actions(state)) {
            double v = minValue(problem().result(state, a));
            if (value < v) {
                action = a;
                value = v;
            }
        }
        return action;
    }

    /**
     * function MAX-VALUE(s) returns a utility value
     *   if TERMINAL-TEST(s) then return UTILITY(s)
     *   v = -infinity
     *   for each a in ACTIONS(s) do
     *     v = MAX(v, MIN-VALUE(RESULT(s, a)))
     *   return v
     * @param state the current state of the problem
     * @return a utility value
     */
    default double maxValue(S state) {
        if (problem().terminalTest(state))
            return problem().utility(state);
        double value = Double.NEGATIVE_INFINITY;
        for (A a : problem().actions(state)) {
            double v = minValue(problem().result(state, a));
            if (value > v)
                value = v;
        }
        return value;
    }

    /**
     * function MIN-VALUE(s) returns a utility value
     *   if TERMINAL-TEST(s) then return UTILITY(s)
     *     v = infinity
     *     for each a in ACTIONS(s) do
     *       v  = MIN(v, MAX-VALUE(RESULT(s, a)))
     *   return v
     * @param state the current state of the problem
     * @return a utility value
     */
    default double minValue(S state) {
        if (problem().terminalTest(state))
            return problem().utility(state);
        double value = Double.POSITIVE_INFINITY;
        for (A a : problem().actions(state)) {
            double v = maxValue(problem().result(state, a));
            if (value > v)
                value = v;
        }
        return value;
    }

    Problem<A, S> problem();
}
