package aima.core.agent;

/**
 * Allows applications to analyze and visualize the interaction of Agent(s) with an Environment.
 *
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public interface EnvironmentListener<P, A> {
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
	void agentAdded(Agent<?, ?> agent, Environment<?, ?> source);

	/**
	 * Indicates the Environment has changed as a result of an Agent's action.
	 * 
	 * @param agent
	 *            the Agent that performed the Action.
	 * @param percept
	 *            the Percept the Agent received from the environment.
	 * @param action
	 *            the Action the Agent performed.
	 * @param source
	 *            the Environment in which the agent has acted.
	 */
	void agentActed(Agent<?, ?> agent, P percept, A action, Environment<?, ?> source);
}
