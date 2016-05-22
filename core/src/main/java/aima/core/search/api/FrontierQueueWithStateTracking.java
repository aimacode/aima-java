package aima.core.search.api;

import java.util.Queue;

/**
 * The data structure for frontier needs to support efficient membership testing. 
 * 
 * @param <A> the type of the actions that can be performed.
 * @param <S> the type of the state space.
 * 
 * @author Ciaran O'Reilly
 */
public interface FrontierQueueWithStateTracking<A, S> extends Queue<Node<A, S>> {
	
	default boolean containsState(Node<A, S> node) {
		return containsState(node.state());
	}
	
	boolean containsState(S state);
}
