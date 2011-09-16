package aima.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A utility class for keeping counts of objects. Will return 0 for any object
 * for which it has not recorded a count against.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class FrequencyCounter<T> {
	private Map<T, Integer> counter;
	private int total;

	/**
	 * Default Constructor.
	 */
	public FrequencyCounter() {
		counter = new HashMap<T, Integer>();
		total = 0;
	}

	/**
	 * Returns the count to which the specified key is mapped in this frequency
	 * counter, or 0 if the map contains no mapping for this key.
	 * 
	 * @param key
	 *            the key whose associated count is to be returned.
	 * 
	 * @return the count to which this map maps the specified key, or 0 if the
	 *         map contains no mapping for this key.
	 */
	public Integer getCount(T key) {
		Integer value = counter.get(key);
		if (value == null) {
			return 0;
		}
		return value;
	}

	/**
	 * Increments the count to which the specified key is mapped in this
	 * frequency counter, or puts 1 if the map contains no mapping for this key.
	 * 
	 * @param key
	 *            the key whose associated count is to be returned.
	 */
	public void incrementFor(T key) {
		Integer value = counter.get(key);
		if (value == null) {
			counter.put(key, 1);
		} else {
			counter.put(key, value + 1);
		}
		// Keep track of the total
		total++;
	}

	/**
	 * Returns the count to which the specified key is mapped in this frequency
	 * counter, divided by the total of all counts.
	 * 
	 * @param key
	 *            the key whose associated count is to be divided.
	 * 
	 * @return the count to which this map maps the specified key, divided by
	 *         the total count.
	 */
	public double probabilityOf(T key) {
		Integer value = getCount(key);
		if (0 == total || 0 == value.intValue()) {
			return 0.0;
		} else {
			return value.doubleValue() / total;
		}
	}

	/**
	 * 
	 * @return a set of objects for which frequency counts have been recorded.
	 */
	public Set<T> getStates() {
		return counter.keySet();
	}

	/**
	 * Remove all the currently recorded frequency counts.
	 */
	public void clear() {
		counter.clear();
		total = 0;
	}

	@Override
	public String toString() {
		return counter.toString();
	}
}
