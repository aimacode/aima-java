package aima.extra.util.datastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Bidirectional map datastructure.
 *
 * @param <K>
 *            Key in the forward direction, value in the backward direction.
 * @param <V>
 *            Value in the forward direction, key in the backward direction.
 * 
 * @author Nagaraj Poti
 */
public class BiMap<K extends Object, V extends Object> {

	// Forward map
	private Map<K, V> forward = new HashMap<K, V>();

	// Inverse map
	private Map<V, K> backward = new HashMap<V, K>();

	/**
	 * Associates the specified key with the specified value in the forward map
	 * and vice versa in the inverse map. If the forward map previously
	 * contained the key, the old value is replaced and consistency is ensured
	 * in the inverse map.
	 * 
	 * @param key
	 *            for the forward map.
	 * @param value
	 *            for the forward map.
	 */
	public void put(K key, V value) {
		if (forward.containsKey(key)) {
			backward.remove(value);
		}
		forward.put(key, value);
		backward.put(value, key);
	}

	/**
	 * Get the value associated with the key in the forward map.
	 * 
	 * @param key
	 *            to be searched for in the forward map.
	 * 
	 * @return value associated with the key in the forward map.
	 */
	public V getForward(K key) {
		return forward.get(key);
	}

	/**
	 * Get the value associated with the key in the inverse map.
	 * 
	 * @param key
	 *            to be searched for in the inverse map.
	 * 
	 * @return value associated with the key in the inverse map.
	 */
	public K getBackward(V key) {
		return backward.get(key);
	}
}