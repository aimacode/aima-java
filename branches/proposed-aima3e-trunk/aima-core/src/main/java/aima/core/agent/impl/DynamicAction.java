package aima.core.agent.impl;

import aima.core.agent.Action;

/**
 * @author Ciaran O'Reilly
 */
public class DynamicAction extends ObjectWithDynamicAttributes implements Action {
	public static final String ATTRIBUTE_NAME = "name";
	//
	
	public DynamicAction(String name) {
		this.setAttribute(ATTRIBUTE_NAME, name);
	}
	
	//
	// START-Action
	public boolean isNoOp() {
		return false;
	}
	// END-Action
	//
	
	public String getName() {
		return Action.class.getSimpleName();
	}
}