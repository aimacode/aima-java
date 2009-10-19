package aima.core.util.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */
public class FIFOQueue extends DefaultQueue {
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
		return super.removeLast();
	}

	@Override
	public Object get() {
		return super.getLast();
	}

}