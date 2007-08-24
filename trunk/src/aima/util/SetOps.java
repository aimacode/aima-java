/*
 * Created on Jun 9, 2005
 *
 */
package aima.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */

public class SetOps<T> {
	public Set<T> union(Set<T> one, Set<T> two) {
		Set<T> union = new HashSet<T>(one);
		union.addAll(two);
		return union;
	}

	public Set<T> intersection(Set<T> one, Set<T> two) {
		Set<T> intersection = new HashSet<T>(one);
		intersection.retainAll(two);
		return intersection;
	}

	public Set<T> difference(Set<T> one, Set<T> two) {
		Set<T> three = new HashSet<T>();
		Iterator<T> iteratorOne = one.iterator();
		while (iteratorOne.hasNext()) {
			T sym = iteratorOne.next();
			if (!(in(two, sym))) {
				three.add(sym);
			}
		}
		return three;
	}

	public boolean in(Set<T> s, T o) {

		Iterator<T> i = s.iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			if (obj.equals(o)) {
				return true;
			}
		}
		return false;
	}

}
