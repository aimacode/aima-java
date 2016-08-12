package aima.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */
public class Util {
	public static final String NO = "No";
	public static final String YES = "Yes";
	//
	private static Random _r = new Random();

	private static final double EPSILON = 0.000000000001;
	
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
	 * Create a Map<K, V> with the passed in keys having their values
	 * initialized to the passed in value.
	 * 
	 * @param keys
	 *            the keys for the newly constructed map.
	 * @param value
	 *            the value to be associated with each of the maps keys.
	 * @return a map with the passed in keys initialized to value.
	 */
	public static <K, V> Map<K, V> create(Collection<K> keys, V value) {
		Map<K, V> map = new LinkedHashMap<K, V>();

		for (K k : keys) {
			map.put(k, value);
		}

		return map;
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

	/**
	 * Randomly select an element from a list.
	 * 
	 * @param <T>
	 *            the type of element to be returned from the list l.
	 * @param l
	 *            a list of type T from which an element is to be selected
	 *            randomly.
	 * @return a randomly selected element from l.
	 */
	public static <T> T selectRandomlyFromList(List<T> l) {
		return l.get(_r.nextInt(l.size()));
	}

	public static boolean randomBoolean() {
		int trueOrFalse = _r.nextInt(2);
		return (!(trueOrFalse == 0));
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

	public static List<Double> normalize(List<Double> values) {
		double[] valuesAsArray = new double[values.size()];
		for (int i = 0; i < valuesAsArray.length; i++) {
			valuesAsArray[i] = values.get(i);
		}
		double[] normalized = normalize(valuesAsArray);
		List<Double> results = new ArrayList<Double>();
		for (int i = 0; i < normalized.length; i++) {
			results.add(normalized[i]);
		}
		return results;
	}

	public static int min(int i, int j) {
		return (i > j ? j : i);
	}

	public static int max(int i, int j) {
		return (i < j ? j : i);
	}

	public static int max(int i, int j, int k) {
		return max(max(i, j), k);
	}

	public static int min(int i, int j, int k) {
		return min(min(i, j), k);
	}

	public static <T> T mode(List<T> l) {
		Hashtable<T, Integer> hash = new Hashtable<T, Integer>();
		for (T obj : l) {
			if (hash.containsKey(obj)) {
				hash.put(obj, hash.get(obj).intValue() + 1);
			} else {
				hash.put(obj, 1);
			}
		}

		T maxkey = hash.keySet().iterator().next();
		for (T key : hash.keySet()) {
			if (hash.get(key) > hash.get(maxkey)) {
				maxkey = key;
			}
		}
		return maxkey;
	}

	public static String[] yesno() {
		return new String[] { YES, NO };
	}

	public static double log2(double d) {
		return Math.log(d) / Math.log(2);
	}

	public static double information(double[] probabilities) {
		double total = 0.0;
		for (double d : probabilities) {
			total += (-1.0 * log2(d) * d);
		}
		return total;
	}

	public static <T> List<T> removeFrom(List<T> list, T member) {
		List<T> newList = new ArrayList<T>(list);
		newList.remove(member);
		return newList;
	}

	public static <T extends Number> double sumOfSquares(List<T> list) {
		double accum = 0;
		for (T item : list) {
			accum = accum + (item.doubleValue() * item.doubleValue());
		}
		return accum;
	}

	public static String ntimes(String s, int n) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < n; i++) {
			buf.append(s);
		}
		return buf.toString();
	}

	public static void checkForNanOrInfinity(double d) {
		if (Double.isNaN(d)) {
			throw new RuntimeException("Not a Number");
		}
		if (Double.isInfinite(d)) {
			throw new RuntimeException("Infinite Number");
		}
	}

	public static int randomNumberBetween(int i, int j) {
		/* i,j bothinclusive */
		return _r.nextInt(j - i + 1) + i;
	}

	public static double calculateMean(List<Double> lst) {
		Double sum = 0.0;
		for (Double d : lst) {
			sum = sum + d.doubleValue();
		}
		return sum / lst.size();
	}

	public static double calculateStDev(List<Double> values, double mean) {

		int listSize = values.size();

		Double sumOfDiffSquared = 0.0;
		for (Double value : values) {
			double diffFromMean = value - mean;
			sumOfDiffSquared += ((diffFromMean * diffFromMean) / (listSize - 1));
			// division moved here to avoid sum becoming too big if this
			// doesn't work use incremental formulation

		}
		double variance = sumOfDiffSquared;
		// (listSize - 1);
		// assumes at least 2 members in list.
		return Math.sqrt(variance);
	}

	public static List<Double> normalizeFromMeanAndStdev(List<Double> values,
			double mean, double stdev) {
		List<Double> normalized = new ArrayList<Double>();
		for (Double d : values) {
			normalized.add((d - mean) / stdev);
		}
		return normalized;
	}
	
	/**
	 * Generates a random double between two limits. Both limits are inclusive.
	 * @param lowerLimit the lower limit.
	 * @param upperLimit the upper limit.
	 * @return a random double bigger or equals {@code lowerLimit} and smaller or equals {@code upperLimit}.
	 */
	public static double generateRandomDoubleBetween(double lowerLimit, double upperLimit) {
		return lowerLimit + ((upperLimit - lowerLimit) * _r.nextDouble());
	}

	/**
	 * Generates a random float between two limits. Both limits are inclusive.
	 * @param lowerLimit the lower limit.
	 * @param upperLimit the upper limit.
	 * @return a random float bigger or equals {@code lowerLimit} and smaller or equals {@code upperLimit}.
	 */
	public static float generateRandomFloatBetween(float lowerLimit, float upperLimit) {
		return lowerLimit + ((upperLimit - lowerLimit) * _r.nextFloat());
	}
	
	/**
	 * Compares two doubles for equality.
	 * @param a the first double.
	 * @param b the second double.
	 * @return true if both doubles contain the same value or the absolute deviation between them is below {@code EPSILON}.
	 */
	public static boolean compareDoubles(double a, double b) {
		if(Double.isNaN(a) && Double.isNaN(b)) return true;
		if(!Double.isInfinite(a) && !Double.isInfinite(b)) return Math.abs(a-b) <= EPSILON;
		return a == b;
	}
	
	/**
	 * Compares two floats for equality.
	 * @param a the first floats.
	 * @param b the second floats.
	 * @return true if both floats contain the same value or the absolute deviation between them is below {@code EPSILON}.
	 */
	public static boolean compareFloats(float a, float b) {
		if(Float.isNaN(a) && Float.isNaN(b)) return true;
		if(!Float.isInfinite(a) && !Float.isInfinite(b)) return Math.abs(a-b) <= EPSILON;
		return a == b;
	}
}