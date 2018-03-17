package aima.core.agent.basic;

import aima.core.agent.api.Agent;
import java.util.List;

/**
 * Possible environments in which Agent(s) can percieve and act.
 * 
 * @author Ritwik Sharma
 */
public interface Environment {
	
	/**
	 * Returns the Agent(s) belonging to this Environment.
	 */
	List<Agent> getAgents();
	
	/**
	 * Add an agent to the Environment.
	 */
	void addAgent(Agent agent);
	
	/**
	 * Remove an agent from the environment.
	 */
	void removeAgent(Agent agent);

	/**
	 * Returns "true" if the Environment is finished with its current task(s).
	 */
	boolean isDone();

}
