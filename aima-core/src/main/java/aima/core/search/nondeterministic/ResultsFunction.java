package aima.core.search.nondeterministic;

import aima.core.agent.Action;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 136.<br>
 * <br>
 * Closely related to ResultFunction, but for non-deterministic problems; in
 * these problems, the outcome of an action will be a set of results, not a
 * single result. This class implements the functionality of RESULTS(s, a), page
 * 136, returning the states resulting from doing action a in state s.
 * 
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public interface ResultsFunction<S, A> {

	/**
	 * Returns the states that result from doing action a in state s
	 * 
	 * @param state
	 *            a particular state.
	 * @param action
	 *            an action to be performed in state s.
	 * @return the states that result from doing action a in state s.
	 */
	Set<S> results(S state, A action);
}