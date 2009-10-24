package aima.core.agent.impl.vacuum;

import aima.core.agent.impl.DynamicPercept;

public class VaccumEnvPercept extends DynamicPercept {
	public static final String ATTRIBUTE_AGENT_LOCATION = "agentLocation";
	public static final String ATTRIBUTE_STATE          = "state";
	
	private VaccumEnvironment.Location agentLocation; 
	private VaccumEnvironment.LocationState state;
	
	public VaccumEnvPercept(VaccumEnvironment.Location agentLocation, VaccumEnvironment.LocationState state) {
		this.agentLocation = agentLocation;
		this.state = state;
		setAttribute(ATTRIBUTE_AGENT_LOCATION, agentLocation);
		setAttribute(ATTRIBUTE_STATE, state);
	}
	
	public VaccumEnvironment.Location getAgentLocation() {
		return agentLocation;
	}
	
	public VaccumEnvironment.LocationState getLocationState() {
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
