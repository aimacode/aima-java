package aima.core.environment.vacuum;

import java.util.LinkedHashMap;
import java.util.Map;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class VacuumEnvironmentState implements EnvironmentState {

	private Map<String, VacuumEnvironment.LocationState> state;
	private Map<Agent, String> agentLocations;

	public VacuumEnvironmentState() {
		state = new LinkedHashMap<String, VacuumEnvironment.LocationState>();
		agentLocations = new LinkedHashMap<Agent, String>();
	}

	public VacuumEnvironmentState(VacuumEnvironment.LocationState locAState,
			VacuumEnvironment.LocationState locBState) {
		this();
		state.put(VacuumEnvironment.LOCATION_A, locAState);
		state.put(VacuumEnvironment.LOCATION_B, locBState);
	}

	public String getAgentLocation(Agent a) {
		return agentLocations.get(a);
	}

	public void setAgentLocation(Agent a, String location) {
		agentLocations.put(a, location);
	}

	public VacuumEnvironment.LocationState getLocationState(String location) {
		return state.get(location);
	}

	public void setLocationState(String location,
			VacuumEnvironment.LocationState s) {
		state.put(location, s);
	}

	public String toString() {
		return state.toString();
	}
}
