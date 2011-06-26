package aima.core.agent.impl;

import aima.core.agent.Percept;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class DynamicPercept extends ObjectWithDynamicAttributes implements
		Percept {
	public DynamicPercept() {

	}

	@Override
	public String describeType() {
		return Percept.class.getSimpleName();
	}

	/**
	 * Constructs a DynamicPercept with one attribute
	 * 
	 * @param key1
	 *            the attribute key
	 * @param value1
	 *            the attribute value
	 */
	public DynamicPercept(Object key1, Object value1) {
		setAttribute(key1, value1);
	}

	/**
	 * Constructs a DynamicPercept with two attributes
	 * 
	 * @param key1
	 *            the first attribute key
	 * @param value1
	 *            the first attribute value
	 * @param key2
	 *            the second attribute key
	 * @param value2
	 *            the second attribute value
	 */
	public DynamicPercept(Object key1, Object value1, Object key2, Object value2) {
		setAttribute(key1, value1);
		setAttribute(key2, value2);
	}

	/**
	 * Constructs a DynamicPercept with an array of attributes
	 * 
	 * @param keys
	 *            the array of attribute keys
	 * @param values
	 *            the array of attribute values
	 */
	public DynamicPercept(Object[] keys, Object[] values) {
		assert (keys.length == values.length);

		for (int i = 0; i < keys.length; i++) {
			setAttribute(keys[i], values[i]);
		}
	}
}