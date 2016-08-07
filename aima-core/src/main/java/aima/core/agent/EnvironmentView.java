package aima.core.agent;

/**
 * Allows external applications/logic to view the interaction of Agent(s) with
 * an Environment.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public interface EnvironmentView {
	/**
	 * A simple notification message from an object in the Environment.
	 * 
	 * @param msg
	 *            the message received.
	 */
	void notify(String msg);

	/**
	 * Indicates an Agent has been added to the environment and what it
	 * perceives initially.
	 * 
	 * @param agent
	 *            the Agent just added to the Environment.
	 * @param source
	 *            the Environment to which the agent was added.
	 */
	void agentAdded(Agent agent, Environment source);

	/**
	 * Indicates the Environment has changed as a result of an Agent's action.
	 * 
	 * @param agent
	 *            the Agent that performed the Action.
	 * @param action
	 *            the Action the Agent performed.
	 * @param source
	 *            the Environment in which the agent has acted.
	 */
	void agentActed(Agent agent, Action action, Environment source);
}
