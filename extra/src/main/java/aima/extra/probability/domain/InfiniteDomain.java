package aima.extra.probability.domain;

/**
 * A Domain over an infinite set of discrete values or a continuous range.
 * 
 * @author Nagaraj Poti
 */
public interface InfiniteDomain extends Domain {

	/**
	 * Test whether a value is a part of this domain or not.
	 * 
	 * @return true if the value is within the domain, false otherwise.
	 */
	boolean test(Object value);
}
