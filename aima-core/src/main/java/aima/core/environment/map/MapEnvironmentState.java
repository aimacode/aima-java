package aima.core.environment.map;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.util.datastructure.Pair;

import java.util.HashMap;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class MapEnvironmentState implements EnvironmentState {
	private java.util.Map<Agent<?, ?>, Pair<String, Double>>
			agentLocationAndTravelDistance = new HashMap<>();

	public MapEnvironmentState() {

	}

	public String getAgentLocation(Agent<?, ?> agent) {
		Pair<String, Double> locAndTDistance = agentLocationAndTravelDistance.get(agent);
		if (null == locAndTDistance) {
			return null;
		}
		return locAndTDistance.getFirst();
	}

	public Double getAgentTravelDistance(Agent<?, ?> agent) {
		Pair<String, Double> locAndTDistance = agentLocationAndTravelDistance.get(agent);
		return (null != locAndTDistance) ? locAndTDistance.getSecond() : null;
	}

	public void setAgentLocationAndTravelDistance(Agent<?, ?> agent, String location, Double travelDistance) {
		agentLocationAndTravelDistance.put(agent, new Pair<>(location, travelDistance));
	}
}
