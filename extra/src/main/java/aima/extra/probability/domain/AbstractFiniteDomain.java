package aima.extra.probability.domain;

import aima.extra.util.datastructure.BiMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract base class of FiniteDomain implementations.
 * 
 * @author Nagaraj Poti
 */
public abstract class AbstractFiniteDomain implements FiniteDomain {

	// Class members

	/**
	 * Mapping of domain values to their respective indices in subclass
	 * datastructures.
	 */
	private BiMap<Object, Integer> valueToIdx = new BiMap<Object, Integer>();

	// PUBLIC

	// DiscreteDomain methods

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

	// FiniteDomain methods

	@Override
	public abstract Set<?> getPossibleValues();

	@Override
	public int getOffset(Object value) {
		Integer idx = valueToIdx.getForward(value);
		if (null == idx) {
			throw new IllegalArgumentException("Value [" + value + "] is not a legal value for this domain.");
		}
		return idx.intValue();
	}

	@Override
	public Object getValueAt(int offset) {
		return valueToIdx.getBackward(offset);
	}

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
		if (!(o instanceof AbstractFiniteDomain)) {
			return false;
		}
		AbstractFiniteDomain other = (AbstractFiniteDomain) o;
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

	protected void indexPossibleValues(Set<?> possibleValues) {
		final AtomicInteger count = new AtomicInteger(0);
		possibleValues.forEach((value) -> valueToIdx.put(value, count.incrementAndGet()));
	}
}
