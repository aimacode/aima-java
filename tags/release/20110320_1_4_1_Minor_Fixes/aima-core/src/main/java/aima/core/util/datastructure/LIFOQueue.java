package aima.core.util.datastructure;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 80.<br>
 * 
 * Last-in, first-out or LIFO queue (also known as a stack), which pops the newest element of the queue;
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class LIFOQueue<E> extends LinkedList<E> implements Queue<E> {
	private static final long serialVersionUID = 1;

	public LIFOQueue() {
		super();
	}

	public LIFOQueue(Collection<? extends E> c) {
		super(c);
	}

	//
	// START-Queue
	public boolean isEmpty() {
		return 0 == size();
	}

	@Override
	public E pop() {
		return poll();
	}

	public void push(E element) {
		addFirst(element);
	}

	public Queue<E> insert(E element) {
		if (offer(element)) {
			return this;
		}
		return null;
	}

	// END-Queue
	//

	//
	// START-Override LinkedList methods in order for it to behave in LIFO
	// order.
	@Override
	public boolean add(E e) {
		addFirst(e);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return addAll(0, c);
	}

	@Override
	public boolean offer(E e) {
		add(0, e);
		return true;
	}
	// End-Override LinkedList methods in order for it to behave like a LIFO.
	//
}