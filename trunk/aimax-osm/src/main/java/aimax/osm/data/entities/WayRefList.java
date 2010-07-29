package aimax.osm.data.entities;

import java.util.Iterator;

/**
 * Provides list-like read access to way references.
 * @author Ruediger Lunde
 */ 
public class WayRefList implements Iterable<WayRef> {
	private WayRef start;
	
	public WayRefList(WayRef start) {
		this.start = start;
	}

	public WayRef get(int index) {
		WayRef result = start;
		for (int i = 0; i < index; i++)
			result = result.getNext();
		return null;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return start == null;
	}

	public Iterator<WayRef> iterator() {
		return new WayRefIterator(start);
	}

	public int size() {
		int result = 0;
		for (WayRef ref = start; ref != null; ref = ref.getNext())
			++result;
		return result;
	}
	
	
	//////////////////////////////////////////////////////////////////////
	// inner classes
	
	/** Iterates across a list of linked way references. */
	private static class WayRefIterator implements Iterator<WayRef> {
		private WayRef curr;
		private WayRefIterator(WayRef start) {
			curr = start;
		}
		@Override
		public boolean hasNext() {
			return curr != null;
		}
		@Override
		public WayRef next() {
			WayRef result= curr;
			curr = curr.getNext();
			return result;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
