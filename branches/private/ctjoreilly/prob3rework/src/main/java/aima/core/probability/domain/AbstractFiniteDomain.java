package aima.core.probability.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractFiniteDomain implements FiniteDomain {

	private String toString = null;
	private Map<Object, Integer> valueToIdx = new HashMap<Object, Integer>();
	private Map<Integer, Object> idxToValue = new HashMap<Integer, Object>();

	public AbstractFiniteDomain() {

	}

	//
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
	//

	//
	// START-FiniteDomain
	@Override
	public abstract Set<? extends Object> getPossibleValues();

	@Override
	public int getOffset(Object value) {
		Integer idx = valueToIdx.get(value);
		if (null == idx) {
			throw new IllegalArgumentException("Value [" + value
					+ "] is not a possible value of this domain.");
		}
		return idx.intValue();
	}

	@Override
	public Object getValueAt(int offset) {
		return idxToValue.get(offset);
	}

	// END-FiniteDomain
	//

	@Override
	public String toString() {
		if (null == toString) {
			toString = getPossibleValues().toString();
		}
		return toString;
	}

	//
	// PROTECTED METHODS
	//
	protected void indexPossibleValues(Set<? extends Object> possibleValues) {
		int idx = 0;
		for (Object value : possibleValues) {
			valueToIdx.put(value, idx);
			idxToValue.put(idx, value);
			idx++;
		}
	}
}
