package aima.basic;

import java.util.Iterator;

/**
 * @author Ravi Mohan
 * 
 */
public class Percept extends ObjectWithDynamicAttributes {
	public Percept() {

	}

	public Percept(Object key1, Object value1) {
		setAttribute(key1, value1);
	}

	public Percept(Object key1, Object value1, Object key2, Object value2) {
		setAttribute(key1, value1);
		setAttribute(key2, value2);
	}

	public Percept(Object[] keys, Object[] values) {
		assert (keys.length == values.length);

		for (int i = 0; i < keys.length; i++) {
			setAttribute(keys[i], values[i]);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Percept)) {
			return super.equals(o);
		}
		return (toString().equals(((Percept) o).toString()));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		boolean first = true;
		Iterator<Object> keys = getSortedAttributeKeys();
		while (keys.hasNext()) {
			Object key = keys.next();

			if (first) {
				sb.append("[");
				first = false;
			}

			sb.append(key);
			sb.append("==");
			sb.append(getAttribute(key));
			if (keys.hasNext()) {
				sb.append(", ");
			} else {
				sb.append("]");
			}
		}

		return sb.toString();
	}
}