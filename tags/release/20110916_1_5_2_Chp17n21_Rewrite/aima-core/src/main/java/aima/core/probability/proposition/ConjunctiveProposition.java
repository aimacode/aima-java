package aima.core.probability.proposition;

import java.util.Map;

import aima.core.probability.RandomVariable;

public class ConjunctiveProposition extends AbstractProposition implements
		BinarySentenceProposition {

	private Proposition left = null;
	private Proposition right = null;
	//
	private String toString = null;

	public ConjunctiveProposition(Proposition left, Proposition right) {
		if (null == left) {
			throw new IllegalArgumentException(
					"Left side of conjunction must be specified.");
		}
		if (null == right) {
			throw new IllegalArgumentException(
					"Right side of conjunction must be specified.");
		}
		// Track nested scope
		addScope(left.getScope());
		addScope(right.getScope());
		addUnboundScope(left.getUnboundScope());
		addUnboundScope(right.getUnboundScope());

		this.left = left;
		this.right = right;
	}

	@Override
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		return left.holds(possibleWorld) && right.holds(possibleWorld);
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(left.toString());
			sb.append(" AND ");
			sb.append(right.toString());
			sb.append(")");

			toString = sb.toString();
		}

		return toString;
	}
}
