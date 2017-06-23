package aima.core.search.nondeterministic;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the path the agent travels through the AND-OR tree (see figure
 * 4.10, page 135, AIMA3e).
 * 
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public class Path<S> extends LinkedList<S> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creating a new path based on this path and the passed in appended states.
	 * 
	 * @param states
	 *            the states to append to a new copy of this path.
	 * 
	 * @return a new Path that contains this path's states along with the passed
	 *         in argument states appended to the end.
	 */
	public Path<S> append(List<S> states) {
		Path<S> appendedPath = new Path<>();
		appendedPath.addAll(this);
		appendedPath.addAll(states);
		return appendedPath;
	}

	/**
	 * Create a new path based on the passed in prepended state and this path's
	 * current states.
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
