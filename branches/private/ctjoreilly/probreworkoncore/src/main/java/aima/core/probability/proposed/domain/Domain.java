package aima.core.probability.proposed.domain;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 486.
 * 
 * Every random variable has a <b>domain</b> - the set of possible values it can
 * take on. The domain of <i>Total</i> for two dice is the set {2,...,12} and
 * the domain of Die<sub>1</sub> is {1,...,6}. A Boolean random variable has the
 * domain {true,false}.
 * 
 * @author Ciaran O'Reilly
 */
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
