package aima.core.probability.proposition;

import java.util.Map;

import aima.core.probability.RandomVariable;
import aima.core.probability.domain.FiniteDomain;

public class SubsetProposition extends AbstractDerivedProposition {

	private FiniteDomain subsetDomain = null;
	private RandomVariable varSubsetOf = null;
	//
	private String toString = null;

	public SubsetProposition(String name, FiniteDomain subsetDomain,
			RandomVariable ofVar) {
		super(name);

		if (null == subsetDomain) {
			throw new IllegalArgumentException("Sum Domain must be specified.");
		}
		this.subsetDomain = subsetDomain;
		this.varSubsetOf = ofVar;
		addScope(this.varSubsetOf);
	}

	//
	// START-Proposition
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		return subsetDomain.getPossibleValues().contains(
				possibleWorld.get(varSubsetOf));
	}

	// END-Proposition
	//

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(getDerivedName());
			sb.append(" = ");
			sb.append(subsetDomain.toString());
			toString = sb.toString();
		}
		return toString;
	}
}
