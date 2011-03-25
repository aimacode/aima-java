package aima.core.probability.proposed.model.domain;

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

	private Set<String> possibleValues = null;

	public ArbitraryTokenDomain(String... pValues) {
		// Keep consistent order
		possibleValues = new LinkedHashSet<String>();
		for (String v : pValues) {
			possibleValues.add(v);
		}
		// Ensure cannot be modified
		possibleValues = Collections.unmodifiableSet(possibleValues);
	}

	//
	// START-Domain

	public int size() {
		return possibleValues.size();
	}

	public boolean isOrdered() {
		return true;
	}

	// END-Domain
	//

	//
	// START-FiniteDomain
	public Set<String> getPossibleValues() {
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
