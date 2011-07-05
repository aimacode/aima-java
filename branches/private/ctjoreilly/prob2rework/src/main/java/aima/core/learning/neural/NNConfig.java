package aima.core.learning.neural;

import java.util.Hashtable;

/**
 * A holder for config data for neural networks and possibly for other learning
 * systems.
 * 
 * @author Ravi Mohan
 * 
 */
public class NNConfig {
	private final Hashtable<String, Object> hash;

	public NNConfig(Hashtable<String, Object> hash) {
		this.hash = hash;
	}

	public NNConfig() {
		this.hash = new Hashtable<String, Object>();
	}

	public double getParameterAsDouble(String key) {

		return (Double) hash.get(key);
	}

	public int getParameterAsInteger(String key) {

		return (Integer) hash.get(key);
	}

	public void setConfig(String key, Double value) {
		hash.put(key, value);
	}

	public void setConfig(String key, int value) {
		hash.put(key, value);
	}
}
