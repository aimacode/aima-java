package aima.extra.probability.domain;

import aima.extra.util.datastructure.BiMap;
import java.util.Set;

/**
 * Abstract base class of FiniteDomain implementations.
 * 
 * @param <T>
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public abstract class AbstractFiniteDomain<T> implements FiniteDomain<T> {

	// Class members

	/**
	 * Mapping of domain values to their respective indices in subclass
	 * datastructures.
	 */
	private BiMap<T, Integer> valueToIdx = new BiMap<T, Integer>();

	// PUBLIC

	// START-Domain

	@Override
	public boolean isFinite() {
		return true;
	}

	@Override
	public boolean isInfinite() {
		return false;
	}

	@Override
	public abstract int size();

	@Override
	public abstract boolean isOrdered();

	// END-Domain

	// START-FiniteDomain

	@Override
	public abstract Set<T> getPossibleValues();

	@Override
	public int getOffset(T value) {
		Integer idx = valueToIdx.getForward(value);
		if (null == idx) {
			throw new IllegalArgumentException("Value [" + value + "] is not a legal value for this domain.");
		}
		return idx.intValue();
	}

	@Override
	public T getValueAt(int offset) {
		return valueToIdx.getBackward(offset);
	}

	// END-FiniteDomain

	/**
	 * Check for equality of AbstractFiniteDomain instances. Two domains are
	 * equal if they have the same values (and are in the same order if they are
	 * both ordered). Domains that have the same values and are both ordered,
	 * but with different orderings specified, are different.
	 * 
	 * @return true if both domains are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AbstractFiniteDomain<?>)) {
			return false;
		}
		AbstractFiniteDomain<?> other = (AbstractFiniteDomain<?>) o;
		if (this.isOrdered() != other.isOrdered()) {
			return false;
		} else if (this.isOrdered() == other.isOrdered() && this.isOrdered() == false) {
			return this.getPossibleValues().equals(other.getPossibleValues());
		} else {
			return this.valueToIdx.equals(other.valueToIdx);
		}
	}
	
	@Override
	public int hashCode() {
		return this.getPossibleValues().hashCode();
	}

	// PROTECTED

	protected void indexPossibleValues(Set<T> possibleValues) {
		possibleValues.forEach((value) -> valueToIdx.put(value, valueToIdx.size()));
	}
}
