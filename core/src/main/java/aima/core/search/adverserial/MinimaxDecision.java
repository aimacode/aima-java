package aima.core.search.adverserial;

import aima.core.api.search.adversarial.Problem;

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
public class MinimaxDecision<A,S> extends AdversarialSearch<A,S>
        implements aima.core.api.search.adversarial.MinimaxDecision<A,S> {

    private Problem<A,S> problem;

    public MinimaxDecision(Problem<A,S> problem) {
        this.problem = problem;
    }

    @Override
    public Problem<A,S> problem() {
        return problem;
    }
}