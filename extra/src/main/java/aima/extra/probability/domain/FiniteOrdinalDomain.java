package aima.extra.probability.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
public class FiniteOrdinalDomain<T extends Comparable<T>> extends AbstractFiniteDomain {

	// Class members
	
	/**
	 * Set of all possible values for the domain.
	 */
	private Set<T> possibleValues = null;

	// Public constructor
	
	/**
	 * Instantiates possibleValues with all possible domain values for this
	 * domain. The ordering of elements is consistent. TreeSet orders the
	 * elements according to supplied Comparator. If no comparator is supplied,
	 * elements will be placed in their natural ascending order.
	 * 
	 * @param dValues
	 *            is the array of all possible values that the domain can take
	 *            on.
	 */
	public FiniteOrdinalDomain(T[] dValues) {
		this(Arrays.asList(dValues));
	}
	
	/**
	 * Instantiates possibleValues with all possible domain values for this
	 * domain. The ordering of elements is consistent. TreeSet orders the
	 * elements according to supplied Comparator. If no comparator is supplied,
	 * elements will be placed in their natural ascending order.
	 * 
	 * @param dValues
	 *            is the list of all possible values that the domain can take
	 *            on.
	 */
	public FiniteOrdinalDomain(List<T> dValues) {
		Objects.requireNonNull(dValues, "Atleast one value must be specified.");
		if (dValues.size() == 0) {
			throw new IllegalArgumentException("At least one value must be specified.");
		}
		this.possibleValues = new TreeSet<T>();
		dValues.forEach((value) -> this.possibleValues.add(value));
		this.possibleValues = Collections.unmodifiableSet(this.possibleValues);
		indexPossibleValues(this.possibleValues);
	}

	// Public methods
	
	// START-Domain
	
	@Override
	public int size() {
		return this.possibleValues.size();
	}

	/**
	 * A FiniteComparableDomain is ordered.
	 */
	@Override
	public boolean isOrdered() {
		return true;
	}
	
	// END-Domain
	
	// START-FiniteDomain

	@Override
	public Set<T> getPossibleValues() {
		return this.possibleValues;
	}
	
	// END-FiniteDomain
}
