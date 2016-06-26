package aima.core.environment.wumpusworld.action;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for Wumpus World Actions.
 * 
 * @author Anurag Rai
 * @author Ciaran O'Reilly
 *
 */
public class WWAction {
	public static final String ATTRIBUTE_NAME = "name";
	private Map<Object, Object> attributes = new LinkedHashMap<Object, Object>();

	public WWAction(String name) {
		this.setAttribute(ATTRIBUTE_NAME, name);
	}

	/**
	 * Returns the value of the name attribute.
	 * 
	 * @return the value of the name attribute.
	 */
	public String getName() {
		return (String) getAttribute(ATTRIBUTE_NAME);
	}

	public void setAttribute(Object key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribute(Object key) {
		return attributes.get(key);
	}

	public String describeType() {
		return getClass().getSimpleName();
	}

	public String describeAttributes() {
		StringBuilder sb = new StringBuilder();

		sb.append("[");
		boolean first = true;
		for (Object key : attributes.keySet()) {
			Object value = attributes.get(key);
			if (key.equals(ATTRIBUTE_NAME) && value.equals(describeType())) {
				continue; // skip this attribute if matches type
			}
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}

			sb.append(key);
			sb.append("=");
			sb.append(value);
		}
		sb.append("]");

		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return super.equals(o);
		}
		return attributes.equals(((WWAction) o).attributes);
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
