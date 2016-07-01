package aima.core.search.api;

/**
 * A conditional plan.
 * 
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the state space
 *
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 * @author Anurag Rai
 */
public interface ConditionalPlan<A, S> {
	/**
	 * An iterator over the conditional plan.
	 * 
	 * @author Ciaran O'Reilly
	 *
	 * @param <A>
	 *            the type of the actions that can be performed.
	 * @param <S>
	 *            the type of the state space
	 */
	interface Interator<A, S> {
		/**
		 * Get the next action depending on the state given.
		 * 
		 * @param state
		 *            the next state encountered.
		 * @return the action to perform or null if no action to be performed (a
		 *         NoOp) based on the sequence of states received by the
		 *         iterator (NOTE: a null is expected when you reach the goal
		 *         state).
		 */
		A next(S state);
	}

	ConditionalPlan.Interator<A, S> iterator();
}
