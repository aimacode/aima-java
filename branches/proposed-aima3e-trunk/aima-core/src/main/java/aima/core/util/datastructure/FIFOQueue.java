package aima.core.util.datastructure;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class FIFOQueue<E> extends LinkedList<E> implements Queue<E> {
	private static final long serialVersionUID = 1;

	public FIFOQueue() {
		super();
	}

	public FIFOQueue(Collection<? extends E> c) {
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