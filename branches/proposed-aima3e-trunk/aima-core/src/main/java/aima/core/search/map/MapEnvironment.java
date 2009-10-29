package aima.core.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicEnvironmentState;
import aima.core.agent.impl.DynamicPercept;

/**
 * Represents the environment a MapAgent can navigate.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapEnvironment extends AbstractEnvironment {

	private Map aMap = null;
	private DynamicEnvironmentState state = new DynamicEnvironmentState();

	public MapEnvironment(Map aMap) {
		this.aMap = aMap;
	}

	public void addAgent(AbstractAgent a, String startLocation) {
		super.addAgent(a);
		state.setAttribute(DynAttributeNames.AGENT_LOCATION, startLocation);
		state.setAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE, 0.0);
	}
	
	public String getAgenetLocation() {
		return (String) state.getAttribute(DynAttributeNames.AGENT_LOCATION);
	}
	
	public Double getAgentTravelDistance() {
		return (Double) state.getAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE);
	}

	@Override
	public EnvironmentState executeAction(Agent agent, Action a) {
		
		if (!a.isNoOp()) {
			MoveToAction act = (MoveToAction) a;
			
			String currLoc = (String) state.getAttribute(DynAttributeNames.AGENT_LOCATION);
			Double distance = aMap.getDistance(currLoc, act.getToLocation());
			if (distance != null) {
				double currTD = (Double) state
						.getAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE);
				state.setAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE, currTD
						+ distance);
				state.setAttribute(DynAttributeNames.AGENT_LOCATION, act.getToLocation());
			}
		}
		
		return state;
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		String currLoc = (String) state.getAttribute(DynAttributeNames.AGENT_LOCATION);
		List<Action> possibleActions = new ArrayList<Action>();
		for (String a : aMap.getLocationsLinkedTo(currLoc)) {
			possibleActions.add(new MoveToAction(a, new Double(aMap.getDistance(currLoc, a))));
		}
		// TODO-should the possible actions be added to the Percept?
		return new DynamicPercept(DynAttributeNames.PERCEPT_IN, currLoc,
				DynAttributeNames.PERCEPT_POSSIBLE_ACTIONS, possibleActions);
	}

	public Map getMap() {
		return aMap;
	}
}
