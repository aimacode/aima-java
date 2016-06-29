package aima.core.search.api;

import java.util.List;

/**
 * A description of the possible <b>actions</b> available to the agent. Given a
 * particular state s, ACTIONS(s) returns the set of actions that can be
 * executed in s.
 *
 * @param <A>
 *            the type of the action(s) that can be performed.
 * @param <S>
 *            the type of the states that the agent encounters.
 * 
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface ActionsFunction<A, S> {
	/**
	 * A description of the possible actions available to the agent from a give
	 * state.
	 *
	 * @param s
	 *            a given state s
	 * @return the set of actions that can be executed in s (returned in a list
	 *         for access convenience, implementation must guarantee Set
	 *         semantics).
	 */
	List<A> actions(S s);
}
