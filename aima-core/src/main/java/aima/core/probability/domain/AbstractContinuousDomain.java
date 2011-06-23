package aima.core.probability.domain;

public abstract class AbstractContinuousDomain implements ContinuousDomain {

	//
	// START-Domain
	@Override
	public boolean isFinite() {
		return false;
	}

	@Override
	public boolean isInfinite() {
		return true;
	}

	@Override
	public int size() {
		throw new IllegalStateException(
				"You cannot determine the size of an infinite domain");
	}

	@Override
	public abstract boolean isOrdered();
	// END-Domain
	//
}
