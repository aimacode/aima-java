package aima.core.search.api;

/**
 * A description of what each action does; the formal name for this is the
 * <b>transition model, specified by a function RESULT(s, a) that returns the
 * state that results from doing action a in state s.</b>
 * 
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the states that the agent encounters.
 * 
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface ResultFunction<A, S> {
	/**
	 * Represents the Transition Model.
	 *
	 * @param s
	 *            a state
	 * @param a
	 *            an action performed in state s
	 * @return the state that results from doing action a in state s.
	 */
	S result(S s, A a);
}
