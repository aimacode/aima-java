package aima.core.environment.vacuum;

import aima.core.agent.impl.DynamicPercept;

public class VacuumEnvPercept extends DynamicPercept {
	public static final String ATTRIBUTE_AGENT_LOCATION = "agentLocation";
	public static final String ATTRIBUTE_STATE = "state";

	public VacuumEnvPercept(VacuumEnvironment.Location agentLocation,
			VacuumEnvironment.LocationState state) {
		setAttribute(ATTRIBUTE_AGENT_LOCATION, agentLocation);
		setAttribute(ATTRIBUTE_STATE, state);
	}

	public VacuumEnvironment.Location getAgentLocation() {
		return (VacuumEnvironment.Location) getAttribute(ATTRIBUTE_AGENT_LOCATION);
	}

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
