package aima.core.environment.vacuum;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a state in the Vacuum World
 * 
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public class VacuumEnvironmentState implements EnvironmentState, FullyObservableVacuumEnvironmentPercept, Cloneable {

	private Map<String, VacuumEnvironment.LocationState> state;
	private Map<Agent, String> agentLocations;

	/**
	 * Constructor
	 */
	public VacuumEnvironmentState() {
		state = new LinkedHashMap<String, VacuumEnvironment.LocationState>();
		agentLocations = new LinkedHashMap<Agent, String>();
	}

	/**
	 * Constructor
	 * 
	 * @param locAState
	 * @param locBState
	 */
	public VacuumEnvironmentState(VacuumEnvironment.LocationState locAState,
			VacuumEnvironment.LocationState locBState) {
		this();
		state.put(VacuumEnvironment.LOCATION_A, locAState);
		state.put(VacuumEnvironment.LOCATION_B, locBState);
	}

	@Override
	public String getAgentLocation(Agent a) {
		return agentLocations.get(a);
	}

	/**
	 * Sets the agent location
	 * 
	 * @param a
	 * @param location
	 */
	public void setAgentLocation(Agent a, String location) {
		agentLocations.put(a, location);
	}

	@Override
	public VacuumEnvironment.LocationState getLocationState(String location) {
		return state.get(location);
	}

	/**
	 * Sets the location state
	 * 
	 * @param location
	 * @param s
	 */
	public void setLocationState(String location, VacuumEnvironment.LocationState s) {
		state.put(location, s);
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() == obj.getClass()) {
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
			result.state = new LinkedHashMap<String, VacuumEnvironment.LocationState>(state);
			agentLocations = new LinkedHashMap<Agent, String>(agentLocations);
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
		return this.state.toString();
	}
}