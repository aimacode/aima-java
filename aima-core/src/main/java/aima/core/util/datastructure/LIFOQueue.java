package aima.core.util.datastructure;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 80.<br>
 * <br>
 * Last-in, first-out or LIFO queue (also known as a stack), which pops the
 * newest element of the queue;
 * 
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
		int size1 = this.size() ;
		if(size1==0)
			return true ;
		return false ;
	}

	@Override
	public E pop() {
		return this.poll();
	}

	public void push(E element) {
		this.addFirst(element);
	}

	public Queue<E> insert(E element) {
		if(offer(element)!=false) {
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
		this.addFirst(e);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.addAll(0, c);
	}

	@Override
	public boolean offer(E e) {
		this.add(0, e);
		return true;
	}
	// End-Override LinkedList methods in order for it to behave like a LIFO.
	//
}