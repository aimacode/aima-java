package aima.core.agent.impl.vacuum;

import java.util.LinkedHashMap;
import java.util.Map;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class VaccumEnvironmentState implements EnvironmentState {

	private Map<VaccumEnvironment.Location, VaccumEnvironment.LocationState> state = new LinkedHashMap<VaccumEnvironment.Location, VaccumEnvironment.LocationState>();
	private Map<Agent, VaccumEnvironment.Location> agentLocations = new LinkedHashMap<Agent, VaccumEnvironment.Location>();
	
	public VaccumEnvironmentState(VaccumEnvironment.LocationState locAState,
			VaccumEnvironment.LocationState locBState) {
		state.put(VaccumEnvironment.Location.A, locAState);
		state.put(VaccumEnvironment.Location.B, locBState);
	}
	
	public VaccumEnvironment.Location getAgentLocation(Agent a) {
		return agentLocations.get(a);
	}
	
	public void setAgentLocation(Agent a, VaccumEnvironment.Location location) {
		agentLocations.put(a, location);
	}
	
	public VaccumEnvironment.LocationState getLocationState(VaccumEnvironment.Location location) {
		return state.get(location);
	}
	
	public void setLocationState(VaccumEnvironment.Location location, VaccumEnvironment.LocationState s) {
		state.put(location, s);
	}

	public String toString() {
		return state.toString();
	}
}
