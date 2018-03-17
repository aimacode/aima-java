package aima.core.agent.basic;

import aima.core.agent.api.Agent;
import java.util.List;

/**
 * Possible environments in which Agent(s) can percieve and act.
 * 
 * @author Ritwik Sharma
 */
public interface Environment {
<<<<<<< HEAD
	 
	List<Agent> getAgents();

	/**
	 * Returns the Agent(s) belonging to this Environment.
	 */
	void addAgent(Agent agent);

	/**
	 * Add an agent to the Environment.
	 */
	void removeAgent(Agent agent);
=======
	
	/**
	 * Returns the Agent(s) belonging to this Environment.
	 */ 
	List<Agent> getAgents();
	
	/**
	 * Add an agent to the Environment.
	 */
	void addAgent(Agent agent);
>>>>>>> 4b0928e2ba050fc4632f60f56368f2216c76b29f

	/**
	 * Remove an agent from the environment.
	 */
<<<<<<< HEAD
	boolean isDone();
=======
	void removeAgent(Agent agent);
>>>>>>> 4b0928e2ba050fc4632f60f56368f2216c76b29f

	/**
	 * Returns "true" if the Environment is finished with its current task(s).
	 */
<<<<<<< HEAD
=======
	boolean isDone();

	
>>>>>>> 4b0928e2ba050fc4632f60f56368f2216c76b29f

}
