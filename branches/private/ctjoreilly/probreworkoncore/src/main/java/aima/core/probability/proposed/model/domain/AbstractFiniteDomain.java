package aima.core.probability.proposed.model.domain;

import java.util.Set;

public abstract class AbstractFiniteDomain implements FiniteDomain {

	private String toString = null;
	
	//
	// START-Domain
	public boolean isFinite() {
		return true;
	}
	
	public boolean isInfinite() {
		return false;
	}
	
	public abstract int size();
	
	public abstract boolean isOrdered();
	// END-Domain
	//
	
	//
	// START-DiscreteDomain
	public abstract Set<? extends Object> getPossibleValues();
	
	// END-DiscreteDomain
	//
	
	@Override
	public String toString() {
		if (null == toString) {
			toString = getPossibleValues().toString();
		}
		return toString;
	}
}
