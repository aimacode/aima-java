package aima.core.probability.domain;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 486.
 * 
 * As in CSPs, domains can be sets of arbitrary tokens; we might choose the
 * domain of <i>Age</i> to be {<i>juvenile,teen,adult</i>} and the domain of
 * <i>Weather</i> might be {<i>sunny,rain,cloudy,snow</i>}.
 * 
 * @author Ciaran O'Reilly
 */
public class ArbitraryTokenDomain extends AbstractFiniteDomain {

	private Set<Object> possibleValues = null;
	private boolean ordered = false;

	public ArbitraryTokenDomain(Object... pValues) {
		this(false, pValues);
	}

	public ArbitraryTokenDomain(boolean ordered, Object... pValues) {
		this.ordered = ordered;
		// Keep consistent order
		possibleValues = new LinkedHashSet<Object>();
		for (Object v : pValues) {
			possibleValues.add(v);
		}
		// Ensure cannot be modified
		possibleValues = Collections.unmodifiableSet(possibleValues);

		indexPossibleValues(possibleValues);
	}

	//
	// START-Domain

	@Override
	public int size() {
		return possibleValues.size();
	}

	@Override
	public boolean isOrdered() {
		return ordered;
	}

	// END-Domain
	//

	//
	// START-FiniteDomain
	@Override
	public Set<Object> getPossibleValues() {
		return possibleValues;
	}

	// END-finiteDomain
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof ArbitraryTokenDomain)) {
			return false;
		}

		ArbitraryTokenDomain other = (ArbitraryTokenDomain) o;

		return this.possibleValues.equals(other.possibleValues);
	}

	@Override
	public int hashCode() {
		return possibleValues.hashCode();
	}
}
