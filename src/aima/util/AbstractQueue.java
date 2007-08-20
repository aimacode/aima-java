package aima.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 71.
 * 
 * The operations on a queue are as follows: MAKE-QUEUE(element,...) creates a
 * queue with the given element(s). EMPTY?(queue) returns true only if there are
 * no more elements in the queue. FIRST(queue) returns the first element of the
 * queue. REMOVE-FIRST(queue) returns FIRST(queue) and removes it from the
 * queue. INSERT(element, queue) inserts an element into the queue and returns
 * the resulting queue. (We will see that different types of queues insert
 * elements in different orders.) INSERT-ALL(elements, queue) inserts a set of
 * elements into the queue and returns the resulting queue.
 */

public class AbstractQueue implements Queue {
	protected LinkedList<Object> linkedList;

	public AbstractQueue() {
		linkedList = new LinkedList<Object>();
	}

	public void addToFront(Object n) {
		linkedList.addFirst(n);
	}

	public void addToBack(Object n) {
		linkedList.addLast(n);
	}

	public void addToFront(List list) {
		for (int i = 0; i < list.size(); i++) {
			addToFront(list.get(list.size() - 1 - i));
		}
	}

	public void addToBack(List list) {
		for (int i = 0; i < list.size(); i++) {
			addToBack(list.get(i));
		}
	}

	public Object removeFirst() {
		return (linkedList.removeFirst());
	}

	public Object removeLast() {
		return (linkedList.removeLast());
	}

	public Object getFirst() {
		return (linkedList.getFirst());
	}

	public Object getLast() {
		return (linkedList.getLast());
	}

	public boolean isEmpty() {
		return linkedList.isEmpty();
	}

	public int size() {
		return linkedList.size();
	}

	public List asList() {
		return linkedList;
	}

	public void add(Object anItem) {
		throw new RuntimeException("must be implemented by subclasses");
	}

	public void add(List items) {
		throw new RuntimeException("must be implemented by subclasses");
	}

	public Object remove() {
		throw new RuntimeException("must be implemented by subclasses");
	}

	public Object get() {
		throw new RuntimeException("must be implemented by subclasses");
	}

}