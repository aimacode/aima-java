package aima.core.agent.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public abstract class ObjectWithDynamicAttributes {
	private Map<Object, Object> attributes = new LinkedHashMap<Object, Object>();

	//
	// PUBLIC METHODS
	//

	/**
	 * By default, returns the simple name of the underlying class as given in
	 * the source code.
	 * 
	 * @return the simple name of the underlying class
	 */
	public String describeType() {
		return getClass().getSimpleName();
	}

	/**
	 * Returns a string representation of the object's current attributes
	 * 
	 * @return a string representation of the object's current attributes
	 */
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

	/**
	 * Returns an unmodifiable view of the object's key set
	 * 
	 * @return an unmodifiable view of the object's key set
	 */
	public Set<Object> getKeySet() {
		return Collections.unmodifiableSet(attributes.keySet());
	}

	/**
	 * Associates the specified value with the specified attribute key. If the
	 * ObjectWithDynamicAttributes previously contained a mapping for the
	 * attribute key, the old value is replaced.
	 * 
	 * @param key
	 *            the attribute key
	 * @param value
	 *            the attribute value
	 */
	public void setAttribute(Object key, Object value) {
		attributes.put(key, value);
	}

	/**
	 * Returns the value of the specified attribute key, or null if the
	 * attribute was not found.
	 * 
	 * @param key
	 *            the attribute key
	 * 
	 * @return the value of the specified attribute name, or null if not found.
	 */
	public Object getAttribute(Object key) {
		return attributes.get(key);
	}

	/**
	 * Removes the attribute with the specified key from this
	 * ObjectWithDynamicAttributes.
	 * 
	 * @param key
	 *            the attribute key
	 */
	public void removeAttribute(Object key) {
		attributes.remove(key);
	}

	/**
	 * Creates and returns a copy of this ObjectWithDynamicAttributes
	 */
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
