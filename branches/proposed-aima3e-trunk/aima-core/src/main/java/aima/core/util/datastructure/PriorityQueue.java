package aima.core.util.datastructure;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 80.<br>
 * 
 * The priority queue, which pops the element of the queue with the highest
 * priority according to some ordering function.
 */

/**
 * @author Ciaran O'Reilly
 */
public class PriorityQueue<E> extends java.util.PriorityQueue<E> implements
		Queue<E> {
	private static final long serialVersionUID = 1;

	public PriorityQueue() {
		super();
	}

	public PriorityQueue(Collection<? extends E> c) {
		super(c);
	}

	public PriorityQueue(int initialCapacity) {
		super(initialCapacity);
	}

	public PriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
		super(initialCapacity, comparator);
	}

	public PriorityQueue(PriorityQueue<? extends E> c) {
		super(c);
	}

	public PriorityQueue(SortedSet<? extends E> c) {
		super(c);
	}

	//
	// START-Queue
	public boolean isEmpty() {
		return 0 == size();
	}

	public E pop() {
		return poll();
	}

	public Queue<E> insert(E element) {
		if (offer(element)) {
			return this;
		}
		return null;
	}
	// END-Queue
	//
}