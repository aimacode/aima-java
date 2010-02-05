package aima.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */
public class FrequencyCounter<T> {
	private Map<T, Integer> counter;

	public FrequencyCounter() {
		counter = new HashMap<T, Integer>();
	}

	public Integer getCount(T key) {
		Integer value = counter.get(key);
		if (value == null) {
			return 0;
		}
		return value;
	}

	public void incrementFor(T key) {
		Integer value = counter.get(key);
		if (value == null) {
			counter.put(key, 1);
		} else {
			counter.put(key, value + 1);
		}
	}

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
