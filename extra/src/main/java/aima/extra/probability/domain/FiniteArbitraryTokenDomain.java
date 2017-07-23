package aima.extra.probability.domain;

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
	 * Constructor accepting unordered set of domain values.
	 * 
	 * @param pValues
	 */
	public FiniteArbitraryTokenDomain(List<?> pValues) {
		this(false, pValues);
	}

	/**
	 * Constructor accepting set of domain values.
	 * 
	 * @param ordered
	 *            true if pValues is an ordered list, false otherwise.
	 * @param pValues
	 *            is the list of all possible values that the domain can take
	 *            on.
	 */
	public FiniteArbitraryTokenDomain(boolean ordered, List<?> pValues) {
		this.ordered = ordered;
		this.possibleValues = new LinkedHashSet<Object>();
		pValues.forEach((value) -> this.possibleValues.add(value));
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
