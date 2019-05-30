package aima.core.agent.impl;

import aima.core.agent.*;
import aima.core.util.Tasks;

import java.util.*;

/**
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public abstract class AbstractEnvironment<P, A> implements Environment<P, A>,
		EnvironmentViewNotifier {

	// Note: Use LinkedHashSet's in order to ensure order is respected as
	// provide
	// access to these elements via List interface.
	protected Set<EnvironmentObject> envObjects = new LinkedHashSet<>();

	protected Set<Agent<? super P, ? extends A>> agents = new LinkedHashSet<>();

	protected Set<EnvironmentView<? super P, ? super A>> views = new LinkedHashSet<>();

	protected Map<Agent<?, ?>, Double> performanceMeasures = new LinkedHashMap<>();

	//
	// PUBLIC METHODS
	//

	//
	// Methods to be implemented by subclasses.

	public abstract void executeAction(Agent<?, ?> agent, A action);

	public abstract P getPerceptSeenBy(Agent<?, ?> agent);

	/**
	 * Method for implementing dynamic environments in which not all changes are
	 * directly caused by agent action execution. The default implementation
	 * does nothing.
	 */
	protected void createExogenousChange() {
	}

	/**
	 * Method is called when an agent doesn't select an action when asked. Default implementation does nothing.
	 * Subclasses can for example modify the isDone status.
	 */
	protected void executeNoOp(Agent<?,?> agent) {
	}

	//
	// START-Environment
	public List<Agent<?, ?>> getAgents() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<>(agents);
	}

	public void addAgent(Agent<? super P, ? extends A> agent) {
		agents.add(agent);
		addEnvironmentObject(agent);
		notifyEnvironmentViews(agent);
	}

	public void removeAgent(Agent<? super P, ? extends A> agent) {
		agents.remove(agent);
		removeEnvironmentObject(agent);
	}

	public List<EnvironmentObject> getEnvironmentObjects() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<>(envObjects);
	}

	public void addEnvironmentObject(EnvironmentObject eo) {
		envObjects.add(eo);
	}

	public void removeEnvironmentObject(EnvironmentObject eo) {
		envObjects.remove(eo);
	}

	/**
	 * Central template method for controlling agent simulation. The concrete
	 * behavior is determined by the primitive operations
	 * {@link #getPerceptSeenBy(Agent)}, {@link #executeAction(Agent, Object)},
	 * and {@link #createExogenousChange()}.
	 */
	public void step() {
		for (Agent<? super P, ? extends A> agent : agents) {
			if (agent.isAlive()) {
				P percept = getPerceptSeenBy(agent);
				Optional<? extends A> anAction = agent.execute(percept);
				if (anAction.isPresent()) {
					executeAction(agent, anAction.get());
					notifyEnvironmentViews(agent, percept, anAction.get());
				} else {
					executeNoOp(agent);
				}
			}
		}
		createExogenousChange();
	}

	public void step(int n) {
		for (int i = 0; i < n; i++) {
			step();
		}
	}

	public void stepUntilDone() {
		while (!isDone())
			step();
	}

	/**
	 * Returns true if the current task was cancelled or no agent is alive anymore.
	 */
	public boolean isDone() {
		return Tasks.currIsCancelled() || agents.stream().noneMatch(Agent::isAlive);
	}

	public double getPerformanceMeasure(Agent<?, ?> forAgent) {
		return performanceMeasures.computeIfAbsent(forAgent, k -> 0.0);
	}

	public void addEnvironmentView(EnvironmentView<? super P, ? super A> ev) {
		views.add(ev);
	}

	public void removeEnvironmentView(EnvironmentView ev) {
		views.remove(ev);
	}

	public void notifyViews(String msg) {
		views.forEach(ev -> ev.notify(msg));
	}

	// END-Environment
	//

	//
	// PROTECTED METHODS
	//

	protected void updatePerformanceMeasure(Agent<?, ?> forAgent, double addTo) {
		performanceMeasures.put(forAgent, getPerformanceMeasure(forAgent) + addTo);
	}

	protected void notifyEnvironmentViews(Agent<?, ?> agent) {
		views.forEach(view -> view.agentAdded(agent, this));
	}

	protected void notifyEnvironmentViews(Agent<?, ?> agent, P percept, A action) {
		views.forEach(view -> view.agentActed(agent, percept, action, this));
	}
}