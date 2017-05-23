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
public class Domain<VAL> implements Iterable<VAL> {

	private final Object[] values;

	public Domain(List<VAL> values) {
		this.values = values.toArray();
	}

	@SafeVarargs
	public Domain(VAL... values) {
		this.values = values;
	}

	public int size() {
		return values.length;
	}

	@SuppressWarnings("unchecked")
	public VAL get(int index) {
		return (VAL) values[index];
	}

	public boolean isEmpty() {
		return values.length == 0;
	}

	public boolean contains(VAL value) {
		for (Object v : values)
			if (value.equals(v))
				return true;
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<VAL> iterator() {
		return new ArrayIterator<>((VAL[]) values);
	}

	/** Not very efficient... */
	@SuppressWarnings("unchecked")
	public List<VAL> asList() {
		return Arrays.asList((VAL[]) values);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass() == obj.getClass()) {
			Domain d = (Domain) obj;
			if (d.values.length != values.length)
				return false;
			for (int i = 0; i < values.length; i++)
				if (!values[i].equals(d.values[i]))
					return false;
			return true;
		}
		return false;
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