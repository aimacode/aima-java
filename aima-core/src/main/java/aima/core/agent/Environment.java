package aima.core.agent;

import java.util.List;

/**
 * An abstract description of possible discrete Environments in which Agent(s)
 * can perceive and act.
 *
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public interface Environment<P, A> {
	/**
	 * Returns the Agents belonging to this Environment.
	 * 
	 * @return The Agents belonging to this Environment.
	 */
	List<Agent<?, ?>> getAgents();

	/**
	 * Add an agent to the Environment.
	 * 
	 * @param agent
	 *            the agent to be added.
	 */
	void addAgent(Agent<? super P, ? extends A> agent);

	/**
	 * Remove an agent from the environment.
	 * 
	 * @param agent
	 *            the agent to be removed.
	 */
	void removeAgent(Agent<? super P, ? extends A> agent);

	/**
	 * Returns the EnvironmentObjects that exist in this Environment.
	 * 
	 * @return the EnvironmentObjects that exist in this Environment.
	 */
	List<EnvironmentObject> getEnvironmentObjects();

	/**
	 * Add an EnvironmentObject to the Environment.
	 * 
	 * @param eo
	 *            the EnvironmentObject to be added.
	 */
	void addEnvironmentObject(EnvironmentObject eo);

	/**
	 * Remove an EnvironmentObject from the Environment.
	 * 
	 * @param eo
	 *            the EnvironmentObject to be removed.
	 */
	void removeEnvironmentObject(EnvironmentObject eo);

	/**
	 * Move the Environment one time step forward.
	 */
	void step();

	/**
	 * Move the Environment n time steps forward.
	 * 
	 * @param n
	 *            the number of time steps to move the Environment forward.
	 */
	default void step(int n) {
		for (int i = 0; i < n; i++)
			step();
	}

	/**
	 * Step through time steps until the Environment has no more tasks.
	 */
	default void stepUntilDone() {
		while (!isDone())
			step();
	}

	/**
	 * Returns <code>true</code> if the Environment is finished with its current
	 * task(s).
	 * 
	 * @return <code>true</code> if the Environment is finished with its current
	 *         task(s).
	 */
	boolean isDone();

	/**
	 * Retrieve the performance measure associated with an Agent.
	 * 
	 * @param agent
	 *            the Agent for which a performance measure is to be retrieved.
	 * @return the performance measure associated with the Agent.
	 */
	double getPerformanceMeasure(Agent<?, ?> agent);

	/**
	 * Add a listener which is notified about environment changes.
	 * 
	 * @param listener
	 *            the listener to be added.
	 */
	void addEnvironmentListener(EnvironmentListener<? super P, ? super A> listener);

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 *            the listener to be removed.
	 */
	void removeEnvironmentListener(EnvironmentListener<? super P, ? super A> listener);

	/**
	 * Notify all environment listeners of a message.
	 * 
	 * @param msg
	 *            the message to notify the registered listeners with.
	 */
	void notify(String msg);
}
