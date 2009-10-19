package aima.core.search.map;

import aima.core.agent.impl.DynamicAction;

public class MoveToAction extends DynamicAction {
	public static final String ATTRIBUTE_MOVE_TO_LOCATION = "location";
	public static final String ATTRIBUTE_DISTANCE         = "distance";
	//
	private String location = "";
	private Double distance = null;
	
	public MoveToAction(String location, Double distance) {
		super("moveTo");
		this.location = location;
		this.distance = distance;
		setAttribute(ATTRIBUTE_MOVE_TO_LOCATION, location);
		setAttribute(ATTRIBUTE_DISTANCE, distance);
	}
	
	public String getToLocation() {
		return location;
	}
	
	public Double getDistance() {
		return distance;
	}
 }
