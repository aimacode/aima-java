package aima.core.api.search.adversarial;

import aima.core.api.agent.Action;

/**
 * Variant of the search interface. Since players can only control the next
 * move, method <code>execute</code> returns only one action, not a
 * sequence of actions.
 *
 * @author Ruediger Lunde
 */
public interface AdversarialSearch<S> {

    /**
     * Returns the action which appears to be the best at the given state.
     */
    Action execute(S state);
}
