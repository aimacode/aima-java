package aima.core.environment.map;

import aima.core.agent.Agent;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicPercept;

/**
 * Represents the environment a SimpleMapAgent can navigate.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class MapEnvironment extends AbstractEnvironment<DynamicPercept, MoveToAction> {

	private Map map;
	private MapEnvironmentState state = new MapEnvironmentState();

	public MapEnvironment(Map map) {
		this.map = map;
	}

	public void addAgent(Agent<? super DynamicPercept, ? extends MoveToAction> agent, String startLocation) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		state.setAgentLocationAndTravelDistance(agent, startLocation, 0.0);
		super.addAgent(agent);
	}

	public String getAgentLocation(Agent<?, ?> agent) {
		return state.getAgentLocation(agent);
	}

	public Double getAgentTravelDistance(Agent<?, ?> agent) {
		return state.getAgentTravelDistance(agent);
	}

	@Override
	public void execute(Agent<?, ?> agent, MoveToAction action) {
		String currLoc = getAgentLocation(agent);
		Double distance = map.getDistance(currLoc, action.getToLocation());
		if (distance != null) {
			double currTD = getAgentTravelDistance(agent);
			state.setAgentLocationAndTravelDistance(agent,
					action.getToLocation(), currTD + distance);
		}
	}

	@Override
	public DynamicPercept getPerceptSeenBy(Agent<?, ?> agent) {
		return new DynamicPercept(AttNames.PERCEPT_IN,
				getAgentLocation(agent));
	}

	public Map getMap() {
		return map;
	}
}