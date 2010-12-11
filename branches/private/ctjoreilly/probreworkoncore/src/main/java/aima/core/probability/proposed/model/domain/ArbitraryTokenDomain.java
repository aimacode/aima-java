package aima.core.probability.proposed.model.domain;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ArbitraryTokenDomain {

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
	// START-DiscreteDomain
	public Set<String> getPossibleValues() {
		return possibleValues;
	}

	// END-DiscreteDomain
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
