package aima.extra.probability.domain;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;

/**
 * Implementation for finite ordered domains whose values are comparable (i.e
 * extend the Comparable interface).
 * 
 * @param <T>
 * 
 * @author Nagaraj Poti
 */
public class FiniteComparableDomain<T extends Comparable<T>> extends AbstractFiniteDomain<T> {

	// Class members
	
	/**
	 * Set of all possible values for the domain.
	 */
	private Set<T> possibleValues = null;

	// PUBLIC
	
	/**
	 * Instantiates possibleValues with all possible domain values for this
	 * domain. The ordering of elements is consistent. TreeSet orders the
	 * elements according to supplied Comparator. If no comparator is supplied,
	 * elements will be placed in their natural ascending order.
	 * 
	 * @param pValues
	 *            is the list of all possible values that the domain can take
	 *            on.
	 */
	public FiniteComparableDomain(List<T> pValues) {
		possibleValues = new TreeSet<T>();
		pValues.forEach((value) -> possibleValues.add(value));
		possibleValues = Collections.unmodifiableSet(possibleValues);
		indexPossibleValues(possibleValues);
	}

	@Override
	public int size() {
		return possibleValues.size();
	}

	/**
	 * A FiniteComparableDomain is ordered.
	 */
	@Override
	public boolean isOrdered() {
		return true;
	}

	@Override
	public Set<T> getPossibleValues() {
		return possibleValues;
	}
}
