package aima.extra.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This code performs some basic operations on list data structures.
 * 
 * @author Nagaraj Poti
 */
public class ListOps {

	/**
	 * Union of two lists. The resultant list contains all elements of l1
	 * followed by elements of l2. No duplicate entries will exist, unless l1 or
	 * l2 themselves contain duplicate elements.
	 * 
	 * @param l1
	 * @param l2
	 * @param <T>
	 * 
	 * @return the union of l1 and l2. (The union of two lists is the list
	 *         containing all of the elements contained in either list.)
	 */
	public static <T> List<T> union(List<T> l1, List<T> l2) {
		if (l1 == l2) {
			return l1;
		} else if (null == l1 || null == l2) {
			return (l1 == null ? l2 : l1);
		}
		List<T> union = Stream.concat(l1.stream(), l2.stream()).distinct().collect(Collectors.toList());
		return union;
	}

	/**
	 * Intersection of two lists. The resultant list contains elements present
	 * in both l1 and l2. No duplicate entries will exist, unless l1 or l2
	 * themselves contain duplicate elements. The elements in the resultant list
	 * retain their relative order as in l1.
	 * 
	 * @param l1
	 * @param l2
	 * @param <T>
	 * 
	 * @return the intersection of l1 and l2. (The intersection of two lists is
	 *         the list containing only the elements contained in both lists.)
	 */
	public static <T> List<T> intersection(List<T> l1, List<T> l2) {
		if (l1 == l2) {
			return l1;
		} else if (null == l1 || null == l2) {
			return null;
		}
		List<T> intersection = l1.stream().filter(l2::contains).collect(Collectors.toList());
		return intersection;
	}

	/**
	 * Difference of two lists. The resultant list contains elements unique to
	 * l1 only (i.e not present in l2). No duplicate entries will exist. The
	 * elements in the resultant list retain their relative order as in l1.
	 * 
	 * @param l1
	 * @param l2
	 * @param <T>
	 * 
	 * @return the difference of l1 and l2. (The difference of two lists is the
	 *         list containing only the elements unique to l1 and not in l2.)
	 */
	public static <T> List<T> difference(List<T> l1, List<T> l2) {
		if (l1 == l2 || null == l1) {
			return null;
		} else if (null == l2) {
			return l1;
		}
		List<T> difference = l1.stream().filter(elem -> !l2.contains(elem)).distinct().collect(Collectors.toList());
		return difference;
	}

	/**
	 * Intersection indices of two lists. Retrieve the indices from the source
	 * list whose elements are present in the target list.
	 * 
	 * @param source
	 *            list containing random variables.
	 * @param target
	 *            list containing random variables.
	 * 
	 * @return list of source list indices of common elements in both lists in
	 *         the order of occurence in the source list.
	 */
	public static <T> List<Integer> getIntersectionIdxInSource(List<T> source, List<T> target) {
		if (null == source || null == target) {
			return null;
		}
		List<Integer> result = IntStream.range(0, source.size()).filter(idx -> target.contains(source.get(idx))).boxed()
				.collect(Collectors.toList());
		return result;
	}

	/**
	 * Intersection indices of two lists. Retrieve the indices from the target
	 * list whose elements are present in the source list.
	 * 
	 * @param source
	 *            list containing random variables.
	 * @param target
	 *            list containing random variables.
	 * 
	 * @return list of target list indices of common elements in both lists in
	 *         the order of occurence in the source list.
	 */
	public static <T> List<Integer> getIntersectionIdxInTarget(List<T> source, List<T> target) {
		if (null == source || null == target) {
			return null;
		}
		List<Integer> result = IntStream.range(0, source.size()).filter(idx -> target.contains(source.get(idx)))
				.map(idx -> target.indexOf(source.get(idx))).boxed().collect(Collectors.toList());
		return result;
	}

	/**
	 * Return an unmodifiable version of a list.
	 * 
	 * @param list
	 * @param <T>
	 * 
	 * @return an unmodifiable list.
	 */
	public static <T> List<T> protectListFromModification(List<T> list) {
		return Collections.unmodifiableList(list);
	}

	/**
	 * @param list
	 * 
	 * @return first element of the list.
	 */
	public static <T> T first(List<T> list) {
		return list.get(0);
	}

	/**
	 * @param list
	 * 
	 * @return a new list excluding the first element of the original list.
	 */
	public static <T> List<T> rest(List<T> list) {
		return list.subList(1, list.size());
	}
}
