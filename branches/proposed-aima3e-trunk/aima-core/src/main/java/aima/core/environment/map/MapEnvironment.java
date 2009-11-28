package aima.core.environment.map;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicPercept;

/**
 * Represents the environment a MapAgent can navigate.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapEnvironment extends AbstractEnvironment {

	private Map aMap = null;
	private MapEnvironmentState state = new MapEnvironmentState();

	public MapEnvironment(Map aMap) {
		this.aMap = aMap;
	}

	public void addAgent(Agent a, String startLocation) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		state.setAgentLocationAndTravelDistance(a, startLocation, 0.0);
		super.addAgent(a);
	}

	public String getAgentLocation(Agent a) {
		return state.getAgentLocation(a);
	}

	public Double getAgentTravelDistance(Agent a) {
		return state.getAgentTravelDistance(a);
	}

	@Override
	public EnvironmentState getCurrentState() {
		return state;
	}

	@Override
	public EnvironmentState executeAction(Agent agent, Action a) {

		if (!a.isNoOp()) {
			MoveToAction act = (MoveToAction) a;

			String currLoc = getAgentLocation(agent);
			Double distance = aMap.getDistance(currLoc, act.getToLocation());
			if (distance != null) {
				double currTD = getAgentTravelDistance(agent);
				state.setAgentLocationAndTravelDistance(agent, act
						.getToLocation(), currTD + distance);
			}
		}

		return state;
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		return new DynamicPercept(DynAttributeNames.PERCEPT_IN,
				getAgentLocation(anAgent));
	}

	public Map getMap() {
		return aMap;
	}
}