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
	List<Agent<? super P, ? extends A>> getAgents();

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
	void step(int n);

	/**
	 * Step through time steps until the Environment has no more tasks.
	 */
	void stepUntilDone();

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
	 * @param forAgent
	 *            the Agent for which a performance measure is to be retrieved.
	 * @return the performance measure associated with the Agent.
	 */
	double getPerformanceMeasure(Agent<?, ?> forAgent);

	/**
	 * Add a view on the Environment.
	 * 
	 * @param ev
	 *            the EnvironmentView to be added.
	 */
	void addEnvironmentView(EnvironmentView<? super P, ? super A> ev);

	/**
	 * Remove a view on the Environment.
	 * 
	 * @param ev
	 *            the EnvironmentView to be removed.
	 */
	void removeEnvironmentView(EnvironmentView<? super P, ? super A> ev);

	/**
	 * Notify all registered EnvironmentViews of a message.
	 * 
	 * @param msg
	 *            the message to notify the registered EnvironmentViews with.
	 */
	void notifyViews(String msg);
}
