package aima.core.agent.impl;

import aima.core.agent.EnvironmentState;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class DynamicEnvironmentState extends ObjectWithDynamicAttributes implements EnvironmentState {
	public DynamicEnvironmentState() {

	}
	
	public String getName() {
		return EnvironmentState.class.getSimpleName();
	}
}