package aima.core.search.framework;

import java.util.Hashtable;
import java.util.Set;

/**
 * Stores key-value pairs for efficiency analysis.
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
	
	public void set(String name, long l) {
		hash.put(name, Long.toString(l));
	}

	public int getInt(String name) {
		return new Integer(hash.get(name)).intValue();
	}

	public double getDouble(String name) {
		return new Double(hash.get(name)).doubleValue();
	}
	
	public long getLong(String name) {
		return new Long(hash.get(name)).longValue();
	}

	public String get(String name) {
		return hash.get(name);
	}

	public Set<String> keySet() {
		return hash.keySet();
	}
	
	public String toString() {
		return hash.toString();
	}
}
