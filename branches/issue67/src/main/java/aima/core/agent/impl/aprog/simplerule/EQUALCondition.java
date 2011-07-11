package aima.core.agent.impl.aprog.simplerule;

import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 * Implementation of an EQUALity condition.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class EQUALCondition extends Condition {
	private Object key;

	private Object value;

	public EQUALCondition(Object aKey, Object aValue) {
		assert (null != aKey);
		assert (null != aValue);

		key = aKey;
		value = aValue;
	}

	@Override
	public boolean evaluate(ObjectWithDynamicAttributes p) {
		return value.equals(p.getAttribute(key));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.append(key).append("==").append(value).toString();
	}
}
