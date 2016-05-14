package aima.core.search.framework;

import java.util.Hashtable;
import java.util.Set;
import java.util.TreeMap;

/**
 * Stores key-value pairs for efficiency analysis.
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class Metrics {
	private Hashtable<String, String> hash;

	public Metrics() {
		this.hash = new Hashtable<String, String>();
	}

	public void set(String name, int i) {
		hash.put(name, Integer.toString(i));
	}

	public void set(String name, double d) {
		hash.put(name, Double.toString(d));
	}

	public void incrementInt(String name) {
		set(name, getInt(name) + 1);
	}

	public void set(String name, long l) {
		hash.put(name, Long.toString(l));
	}

	public int getInt(String name) {
		String value = hash.get(name);
		return value != null ? Integer.parseInt(value) : 0;
	}

	public double getDouble(String name) {
		String value = hash.get(name);
		return value != null ? Double.parseDouble(value) : Double.NaN;
	}

	public long getLong(String name) {
		String value = hash.get(name);
		return value != null ? Long.parseLong(value) : 0l;
	}

	public String get(String name) {
		return hash.get(name);
	}

	public Set<String> keySet() {
		return hash.keySet();
	}

	/** Sorts the key-value pairs by key names and formats them as equations. */
	public String toString() {
		TreeMap<String, String> map = new TreeMap<String, String>(hash);
		return map.toString();
	}
}
