package aima.core.search.map;

import aima.core.agent.impl.DynamicAction;

public class MoveToAction extends DynamicAction {
	public static final String ATTRIBUTE_MOVE_TO_LOCATION = "location";
	public static final String ATTRIBUTE_DISTANCE         = "distance";
	
	public MoveToAction(String location, Double distance) {
		super("moveTo");
		setAttribute(ATTRIBUTE_MOVE_TO_LOCATION, location);
		setAttribute(ATTRIBUTE_DISTANCE, distance);
	}
	
	public String getToLocation() {
		return (String) getAttribute(ATTRIBUTE_MOVE_TO_LOCATION);
	}
	
	public Double getDistance() {
		return (Double) getAttribute(ATTRIBUTE_DISTANCE);
	}
 }
