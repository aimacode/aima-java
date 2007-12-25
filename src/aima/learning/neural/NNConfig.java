package aima.learning.neural;

import java.util.Hashtable;

/*
 * a holder for config data for neural networks and possibly for other
 * learning systems.
 */
public class NNConfig {
	private final Hashtable<String, Object> hash;

	public NNConfig(Hashtable<String, Object> hash) {
		this.hash = hash;
	}

	public double getParameterAsDouble(String key) {

		return (Double) hash.get(key);
	}

	public int getParameterAsInteger(String key) {

		return (Integer) hash.get(key);
	}

}
