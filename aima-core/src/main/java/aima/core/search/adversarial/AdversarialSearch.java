package aima.core.search.adversarial;

import aima.core.search.framework.Metrics;

/**
 * Variant of the search interface. Since players can only control the next
 * move, method <code>makeDecision</code> returns only one action, not a
 * sequence of actions.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 * @author Ruediger Lunde
 */
public interface AdversarialSearch<S, A> {

    /**
     * Returns the action which appears to be the best at the given state.
     */
    A makeDecision(S state);

    /**
     * Returns all the metrics of the search.
     *
     * @return all the metrics of the search.
     */
    Metrics getMetrics();
}
