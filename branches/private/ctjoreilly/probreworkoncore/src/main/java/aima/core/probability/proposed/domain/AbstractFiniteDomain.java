package aima.core.probability.proposed.domain;

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
	// START-FiniteDomain
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
