package aima.core.probability.proposed.domain;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class FiniteIntegerDomain extends AbstractFiniteDomain {

	private Set<Integer> possibleValues = null;

	public FiniteIntegerDomain(Integer... pValues) {
		// Keep consistent order
		possibleValues = new LinkedHashSet<Integer>();
		for (Integer v : pValues) {
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
	public Set<Integer> getPossibleValues() {
		return possibleValues;
	}

	// END-DiscreteDomain
	//
	
	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof FiniteIntegerDomain)) {
			return false;
		}

		FiniteIntegerDomain other = (FiniteIntegerDomain) o;

		return this.possibleValues.equals(other.possibleValues);
	}
	
	@Override
	public int hashCode() {
		return possibleValues.hashCode();
	}
}
