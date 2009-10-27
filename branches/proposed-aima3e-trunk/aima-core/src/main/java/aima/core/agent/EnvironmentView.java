package aima.core.agent;

/**
 * Allows external applications/logic to view the interaction of Agents with an Environment. 
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface EnvironmentView {
	/**
	 * A simple notification message from the Environment, one of its objects.
	 * 
	 * @param msg
	 *            the message received.
	 */
	void notify(String msg);

	/**
	 * Indicates the Environment has changed as a result of an Agent's action.
	 * 
	 * @param agent
	 *            the Agent that performed the Action.
	 * @param action
	 *            the Action the Agent performed.
	 * @param state
	 *            the EnvironmentState that resulted from the Agent's Action on
	 *            the Environment.
	 */
	void envChanged(Agent agent, Action action, EnvironmentState state);
}
