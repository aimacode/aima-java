package aima.extra.probability.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * As in CSPs, domains can be sets of arbitrary tokens; we might choose the
 * domain of <i>Age</i> to be {<i>juvenile,teen,adult</i>} and the domain of
 * <i>Weather</i> might be {<i>sunny,rain,cloudy,snow</i>}.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class FiniteArbitraryTokenDomain extends AbstractFiniteDomain {

	/**
	 * Set of all possible values for the domain.
	 */
	private Set<Object> possibleValues = null;

	private boolean ordered = false;

	// Public constructors

	/**
	 * Constructor accepting unordered array of domain values.
	 * 
	 * @param dValues
	 */
	public FiniteArbitraryTokenDomain(Object... dValues) {
		this(false, Arrays.asList(dValues));
	}
	
	/**
	 * Constructor accepting unordered list of domain values.
	 * 
	 * @param dValues
	 */
	public FiniteArbitraryTokenDomain(List<?> dValues) {
		this(false, dValues);
	}

	/**
	 * Constructor accepting set of domain values.
	 * 
	 * @param ordered
	 *            true if dValues is an ordered array, false otherwise.
	 * @param dValues
	 *            is the array of all possible values that the domain can take
	 *            on.
	 */
	public FiniteArbitraryTokenDomain(boolean ordered, Object... dValues) {
		this(ordered, Arrays.asList(dValues));
	}
	
	/**
	 * Constructor accepting set of domain values.
	 * 
	 * @param ordered
	 *            true if dValues is an ordered list, false otherwise.
	 * @param dValues
	 *            is the list of all possible values that the domain can take
	 *            on.
	 */
	public FiniteArbitraryTokenDomain(boolean ordered, List<?> dValues) {
		this.ordered = ordered;
		this.possibleValues = new LinkedHashSet<Object>();
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

	@Override
	public boolean isOrdered() {
		return this.ordered;
	}
	
	// END-Domain

	// START-FiniteDomain
	
	@Override
	public Set<?> getPossibleValues() {
		return this.possibleValues;
	}
	
	// END-FiniteDomain
}
