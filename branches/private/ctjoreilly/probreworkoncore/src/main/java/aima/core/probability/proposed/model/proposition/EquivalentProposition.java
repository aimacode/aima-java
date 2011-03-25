package aima.core.probability.proposed.model.proposition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.probability.proposed.model.RandomVariable;

public class EquivalentProposition extends ConstraintProposition {

	private List<RandomVariable> equivVars = new ArrayList<RandomVariable>();
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
			equivVars.add(rv);
		}
	}

	//
	// START-Proposition
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		boolean holds = true;

		RandomVariable rvC, rvL = equivVars.get(0);
		for (int i = 1; i < equivVars.size(); i++) {
			rvC = equivVars.get(i);
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
			sb.append(getRandomVariable().getName());
			for (RandomVariable rv : equivVars) {
				sb.append(" = ");
				sb.append(rv);
			}
			toString = sb.toString();
		}
		return toString;
	}
}
