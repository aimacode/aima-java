package aima.core.agent.impl.vacuum;

import aima.core.agent.impl.DynamicPercept;

public class VacuumEnvPercept extends DynamicPercept {
	public static final String ATTRIBUTE_AGENT_LOCATION = "agentLocation";
	public static final String ATTRIBUTE_STATE          = "state";
	
	private VacuumEnvironment.Location agentLocation; 
	private VacuumEnvironment.LocationState state;
	
	public VacuumEnvPercept(VacuumEnvironment.Location agentLocation, VacuumEnvironment.LocationState state) {
		this.agentLocation = agentLocation;
		this.state = state;
		setAttribute(ATTRIBUTE_AGENT_LOCATION, agentLocation);
		setAttribute(ATTRIBUTE_STATE, state);
	}
	
	public VacuumEnvironment.Location getAgentLocation() {
		return agentLocation;
	}
	
	public VacuumEnvironment.LocationState getLocationState() {
		return state;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		sb.append(agentLocation);
		sb.append(", ");
		sb.append(state);
		sb.append("]");
		
		return sb.toString();
	}
}
