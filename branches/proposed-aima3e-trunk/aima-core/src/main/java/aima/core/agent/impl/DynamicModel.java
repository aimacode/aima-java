package aima.core.agent.impl;

import aima.core.agent.Model;

/**
 * @author Ciaran O'Reilly
 */
public class DynamicModel extends ObjectWithDynamicAttributes implements Model {
	public DynamicModel() {

	}

	@Override
	public String describeType() {
		return Model.class.getSimpleName();
	}
}