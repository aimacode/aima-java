package aima.core.search.api;

import java.util.Comparator;

/**
 * The data structure for a priority based frontier needs to support efficient membership 
 * testing, so it should combine the capabilities of a priority queue and a hash table. 
 * 
 * @param <A> the type of the actions that can be performed.
 * @param <S> the type of the state space.
 * 
 * @author Ciaran O'Reilly
 */
public interface PriorityFrontierQueue<A, S> extends FrontierQueueWithStateTracking<A, S> {
	
	default boolean containsState(Node<A, S> node) {
		return containsState(node.state());
	}
	
	boolean containsState(S state);
	
	// NOTE: by Java's PriorityQueue convention, nodes that compare lower have higher priority.
	Comparator<Node<A, S>> getComparator();
}
