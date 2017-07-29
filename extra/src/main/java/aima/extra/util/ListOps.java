package aima.extra.util;

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
	 * followed by elements of l2. No duplicate entries will exist.
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
		}
		List<T> union = Stream.concat(l1.stream(), l2.stream()).distinct().collect(Collectors.toList());
		return union;
	}

	/**
	 * Intersection of two lists. The resultant list contains elements present
	 * in both l1 and l2. No duplicate entries will exist. The elements in the
	 * resultant list retain their relative order as in l1.
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
		List<T> difference = l1.stream().filter(elem -> !l2.contains(elem)).collect(Collectors.toList());
		return difference;
	}

	/**
	 * Intersection indices of two lists. Retrieve the indices from the source
	 * list that whose elements are present in the target list.
	 * 
	 * @param source
	 *            list containing random variables.
	 * @param target
	 *            list containing random variables.
	 * 
	 * @return list of indices of common elements in both lists in sorted order.
	 */
	public static <T> List<Integer> getIntersectionIdx(List<T> source, List<T> target) {
		List<Integer> result = IntStream.range(0, source.size()).filter(idx -> target.contains(source.get(idx))).boxed()
				.sorted().collect(Collectors.toList());
		return result;
	}

	/**
	 * Difference indices of two lists. Retrieve the indices from the source
	 * list whose elements are not present in the target list.
	 * 
	 * @param source
	 *            list containing random variables.
	 * @param target
	 *            list containing random variables.
	 * 
	 * @return list of indices of elements unique to the source list in sorted
	 *         order.
	 */
	public static <T> List<Integer> getDifferenceIdx(List<T> source, List<T> target) {
		List<Integer> result = IntStream.range(0, source.size()).filter(idx -> !target.contains(source.get(idx))).boxed()
				.sorted().collect(Collectors.toList());
		return result;
	}
}
