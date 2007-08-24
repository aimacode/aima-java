package aima.datastructures;

import java.util.ArrayList;
import java.util.List;

import aima.util.AbstractQueue;

/**
 * @author Ravi Mohan
 * 
 */
public class LIFOQueue extends AbstractQueue {

	@Override
	public void add(Object anItem) {
		super.addToFront(anItem);
	}

	@Override
	public void add(List items) {
		List<Object> reversed = new ArrayList<Object>();
		for (int i = items.size() - 1; i > -1; i--) {
			reversed.add(items.get(i));
		}
		super.addToFront(reversed);
	}

	@Override
	public Object remove() {
		return super.removeFirst();
	}

	@Override
	public Object get() {
		return super.getFirst();
	}
}