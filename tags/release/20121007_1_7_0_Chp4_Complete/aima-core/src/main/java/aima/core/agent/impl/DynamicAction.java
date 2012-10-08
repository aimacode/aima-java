package aima.core.agent.impl;

import aima.core.agent.Action;

/**
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class DynamicAction extends ObjectWithDynamicAttributes implements
		Action {
	public static final String ATTRIBUTE_NAME = "name";

	//

	public DynamicAction(String name) {
		this.setAttribute(ATTRIBUTE_NAME, name);
	}

	/**
	 * Returns the value of the name attribute.
	 * 
	 * @return the value of the name attribute.
	 */
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