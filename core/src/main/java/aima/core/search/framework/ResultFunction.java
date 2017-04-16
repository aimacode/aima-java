package aima.core.search.framework;

import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??. <br>
 * <br>
 * Closely related to ResultFunction, but for non-deterministic problems; in
 * these problems, the outcome of an action will be a set of results, not a
 * single result. This class implements the functionality of RESULTS(s, a), page
 * 136, returning the states resulting from doing action a in state s.
 * 
 * @author Andrew Brown
 */
public interface ResultFunction<A> {

	/**
	 * Returns the states that result from doing action a in state s
	 * 
	 * @param s
	 *            a particular state.
	 * @param a
	 *            an action to be performed in state s.
	 * @return the states that result from doing action a in state s.
	 */
	Set<Object> result(Object s, A a);
}
