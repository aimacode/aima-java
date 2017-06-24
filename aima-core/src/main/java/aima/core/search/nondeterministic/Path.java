package aima.core.search.nondeterministic;

import java.util.LinkedList;

/**
 * Represents the path the agent travels through the AND-OR tree (see figure
 * 4.10, page 135, AIMA3e).
 * 
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public class Path<S> extends LinkedList<S> {

	/**
	 * Create a new path containing this path's current states followed by the provided additional state.
	 *
	 * @param state
	 *            the state to be prepended.
	 * @return a new Path that contains the passed in state along with this
	 *         path's current states.
	 */
	public Path<S> append(S state) {
		Path<S> appendedPath = new Path<>();
		appendedPath.addAll(this);
		appendedPath.add(state);
		return appendedPath;
	}

	/**
	 * Create a new path containing the provided additional state followed by this path's current states.
	 * 
	 * @param state
	 *            the state to be prepended.
	 * @return a new Path that contains the passed in state along with this
	 *         path's current states.
	 */
	public Path<S> prepend(S state) {
		Path<S> prependedPath = new Path<>();
		prependedPath.add(state);
		prependedPath.addAll(this);
		return prependedPath;
	}
}
