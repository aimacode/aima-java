package aima.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class FrequencyCounter<T> {
	private Map<T, Integer> counter;

	public FrequencyCounter() {
		counter = new HashMap<T, Integer>();
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
	public Double probabilityOf(T key) {
		Integer value = getCount(key);
		if (value == 0) {
			return 0.0;
		} else {
			Double total = 0.0;
			for (T k : counter.keySet()) {
				total += getCount(k);
			}
			return value / total;
		}
	}

	@Override
	public String toString() {
		return counter.toString();
	}

	public Set<T> getStates() {
		return counter.keySet();
	}
}
