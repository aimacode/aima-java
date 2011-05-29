package aima.core.probability.proposed.proposition;

import java.util.Map;

import aima.core.probability.proposed.RandomVariable;

public class AssignmentProposition extends AbstractTermProposition {
	private Object value = null;
	//
	private String toString = null;

	public AssignmentProposition(RandomVariable forVariable, Object value) {
		super(forVariable);
		if (null == value) {
			throw new IllegalArgumentException(
					"The value for the Random Variable must be specified.");
		}

		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		return value.equals(possibleWorld.get(getTermVariable()));
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(getTermVariable().getName());
			sb.append(" = ");
			sb.append(value);

			toString = sb.toString();
		}
		return toString;
	}
}
