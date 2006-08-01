package aima.util;

import java.util.LinkedList;
import java.util.List;

public class AbstractQueue implements Queue {
	protected LinkedList<Object> linkedList;

	public AbstractQueue () {
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