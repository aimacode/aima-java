package aima.core.api.search.adversarial;

/**
 * Variant of the search interface. Since players can only control the next
 * move, method <code>execute</code> returns only one action, not a
 * sequence of actions.
 *
 * @author Ruediger Lunde
 * @author Minh Tuan Tran
 */
public interface AdversarialSearch<A,S> {

    /**
     * Returns the action which appears to be the best at the given s.
     */
    A execute(S state);
}
