package aima.basic;

/*
 * The base class of all agents .
 *  
 */
import java.util.ArrayList;
import java.util.List;

import aima.util.Calculator;

/**
 * @author Ravi Mohan
 * 
 */
public class XYEnvironment extends Environment {
	public static String LOCATION = "location";

	int width;

	int height;

	XYLocation defaultLocation;

	public XYEnvironment() {

	}

	public XYEnvironment(int width, int height) {

		this.width = width;
		this.height = height;
		this.defaultLocation = new XYLocation(1, 1);
	}

	public void addObject(EnvironmentObject o, XYLocation loc) {

		super.addObject(o, LOCATION, loc);
	}

	public void addAgent(Agent a, XYLocation loc) {
		super.addAgent(a, LOCATION, loc);
	}

	public void moveObjectToAbsoluteLocation(Agent a, XYLocation loc) {

		a.setAttribute(LOCATION, loc);

	}

	public void moveObject(Agent a, String direction) {
		XYLocation presentLocation = (XYLocation) a.getAttribute(LOCATION);
		XYLocation locationToMoveTo = presentLocation.locationAt(direction);

		if (!(isBlocked(locationToMoveTo))) {
			moveObjectToAbsoluteLocation(a, locationToMoveTo);
		}

	}

	public ArrayList<ObjectWithDynamicAttributes> getObjectsAt(XYLocation loc) {

		ArrayList<ObjectWithDynamicAttributes> retval = new ArrayList<ObjectWithDynamicAttributes>();

		List<ObjectWithDynamicAttributes> all = getAllObjects();
		for (ObjectWithDynamicAttributes obj : all) {
			XYLocation objLoc = (XYLocation) obj.getAttribute(LOCATION);
			if (objLoc.equals(loc)) {
				retval.add(obj);
			}

		}
		return retval;
	}

	public ArrayList getObjectsNear(Agent agent, int radius) {
		ArrayList<Object> retval = new ArrayList<Object>();

		XYLocation agentLocation = (XYLocation) agent.getAttribute(LOCATION);

		List<ObjectWithDynamicAttributes> all = getAllObjects();
		for (ObjectWithDynamicAttributes a : all) {
			if (!(a.equals(agent))) {
				XYLocation otherAgentLocation = (XYLocation) a
						.getAttribute(LOCATION);
				if (withinRadius(radius, agentLocation, otherAgentLocation)) {
					retval.add(a);
				}
			}
		}
		return retval;
	}

	private boolean withinRadius(int radius, XYLocation agentLocation,
			XYLocation objectLocation) {
		int dist = Calculator.calculateSquareOfDistanceBetweenLocations(
				agentLocation, objectLocation);
		int radiusSquared = radius * radius;
		boolean withinRadius = (dist <= radiusSquared);
		return withinRadius;
	}

	@Override
	public void executeAction(Agent a, String Action) {

	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		return new Percept();
	}

	public boolean isBlocked(XYLocation loc) {
		boolean retval = false;
		ArrayList objs = this.getObjectsAt(loc);

		for (Object o : objs) {
			if (o instanceof Wall) {
				retval = true;
			}
		}
		return retval;
	}

	public void makePerimeter() {
		for (int i = 0; i < width; i++) {
			XYLocation loc = new XYLocation(i, 0);
			XYLocation loc2 = new XYLocation(i, height - 1);
			addObject(new Wall(), loc);
			addObject(new Wall(), loc2);
		}

		for (int i = 0; i < height; i++) {
			XYLocation loc = new XYLocation(0, i);
			XYLocation loc2 = new XYLocation(width - 1, i);
			addObject(new Wall(), loc);
			addObject(new Wall(), loc2);
		}

	}

}