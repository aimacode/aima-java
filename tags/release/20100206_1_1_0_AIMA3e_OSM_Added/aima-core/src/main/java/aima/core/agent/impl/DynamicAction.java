package aima.core.agent.impl;

import aima.core.agent.Action;

/**
 * @author Ciaran O'Reilly
 */
public class DynamicAction extends ObjectWithDynamicAttributes implements
		Action {
	public static final String ATTRIBUTE_NAME = "name";

	//

	public DynamicAction(String name) {
		this.setAttribute(ATTRIBUTE_NAME, name);
	}

	public String getName() {
		return (String) getAttribute(ATTRIBUTE_NAME);
	}

	//
	// START-Action
	public boolean isNoOp() {
		return false;
	}

	// END-Action
	//

	@Override
	public String describeType() {
		return Action.class.getSimpleName();
	}
}