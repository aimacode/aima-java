package aima.core.agent.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentObject;
import aima.core.agent.EnvironmentView;
import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public abstract class AbstractEnvironment implements Environment,
		EnvironmentViewNotifier {

	// Note: Use LinkedHashSet's in order to ensure order is respected as
	// provide
	// access to these elements via List interface.
	protected Set<EnvironmentObject> envObjects = new LinkedHashSet<EnvironmentObject>();

	protected Set<Agent> agents = new LinkedHashSet<Agent>();

	protected Set<EnvironmentView> views = new LinkedHashSet<EnvironmentView>();

	protected Map<Agent, Double> performanceMeasures = new LinkedHashMap<Agent, Double>();

	//
	// PRUBLIC METHODS
	//
	
	//
	// Methods to be implemented by subclasses.

	public abstract void executeAction(Agent agent, Action action);

	public abstract Percept getPerceptSeenBy(Agent anAgent);

	/**
	 * Method for implementing dynamic environments in which not all changes are
	 * directly caused by agent action execution. The default implementation
	 * does nothing.
	 */
	public void createExogenousChange() {
	}

	//
	// START-Environment
	public List<Agent> getAgents() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<Agent>(agents);
	}

	public void addAgent(Agent a) {
		addEnvironmentObject(a);
	}

	public void removeAgent(Agent a) {
		removeEnvironmentObject(a);
	}

	public List<EnvironmentObject> getEnvironmentObjects() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<EnvironmentObject>(envObjects);
	}

	public void addEnvironmentObject(EnvironmentObject eo) {
		envObjects.add(eo);
		if (eo instanceof Agent) {
			Agent a = (Agent) eo;
			if (!agents.contains(a)) {
				agents.add(a);
				this.notifyEnvironmentViews(a);
			}
		}
	}

	public void removeEnvironmentObject(EnvironmentObject eo) {
		envObjects.remove(eo);
		agents.remove(eo);
	}

	/**
	 * Central template method for controlling agent simulation. The concrete
	 * behavior is determined by the primitive operations
	 * {@link #getPerceptSeenBy(Agent)}, {@link #executeAction(Agent, Action)},
	 * and {@link #createExogenousChange()}.
	 */
	public void step() {
		for (Agent agent : agents) {
			if (agent.isAlive()) {
				Action anAction = agent.execute(getPerceptSeenBy(agent));
				executeAction(agent, anAction);
				notifyEnvironmentViews(agent, anAction);
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
		while (!isDone()) {
			step();
		}
	}

	public boolean isDone() {
		for (Agent agent : agents) {
			if (agent.isAlive()) {
				return false;
			}
		}
		return true;
	}

	public double getPerformanceMeasure(Agent forAgent) {
		Double pm = performanceMeasures.get(forAgent);
		if (null == pm) {
			pm = new Double(0);
			performanceMeasures.put(forAgent, pm);
		}

		return pm;
	}

	public void addEnvironmentView(EnvironmentView ev) {
		views.add(ev);
	}

	public void removeEnvironmentView(EnvironmentView ev) {
		views.remove(ev);
	}

	public void notifyViews(String msg) {
		for (EnvironmentView ev : views) {
			ev.notify(msg);
		}
	}

	// END-Environment
	//

	//
	// PROTECTED METHODS
	//

	protected void updatePerformanceMeasure(Agent forAgent, double addTo) {
		performanceMeasures.put(forAgent, getPerformanceMeasure(forAgent)
				+ addTo);
	}

	protected void notifyEnvironmentViews(Agent agent) {
		for (EnvironmentView view : views) {
			view.agentAdded(agent, this);
		}
	}

	protected void notifyEnvironmentViews(Agent agent, Action action) {
		for (EnvironmentView view : views) {
			view.agentActed(agent, action, this);
		}
	}
}