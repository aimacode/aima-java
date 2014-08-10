package aima.core.probability.proposition;

import java.util.Map;

import aima.core.probability.RandomVariable;

public class NotProposition extends AbstractProposition implements
		UnarySentenceProposition {

	private Proposition proposition;
	//
	private String toString = null;

	public NotProposition(Proposition prop) {
		if (null == prop) {
			throw new IllegalArgumentException(
					"Proposition to be negated must be specified.");
		}
		// Track nested scope
		addScope(prop.getScope());
		addUnboundScope(prop.getUnboundScope());

		proposition = prop;
	}

	@Override
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		return !proposition.holds(possibleWorld);
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("(NOT ");
			sb.append(proposition.toString());
			sb.append(")");

			toString = sb.toString();
		}
		return toString;
	}
}
