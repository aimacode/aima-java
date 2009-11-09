package aima.core.util.datastructure;

import java.util.Collection;
import java.util.LinkedList;

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
		return offerFirst(e);
	}
	// End-Override LinkedList methods in order for it to behave like a LIFO.
	//
}