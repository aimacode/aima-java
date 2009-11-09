package aima.core.agent.impl;

import aima.core.agent.Percept;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class DynamicPercept extends ObjectWithDynamicAttributes implements
		Percept {
	public DynamicPercept() {

	}

	@Override
	public String describeType() {
		return Percept.class.getSimpleName();
	}

	public DynamicPercept(Object key1, Object value1) {
		setAttribute(key1, value1);
	}

	public DynamicPercept(Object key1, Object value1, Object key2, Object value2) {
		setAttribute(key1, value1);
		setAttribute(key2, value2);
	}

	public DynamicPercept(Object[] keys, Object[] values) {
		assert (keys.length == values.length);

		for (int i = 0; i < keys.length; i++) {
			setAttribute(keys[i], values[i]);
		}
	}
}