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

	// PUBLIC

	/**
	 * Constructor accepting unordered list of domain values.
	 * 
	 * @param pValues
	 */
	public FiniteArbitraryTokenDomain(List<Object> pValues) {
		this(false, pValues);
	}

	/**
	 * Constructor accepting list of domain values.
	 * 
	 * @param ordered
	 *            true if pValues is an ordered list, false otherwise.
	 * 
	 * @param pValues
	 *            is the list of all possible values that the domain can take
	 *            on.
	 */
	public FiniteArbitraryTokenDomain(boolean ordered, List<Object> pValues) {
		this.ordered = ordered;
		possibleValues = new LinkedHashSet<Object>();
		pValues.forEach((value) -> possibleValues.add(value));
		indexPossibleValues(possibleValues);
	}

	@Override
	public int size() {
		return possibleValues.size();
	}

	@Override
	public boolean isOrdered() {
		return ordered;
	}

	@Override
	public Set<Object> getPossibleValues() {
		return Collections.unmodifiableSet(possibleValues);
	}
}
