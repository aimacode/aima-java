package aima.core.probability.proposed.domain;

import java.util.Set;

public abstract class AbstractFiniteDomain implements FiniteDomain {

	private String toString = null;

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

	// END-FiniteDomain
	//

	@Override
	public String toString() {
		if (null == toString) {
			toString = getPossibleValues().toString();
		}
		return toString;
	}
}
