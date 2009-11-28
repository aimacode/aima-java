package aima.core.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * @author Ravi Mohan
 * 
 */
public class Util {
	public static final String NO = "No";

	public static final String YES = "Yes";

	private static Random r = new Random();

	public static <T> T first(List<T> l) {
		return l.get(0);
	}

	public static <T> List<T> rest(List<T> l) {
		List<T> newList = new ArrayList<T>(l.subList(1, l.size()));
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
		return l.get(r.nextInt(l.size()));
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
		return r.nextInt(j - i + 1) + i;
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

	public static double generateRandomDoubleBetween(double lowerLimit,
			double upperLimit) {

		return lowerLimit + ((upperLimit - lowerLimit) * r.nextDouble());
	}
}