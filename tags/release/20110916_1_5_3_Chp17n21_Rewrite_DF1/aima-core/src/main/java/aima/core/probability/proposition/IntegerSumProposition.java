package aima.core.probability.proposition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.probability.RandomVariable;
import aima.core.probability.domain.FiniteIntegerDomain;

public class IntegerSumProposition extends AbstractDerivedProposition {

	private FiniteIntegerDomain sumsDomain = null;
	private List<RandomVariable> sumVars = new ArrayList<RandomVariable>();
	//
	private String toString = null;

	public IntegerSumProposition(String name, FiniteIntegerDomain sumsDomain,
			RandomVariable... sums) {
		super(name);

		if (null == sumsDomain) {
			throw new IllegalArgumentException("Sum Domain must be specified.");
		}
		if (null == sums || 0 == sums.length) {
			throw new IllegalArgumentException(
					"Sum variables must be specified.");
		}
		this.sumsDomain = sumsDomain;
		for (RandomVariable rv : sums) {
			addScope(rv);
			sumVars.add(rv);
		}
	}

	//
	// START-Proposition
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		Integer sum = new Integer(0);

		for (RandomVariable rv : sumVars) {
			Object o = possibleWorld.get(rv);
			if (o instanceof Integer) {
				sum += ((Integer) o);
			} else {
				throw new IllegalArgumentException(
						"Possible World does not contain a Integer value for the sum variable:"
								+ rv);
			}
		}

		return sumsDomain.getPossibleValues().contains(sum);
	}

	// END-Proposition
	//

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(getDerivedName());
			sb.append(" = ");
			sb.append(sumsDomain.toString());
			toString = sb.toString();
		}
		return toString;
	}
}
