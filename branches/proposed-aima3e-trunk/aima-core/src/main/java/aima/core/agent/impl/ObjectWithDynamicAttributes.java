package aima.core.agent.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public abstract class ObjectWithDynamicAttributes {
	private Map<Object, Object> attributes = new LinkedHashMap<Object, Object>();

	//
	// PUBLIC METHODS
	//
	public String describeType() {
		return getClass().getSimpleName();
	}

	public String describeAttributes() {
		StringBuilder sb = new StringBuilder();

		sb.append("[");
		boolean first = true;
		for (Object key : attributes.keySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}

			sb.append(key);
			sb.append("==");
			sb.append(attributes.get(key));
		}
		sb.append("]");

		return sb.toString();
	}

	public Set<Object> getKeySet() {
		return Collections.unmodifiableSet(attributes.keySet());
	}

	public void setAttribute(Object key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribute(Object key) {
		return attributes.get(key);
	}

	public void removeAttribute(Object key) {
		attributes.remove(key);
	}

	public ObjectWithDynamicAttributes copy() {
		ObjectWithDynamicAttributes copy = null;

		try {
			copy = getClass().newInstance();
			copy.attributes.putAll(attributes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return copy;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return super.equals(o);
		}
		return attributes.equals(((ObjectWithDynamicAttributes) o).attributes);
	}

	@Override
	public int hashCode() {
		return attributes.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(describeType());
		sb.append(describeAttributes());

		return sb.toString();
	}
}
