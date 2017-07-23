package aima.extra.probability.domain;

import aima.extra.util.datastructure.BiMap;
import java.util.Set;

/**
 * Abstract base class of FiniteDomain implementations.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public abstract class AbstractFiniteDomain implements FiniteDomain {

	// Class members

	/**
	 * Mapping of domain values to their respective indices in subclass
	 * datastructures.
	 */
	private BiMap<Object, Integer> valueToIdx = new BiMap<Object, Integer>();

	// Public methods

	// START-Domain

	@Override
	public boolean isFinite() {
		return true;
	}

	@Override
	public boolean isInfinite() {
		return false;
	}

	// END-Domain

	// START-FiniteDomain

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
		Object value = valueToIdx.getBackward(offset);
		if (null == value) {
			throw new IllegalArgumentException("Offset index [" + offset + "] is not valid.");
		}
		return value;
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
		if (o == this) {
			return true;
		}
		else if (!(o instanceof AbstractFiniteDomain)) {
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
	
	/**
	 * @return hashCode() of the underlying set structure. 
	 */
	@Override
	public int hashCode() {
		return this.getPossibleValues().hashCode();
	}

	// Protected methods

	protected void indexPossibleValues(Set<?> possibleValues) {
		possibleValues.forEach((value) -> valueToIdx.put(value, valueToIdx.size()));
	}
}
