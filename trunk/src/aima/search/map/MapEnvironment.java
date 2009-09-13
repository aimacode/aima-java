package aima.search.map;

import aima.basic.Agent;
import aima.basic.Environment;
import aima.basic.Percept;

/**
 * Represents the environment a MapAgent can navigate.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapEnvironment extends Environment {

	private Map aMap = null;

	public MapEnvironment(Map aMap) {
		this.aMap = aMap;
	}

	public void addAgent(Agent a, String startLocation) {
		super.addAgent(a);
		a.setAttribute(DynAttributeNames.AGENT_LOCATION, startLocation);
		a.setAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE, 0);
	}

	@Override
	public void executeAction(Agent a, String act) {
		String currLoc = (String) a
				.getAttribute(DynAttributeNames.AGENT_LOCATION);
		Integer distance = aMap.getDistance(currLoc, act);
		if (distance != null) {
			int currTD = (Integer) a
					.getAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE);
			a.setAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE, currTD
					+ distance);
			a.setAttribute(DynAttributeNames.AGENT_LOCATION, act);
		}
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		return new Percept(DynAttributeNames.PERCEPT_IN, anAgent
				.getAttribute(DynAttributeNames.AGENT_LOCATION));
	}

	public Map getMap() {
		return aMap;
	}

	public String randomlySelectDestination() {
		return aMap.randomlyGenerateDestination();
	}
}
