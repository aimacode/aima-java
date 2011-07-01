package aima.core.environment.vacuum;

import aima.core.agent.impl.DynamicPercept;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class VacuumEnvPercept extends DynamicPercept {
	public static final String ATTRIBUTE_AGENT_LOCATION = "agentLocation";
	public static final String ATTRIBUTE_STATE = "state";

	/**
	 * Constructs a vacuum environment percept from the agent's perception of
	 * the current location and state.
	 * 
	 * @param agentLocation
	 *            the agent's perception of the current location.
	 * @param state
	 *            the agent's perception of the current state.
	 */
	public VacuumEnvPercept(String agentLocation,
			VacuumEnvironment.LocationState state) {
		setAttribute(ATTRIBUTE_AGENT_LOCATION, agentLocation);
		setAttribute(ATTRIBUTE_STATE, state);
	}

	/**
	 * Returns the agent's perception of the current location, which is either A
	 * or B.
	 * 
	 * @return the agent's perception of the current location, which is either A
	 *         or B.
	 */
	public String getAgentLocation() {
		return (String) getAttribute(ATTRIBUTE_AGENT_LOCATION);
	}

	/**
	 * Returns the agent's perception of the current state, which is either
	 * <em>Clean</em> or <em>Dirty</em>.
	 * 
	 * @return the agent's perception of the current state, which is either
	 *         <em>Clean</em> or <em>Dirty</em>.
	 */
	public VacuumEnvironment.LocationState getLocationState() {
		return (VacuumEnvironment.LocationState) getAttribute(ATTRIBUTE_STATE);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("[");
		sb.append(getAgentLocation());
		sb.append(", ");
		sb.append(getLocationState());
		sb.append("]");

		return sb.toString();
	}
}
