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
		return obj != null && getClass() == obj.getClass() && Arrays.equals(values, ((Domain) obj).values);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(values);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("{");
		for (Object value : values) {
			if (result.length() > 1)
				result.append(", ");
			result.append(value);
		}
		result.append("}");
		return result.toString();
	}
}