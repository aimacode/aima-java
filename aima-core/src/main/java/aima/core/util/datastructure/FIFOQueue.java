package aima.core.util.datastructure;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 80.<br>
 * <br>
 * First-in, first-out or FIFO queue, which pops the oldest element of the
 * queue;
 * 
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
		int size1 = this.size() ;
		if(size1==0)
		{
			return 1 ;
		}
		else
		{
			return 0 ;
		}
	}

	public E pop() {
		return this.poll();
	}

	public void push(E element) {
		this.addLast(element);
	}

	public Queue<E> insert(E element) {
		if (offer(element)!=0) {
			return this;
		}
		return null;
	}
	// END-Queue
	//
}
