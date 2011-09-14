package aima.core.learning.reinforcement.example;

import java.util.HashMap;
import java.util.Map;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.environment.cellworld.Cell;

/**
 * An implementation of the EnvironmentState interface for a Cell World.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class CellWorldEnvironmentState implements EnvironmentState {
	private Map<Agent, CellWorldPercept> agentLocations = new HashMap<Agent, CellWorldPercept>();

	/**
	 * Default Constructor.
	 */
	public CellWorldEnvironmentState() {
	}

	/**
	 * Reset the environment state to its default state.
	 */
	public void reset() {
		agentLocations.clear();
	}

	/**
	 * Set an agent's location within the cell world environment.
	 * 
	 * @param anAgent
	 *            the agents whose location is to be tracked.
	 * @param location
	 *            the location for the agent in the cell world environment.
	 */
	public void setAgentLocation(Agent anAgent, Cell<Double> location) {
		CellWorldPercept percept = agentLocations.get(anAgent);
		if (null == percept) {
			percept = new CellWorldPercept(location);
			agentLocations.put(anAgent, percept);
		} else {
			percept.setCell(location);
		}
	}

	/**
	 * Get the location of an agent within the cell world environment.
	 * 
	 * @param anAgent
	 *            the agent whose location is being queried.
	 * @return the location of the agent within the cell world environment.
	 */
	public Cell<Double> getAgentLocation(Agent anAgent) {
		return agentLocations.get(anAgent).getCell();
	}

	/**
	 * Get a percept for an agent, representing what it senses within the cell
	 * world environment.
	 * 
	 * @param anAgent
	 *            the agent a percept is being queried for.
	 * @return a percept for the agent, representing what it senses within the
	 *         cell world environment.
	 */
	public CellWorldPercept getPerceptFor(Agent anAgent) {
		return agentLocations.get(anAgent);
	}
}