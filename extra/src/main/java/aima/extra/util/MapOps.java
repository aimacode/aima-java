package aima.extra.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This code performs some basic operations on map data structures.
 * 
 * @author Nagaraj Poti
 */
public class MapOps {

	/**
	 * Merge two maps and return a new map
	 * 
	 * @param mp1
	 * @param mp2
	 *
	 * @return a new map consisting of entries from mp1 and mp2.
	 */
	public static <K, V> Map<K, V> merge(Map<K, V> mp1, Map<K, V> mp2) {
		Map<K, V> result = new HashMap<K, V>();
		result.putAll(mp1);
		result.putAll(mp2);
		return result;
	}
}
