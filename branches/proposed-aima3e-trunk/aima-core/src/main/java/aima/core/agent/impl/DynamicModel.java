package aima.core.agent.impl;

import aima.core.agent.Model;

/**
 * @author Ciaran O'Reilly
 */
public class DynamicModel extends ObjectWithDynamicAttributes implements Model {
	public DynamicModel() {

	}
	
	public String getName() {
		return Model.class.getSimpleName();
	}
}