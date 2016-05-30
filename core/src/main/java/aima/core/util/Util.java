package aima.core.util;


import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class Util {
	/**
	 * Get the first element from a list.
	 * 
	 * @param l
	 *            the list the first element is to be extracted from.
	 * @return the first element of the passed in list.
	 */
	public static <T> T first(List<T> l) {
		return l.get(0);
	}

	/**
	 * Get a sublist of all of the elements in the list except for first.
	 * 
	 * @param l
	 *            the list the rest of the elements are to be extracted from.
	 * @return a list of all of the elements in the passed in list except for
	 *         the first element.
	 */
	public static <T> List<T> rest(List<T> l) {
		return l.subList(1, l.size());
	}

	/**
	 * Create a set for the provided values.
	 * @param values
	 *        the sets initial values.
	 * @return a Set of the provided values.
	 */
	@SafeVarargs
	public static <V> Set<V> createSet(V... values) {
		Set<V> set = new LinkedHashSet<V>();
		
		for (V v : values) {
			set.add(v);
		}
		
		return set;
	}
}