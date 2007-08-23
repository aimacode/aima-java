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

	private static final String LOCATION = "location";

	public MapEnvironment(Map aMap) {
		this.aMap = aMap;
	}

	public void addAgent(Agent a, String startLocation) {
		super.addAgent(a);
		a.setAttribute(LOCATION, startLocation);
	}

	public void executeAction(Agent a, String act) {
		if (!MapAgent.NO_OP.equals(act) && !MapAgent.DIE.equals(act)) {
			a.setAttribute(LOCATION, act);
		}
		if (MapAgent.DIE.equals(act)) {
			a.die();
		}
	}

	public Percept getPerceptSeenBy(Agent anAgent) {
		return new Percept("In", anAgent.getAttribute(LOCATION));
	}

	public Map getMap() {
		return aMap;
	}

	public String randomlySelectDestination() {
		return aMap.randomlyGenerateDestination();
	}
}
