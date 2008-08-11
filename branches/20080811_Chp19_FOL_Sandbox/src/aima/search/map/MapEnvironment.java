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
	public static final String STATE_IN = "In";

	private static final String LOCATION = "location";

	private Map aMap = null;

	public MapEnvironment(Map aMap) {
		this.aMap = aMap;
	}

	public void addAgent(Agent a, String startLocation) {
		super.addAgent(a);
		a.setAttribute(LOCATION, startLocation);
	}

	@Override
	public void executeAction(Agent a, String act) {
		if (!Agent.NO_OP.equals(act)) {
			a.setAttribute(LOCATION, act);
		}
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		return new Percept(STATE_IN, anAgent.getAttribute(LOCATION));
	}

	public Map getMap() {
		return aMap;
	}

	public String randomlySelectDestination() {
		return aMap.randomlyGenerateDestination();
	}
}
