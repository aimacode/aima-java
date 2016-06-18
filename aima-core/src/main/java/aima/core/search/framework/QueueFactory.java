package aima.core.search.framework;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Factory class for queues. Changes made here will affect all queue based
 * search algorithms of this library.
 * 
 * @author Ruediger Lunde
 *
 */
public class QueueFactory {

	/**
	 * Returns a {@link LinkedList}.
	 */
	public static <E> Queue<E> createFifoQueue() {
		return new LinkedList<E>();
	}

	/**
	 * Returns a Last-in-first-out (Lifo) view on a {@link LinkedList}.
	 */
	public static <E> Queue<E> createLifoQueue() {
		return Collections.asLifoQueue(new LinkedList<E>());
	}

	/**
	 * Returns a standard java {@link PriorityQueue}. Note that the smallest
	 * element comes first!
	 */
	public static <E> Queue<E> createPriorityQueue(Comparator<? super E> comparator) {
		return new PriorityQueue<E>(11, comparator);
	}
}
