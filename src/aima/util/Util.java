/*
 * Created on Aug 24, 2003 by Ravi Mohan
 *  
 */
package aima.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import aima.probability.Randomizer;

public class Util {
	public static final String NO = "No";

	public static final String YES = "Yes";

	private static Random r = new Random();

	public static <T> T first(List<T> l) {

		List<T> newList = new ArrayList<T>();
		for (T element : l) {
			newList.add(element);
		}
		return newList.get(0);
	}

	public static <T> List<T> rest(List<T> l) {
		List<T> newList = new ArrayList<T>();
		for (T element : l) {
			newList.add(element);
		}
		newList.remove(0);
		return newList;
	}

	public static boolean randomBoolean() {
		int trueOrFalse = r.nextInt(2);
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
		double totalN = 0.0;
		for (double d : normalized) {
			totalN = totalN + d;
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

	public static <T> T selectRandomlyFromList(List<T> l) {
		int index = r.nextInt(l.size());
		return l.get(index);
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
		List<T> newList = new ArrayList<T>();
		for (T s : list) {
			if (!(s.equals(member))) {
				newList.add(s);
			}
		}
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
		if ( Double.isInfinite(d)) {
			throw new RuntimeException("Infinite Number");
		}
	}

}