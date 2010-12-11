package aima.core.probability.proposed.model.domain;

// TODO - documentation pg. 486 AIMA3e.
public interface Domain {

	/**
	 * 
	 * @return true if the Domain is finite, false otherwise (i.e. discrete
	 *         (like the integers) or continuous (like the reals)).
	 */
	boolean isFinite();

	/**
	 * 
	 * @return !isFinite().
	 */
	boolean isInfinite();

	/**
	 * 
	 * @return the size of the Domain, only applicable if isFinite() == true.
	 */
	int size();

	/**
	 * 
	 * @return true if the domain is ordered, false otherwise.
	 */
	boolean isOrdered();
}
