package aima.core.environment.map;

import java.util.Optional;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicPercept;

/**
 * Represents the environment a MapAgent can navigate.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class MapEnvironment<A,P> extends AbstractEnvironment<A,P> {

	private Map map = null;
	private MapEnvironmentState state = new MapEnvironmentState();

	public MapEnvironment(Map map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public void addAgent(Agent<A,P> a, String startLocation) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		state.setAgentLocationAndTravelDistance(a, startLocation, 0.0);
		super.addAgent(a);
	}

	public String getAgentLocation(Agent<A,P> a) {
		return state.getAgentLocation(a);
	}

	public Double getAgentTravelDistance(Agent<A,P> a) {
		return state.getAgentTravelDistance(a);
	}

	@Override
	public EnvironmentState getCurrentState() {
		return state;
	}

	@Override
	public EnvironmentState executeAction(Agent<A,P> agent, A a) {

		Optional<A> b = Optional.of(a);
		if (!b.isPresent()) //check for no operation
		{
			MoveToAction act = (MoveToAction) a;

			String currLoc = getAgentLocation(agent);
			Double distance = map.getDistance(currLoc, act.getToLocation());
			if (distance != null) {
				double currTD = getAgentTravelDistance(agent);
				state.setAgentLocationAndTravelDistance(agent,
						act.getToLocation(), currTD + distance);
			}
		}

		return state;
	}

	@SuppressWarnings("unchecked")
	public P getPerceptSeenBy(Agent<A,P> anAgent) {
		return (P) new DynamicPercept(DynAttributeNames.PERCEPT_IN,
				getAgentLocation(anAgent));
	}

	public Map getMap() {
		return map;
	}
}
