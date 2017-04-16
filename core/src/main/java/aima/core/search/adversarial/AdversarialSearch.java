package aima.core.search.adversarial;

import aima.core.search.framework.Metrics;

/**
 * Variant of the search interface. Since players can only control the next
 * move, method <code>makeDecision</code> returns only one action, not a
 * sequence of actions.
 * 
 * @author Subham Mishra
 * @author Ruediger Lunde
 */
public interface AdversarialSearch<State, Action> {

	/** Returns the action which appears to be the best at the given state. 
	 * 
	 * @param state
	 *    Current state
	 * @return
	 *    best action for current state
	 */
	Action makeDecision(State state);

	/**
	 * Returns all the metrics of the search.
	 * 
	 * @return all the metrics of the search.
	 */
	Metrics getMetrics();
}
