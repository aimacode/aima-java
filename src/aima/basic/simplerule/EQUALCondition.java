package aima.basic.simplerule;

import aima.basic.ObjectWithDynamicAttributes;

/**
 * Implementation of an EQUALity condition.
 *
 */

/**
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
		StringBuffer sb = new StringBuffer();

		return sb.append(key).append("==").append(value).toString();
	}
}
