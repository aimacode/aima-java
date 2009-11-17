package aima.core.environment.map;

import aima.core.agent.impl.DynamicAction;

public class MoveToAction extends DynamicAction {
	public static final String ATTRIBUTE_MOVE_TO_LOCATION = "location";

	public MoveToAction(String location) {
		super("moveTo");
		setAttribute(ATTRIBUTE_MOVE_TO_LOCATION, location);
	}

	public String getToLocation() {
		return (String) getAttribute(ATTRIBUTE_MOVE_TO_LOCATION);
	}
}
