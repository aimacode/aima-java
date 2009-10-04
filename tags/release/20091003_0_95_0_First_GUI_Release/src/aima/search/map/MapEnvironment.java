package aima.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.basic.Agent;
import aima.basic.Environment;
import aima.basic.Percept;
import aima.util.Util;

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
		a.setAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE, 0.0);
	}

	@Override
	public void executeAction(Agent a, String act) {
		String currLoc = (String) a
				.getAttribute(DynAttributeNames.AGENT_LOCATION);
		Double distance = aMap.getDistance(currLoc, act);
		if (distance != null) {
			double currTD = (Double) a
					.getAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE);
			a.setAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE, currTD
					+ distance);
			a.setAttribute(DynAttributeNames.AGENT_LOCATION, act);
		}
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		String currLoc = (String) anAgent.getAttribute(
				DynAttributeNames.AGENT_LOCATION);
		List<Object> possibleActions = new ArrayList<Object>();
		for (String a : aMap.getLocationsLinkedTo(currLoc)) {
			possibleActions.add(a);
			possibleActions.add(new Double(aMap.getDistance(currLoc, a)));
		}
		return new Percept(DynAttributeNames.PERCEPT_IN, currLoc,
				DynAttributeNames.PERCEPT_POSSIBLE_ACTIONS, possibleActions);
	}

	public Map getMap() {
		return aMap;
	}
}
