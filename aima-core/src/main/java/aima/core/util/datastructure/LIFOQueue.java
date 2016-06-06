package aima.core.util.datastructure;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 80.<br>
 * <br>
 * Last-in, first-out or LIFO queue (also known as a stack), which pops the
 * newest element of the queue. This implementation overrides standard Java
 * queue operations of LinkedList to obtain a LIFO behavior.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class LIFOQueue<E> extends LinkedList<E> implements Queue<E> {
	private static final long serialVersionUID = 1;

	public LIFOQueue(char c) {
		super();
	}

	public LIFOQueue(char ch, Collection<? extends E> c) {
		super(c);
	}
	
	/** Adds the specified element at the head of the queue. */
	@Override
	public boolean add(E element) {
		addFirst(element);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return addAll(0, c);
	}

	/** Adds the specified element at the head of the queue. */
	@Override
	public boolean offer(E e) {
		addFirst(e);
		return true;
	}
}