package aima.core.agent.basic;

import aima.core.agent.api.Agent;
import java.util.List;

/**
 * Possible environments in which Agent(s) can percieve and act.
 * 
 * @author Ritwik Sharma
 */
public interface Environment {
	 
	List<Agent> getAgents();

	/**
	 * Returns the Agent(s) belonging to this Environment.
	 */
	void addAgent(Agent agent);

	/**
	 * Add an agent to the Environment.
	 */
	void removeAgent(Agent agent);

	/**
	 * Remove an agent from the environment.
	 */
	boolean isDone();

	/**
	 * Returns "true" if the Environment is finished with its current task(s).
	 */

}
