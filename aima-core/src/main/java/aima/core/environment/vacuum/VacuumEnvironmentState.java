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

	private Map<VacuumEnvironment.Location, VacuumEnvironment.LocationState> state = new LinkedHashMap<VacuumEnvironment.Location, VacuumEnvironment.LocationState>();
	private Map<Agent, VacuumEnvironment.Location> agentLocations = new LinkedHashMap<Agent, VacuumEnvironment.Location>();

	public VacuumEnvironmentState(VacuumEnvironment.LocationState locAState,
			VacuumEnvironment.LocationState locBState) {
		state.put(VacuumEnvironment.Location.A, locAState);
		state.put(VacuumEnvironment.Location.B, locBState);
	}

	public VacuumEnvironment.Location getAgentLocation(Agent a) {
		return agentLocations.get(a);
	}

	public void setAgentLocation(Agent a, VacuumEnvironment.Location location) {
		agentLocations.put(a, location);
	}

	public VacuumEnvironment.LocationState getLocationState(
			VacuumEnvironment.Location location) {
		return state.get(location);
	}

	public void setLocationState(VacuumEnvironment.Location location,
			VacuumEnvironment.LocationState s) {
		state.put(location, s);
	}

	public String toString() {
		return state.toString();
	}
}
