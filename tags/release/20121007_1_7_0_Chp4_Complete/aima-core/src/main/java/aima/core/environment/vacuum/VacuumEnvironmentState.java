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
public class VacuumEnvironmentState implements EnvironmentState,
		FullyObservableVacuumEnvironmentPercept {

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

	/**
	 * Copy Constructor.
	 * 
	 * @param toCopyState
	 *            Vacuum Environment State to copy.
	 */
	public VacuumEnvironmentState(VacuumEnvironmentState toCopyState) {
		this();
		this.state.putAll(toCopyState.state);
		this.agentLocations.putAll(toCopyState.agentLocations);
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
	public void setLocationState(String location,
			VacuumEnvironment.LocationState s) {
		state.put(location, s);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof VacuumEnvironmentState) {
			VacuumEnvironmentState s = (VacuumEnvironmentState) o;
			if (this.state.equals(s.state)
					&& this.agentLocations.equals(s.agentLocations)) {
				return true;
			}
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
		int hash = 7;
		hash = 13 * hash + (this.state != null ? this.state.hashCode() : 0);
		hash = 53
				* hash
				+ (this.agentLocations != null ? this.agentLocations.hashCode()
						: 0);
		return hash;
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