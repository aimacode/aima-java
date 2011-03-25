package aima.core.probability.proposed.model.domain;

public abstract class AbstractInfiniteDiscreteDomain implements FiniteDomain {

	//
	// START-Domain
	public boolean isFinite() {
		return false;
	}
	
	public boolean isInfinite() {
		return true;
	}
	
	public int size() {
		throw new IllegalStateException("You cannot determine the size of an infinite domain");
	}
	
	public abstract boolean isOrdered();
	// END-Domain
	//
}