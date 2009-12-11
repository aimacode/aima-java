package aima.core.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */
public class Converter<T> {

	public List<T> setToList(Set<T> set) {
		List<T> retVal = new ArrayList<T>(set);
		return retVal;
	}

	public Set<T> listToSet(List<T> l) {
		Set<T> retVal = new HashSet<T>(l);
		return retVal;
	}
}
