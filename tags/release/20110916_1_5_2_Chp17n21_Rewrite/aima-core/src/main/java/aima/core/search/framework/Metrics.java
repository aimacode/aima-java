package aima.core.search.framework;

import java.util.Hashtable;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
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

	public int getInt(String name) {
		return new Integer(hash.get(name)).intValue();
	}

	public double getDouble(String name) {
		return new Double(hash.get(name)).doubleValue();
	}

	public String get(String name) {
		return hash.get(name);
	}

	public Set<String> keySet() {
		return hash.keySet();
	}
}