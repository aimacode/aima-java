package aima.core.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import aima.core.util.collect.CartesianProduct;

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
	 * 
	 * @param values
	 *            the sets initial values.
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

	public static double[] normalize(double[] probDist) {
		int len = probDist.length;
		double total = 0.0;
		for (double d : probDist) {
			total = total + d;
		}

		double[] normalized = new double[len];
		if (total != 0) {
			for (int i = 0; i < len; i++) {
				normalized[i] = probDist[i] / total;
			}
		}

		return normalized;
	}

	public static <T> void permuteArguments(Class<T> argumentType, List<List<T>> individualArgumentValues,
			Consumer<T[]> argConsumer) {
		Iterator<T[]> argsIt = new CartesianProduct<T>(argumentType, individualArgumentValues).iterator();
		while (argsIt.hasNext()) {
			argConsumer.accept(argsIt.next());
		}
	}
}