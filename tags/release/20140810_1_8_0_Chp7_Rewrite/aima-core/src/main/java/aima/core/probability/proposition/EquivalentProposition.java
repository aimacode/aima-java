package aima.core.probability.proposition;

import java.util.Iterator;
import java.util.Map;

import aima.core.probability.RandomVariable;

public class EquivalentProposition extends AbstractDerivedProposition {
	//
	private String toString = null;

	public EquivalentProposition(String name, RandomVariable... equivs) {
		super(name);

		if (null == equivs || 1 >= equivs.length) {
			throw new IllegalArgumentException(
					"Equivalent variables must be specified.");
		}
		for (RandomVariable rv : equivs) {
			addScope(rv);
		}
	}

	//
	// START-Proposition
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		boolean holds = true;

		Iterator<RandomVariable> i = getScope().iterator();
		RandomVariable rvC, rvL = i.next();
		while (i.hasNext()) {
			rvC = i.next();
			if (!possibleWorld.get(rvL).equals(possibleWorld.get(rvC))) {
				holds = false;
				break;
			}
			rvL = rvC;
		}

		return holds;
	}

	// END-Proposition
	//

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(getDerivedName());
			for (RandomVariable rv : getScope()) {
				sb.append(" = ");
				sb.append(rv);
			}
			toString = sb.toString();
		}
		return toString;
	}
}
