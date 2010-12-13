package aima.core.probability.proposed.model.proposition;

import java.util.Map;

import aima.core.probability.proposed.model.RandomVariable;

public class NotProposition extends UnarySentenceProposition {

	private Proposition notProp;
	//
	private String toString = null;
	
	public NotProposition(Proposition prop) {
		if (null == prop) {
			throw new IllegalArgumentException("Proposition to be negated must be specified.");
		}
		notProp = prop;
	}
	
	@Override
	public boolean matches(Map<RandomVariable, Object> possibleWorld) {
		return !notProp.matches(possibleWorld);
	}
	
	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("(NOT ");
			sb.append(notProp.toString());
			sb.append(")");
			
			toString = sb.toString();
		}
		return toString;
	}
}
