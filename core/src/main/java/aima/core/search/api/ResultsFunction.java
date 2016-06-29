package aima.core.search.api;

import java.util.List;

/**
 * A generalization of the basic transition model RESULT(s, a) which returns a
 * single state, RESULTS(s, a) returns a set of possible outcome states (i.e.
 * nondeterministic action outcomes).
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
public interface ResultsFunction<A, S> {
	/**
	 * Represents the Transition Model.
	 *
	 * @param s
	 *            a state
	 * @param a
	 *            an action performed in state s
	 * @return the set of states that can results from doing action a in state s
	 *         (returned in a list for access convenience, implementation must
	 *         guarantee Set semantics).
	 */
	List<S> results(S s, A a);
}
