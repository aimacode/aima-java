package aima.core.agent.impl.aprog.simplerule;

import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 * Base abstract class for describing conditions.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public abstract class Condition {
	public abstract boolean evaluate(ObjectWithDynamicAttributes p);

	@Override
	public boolean equals(Object o) {
		return o != null && getClass() == o.getClass() && toString().equals(o.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
