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
public class BiMap<K, V> {

	// Forward map
	private Map<K, V> forward = new HashMap<K, V>();

	// Inverse map
	private Map<V, K> backward = new HashMap<V, K>();

	// Public methods
	
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
		if (this.forward.containsKey(key)) {
			this.backward.remove(value);
		}
		this.forward.put(key, value);
		this.backward.put(value, key);
	}

	/**
	 * Get the value associated with the key in the forward map.
	 * 
	 * @param key
	 *            to be searched for in the forward map.
	 * 
	 * @return value associated with the key in the forward map if it exists,
	 *         null otherwise.
	 */
	public V getForward(K key) {
		return this.forward.get(key);
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
		return this.backward.get(key);
	}

	/**
	 * @return the number of key-value mappings in this BiMap.
	 */
	public int size() {
		return this.forward.size();
	}

	/**
	 * Compares the specified object with this BiMap for equality. Returns
	 * <tt>true</tt> if the given object is also a BiMap and the two BiMaps
	 * represent the same mappings.
	 *
	 * @param o
	 *            object to be compared for equality with this BiMap
	 * 
	 * @return <tt>true</tt> if the specified object is equal to this BiMap
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof BiMap<?, ?>)) {
			return false;
		}
		BiMap<?, ?> other = (BiMap<?, ?>) o;
		return this.forward.equals(other.forward);
	}
}