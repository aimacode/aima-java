package aima.core.search.csp;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import aima.core.util.ArrayIterator;

/**
 * A domain Di consists of a set of allowable values {v1, ... , vk} for the
 * corresponding variable Xi and defines a default order on those values. This
 * implementation guarantees, that domains are never changed after they have
 * been created. Domain reduction is implemented by replacement instead of
 * modification. So previous states can easily and safely be restored.
 * 
 * @author Ruediger Lunde
 */
public class Domain implements Iterable<Object> {

	private Object[] values;

	public Domain(List<?> values) {
		this.values = new Object[values.size()];
		for (int i = 0; i < values.size(); i++)
			this.values[i] = values.get(i);
	}

	public Domain(Object[] values) {
		this.values = Arrays.copyOf(values, values.length);
	}

	public int size() {
		return values.length;
	}

	public Object get(int index) {
		return values[index];
	}

	public boolean isEmpty() {
		return values.length == 0;
	}

	public boolean contains(Object value) {
		for (Object v : values)
			if (v.equals(value))
				return true;
		return false;
	}

	@Override
	public Iterator<Object> iterator() {
		return new ArrayIterator<Object>(values);
	}

	/** Not very efficient... */
	public List<Object> asList() {
		return Arrays.asList(values);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Domain) {
			Domain d = (Domain) obj;
			if (d.size() != values.length)
				return false;
			else
				for (int i = 0; i < values.length; i++)
					if (!values[i].equals(d.values[i]))
						return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 9; // arbitrary seed value
		int multiplier = 13; // arbitrary multiplier value
		for (Object value : values)
			hash = hash * multiplier + value.hashCode();
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("{");
		boolean comma = false;
		for (Object value : values) {
			if (comma)
				result.append(", ");
			result.append(value.toString());
			comma = true;
		}
		result.append("}");
		return result.toString();
	}
}