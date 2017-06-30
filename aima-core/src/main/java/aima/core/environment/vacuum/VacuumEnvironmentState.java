package aima.core.environment.vacuum;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a state in the Vacuum World
 * 
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public class VacuumEnvironmentState implements EnvironmentState, Percept, Cloneable {

	private Map<String, VacuumEnvironment.LocationState> state;
	private Map<Agent, String> agentLocations;

	/**
	 * Constructor
	 */
	public VacuumEnvironmentState() {
		state = new LinkedHashMap<>();
		agentLocations = new LinkedHashMap<>();
	}

	public String getAgentLocation(Agent a) {
		return agentLocations.get(a);
	}

	/**
	 * Sets the agent location
	 */
	public void setAgentLocation(Agent a, String location) {
		agentLocations.put(a, location);
	}

	public VacuumEnvironment.LocationState getLocationState(String location) {
		return state.get(location);
	}

	/**
	 * Sets the location state
	 */
	public void setLocationState(String location, VacuumEnvironment.LocationState s) {
		state.put(location, s);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass() == obj.getClass()) {
			VacuumEnvironmentState s = (VacuumEnvironmentState) obj;
			return state.equals(s.state) && agentLocations.equals(s.agentLocations);
		}
		return false;
	}

	/**
	 * Override hashCode()
	 * 
	 * @return the hash code for this object.
	 */
	@Override
	public int hashCode() {
		return 3 * state.hashCode() + 13 * agentLocations.hashCode();
	}

	@Override
	public VacuumEnvironmentState clone() {
		VacuumEnvironmentState result = null;
		try {
			result = (VacuumEnvironmentState) super.clone();
			result.state = new LinkedHashMap<>(state);
			agentLocations = new LinkedHashMap<>(agentLocations);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Returns a string representation of the environment
	 * 
	 * @return a string representation of the environment
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		for (Map.Entry<String, VacuumEnvironment.LocationState> entity : state.entrySet()) {
			if (builder.length() > 2) builder.append(", ");
			builder.append(entity.getKey()).append("=").append(entity.getValue());
		}
		int i = 0;
		for (Map.Entry<Agent, String> entity : agentLocations.entrySet()) {
			if (builder.length() > 2) builder.append(", ");
			builder.append("Loc").append(++i).append("=").append(entity.getValue());
		}
		builder.append("}");
		return builder.toString();
	}
}