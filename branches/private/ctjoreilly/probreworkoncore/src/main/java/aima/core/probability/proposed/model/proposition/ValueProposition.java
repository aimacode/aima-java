package aima.core.probability.proposed.model.proposition;

import java.util.Map;

import aima.core.probability.proposed.model.RandomVariable;

public class ValueProposition extends AbstractProposition {

	private RandomVariable forVariable = null;
	private Object value = null;
	//
	private String toString = null;
	
	public ValueProposition(RandomVariable forVariable, Object value) {
		if (null == forVariable) {
			throw new IllegalArgumentException(
					"The Random Variable for the value must be specified.");
		}
		if (null == value) {
			throw new IllegalArgumentException(
					"The value for the Random Variable must be specified.");
		}
		addScope(forVariable);
		this.forVariable = forVariable;
		this.value = value;
	}
	
	//
	// START-Proposition
	public boolean matches(Map<RandomVariable, Object> possibleWorld) {		
		return value.equals(possibleWorld.get(forVariable));
	}
	// END-Proposition
	//
	
	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(forVariable.getName());
			sb.append(" = ");
			sb.append(value);
			
			toString = sb.toString();
		}
		return toString;
	}
}