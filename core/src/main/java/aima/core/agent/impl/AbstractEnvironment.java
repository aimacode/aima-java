package aima.core.agent.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentObject;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;

/**
 * 
 * @author Subham Mishra
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public abstract class AbstractEnvironment<A,P>  {

	// Note: Use LinkedHashSet's in order to ensure order is respected as
	// provide
	// access to these elements via List interface.
	protected Set<EnvironmentObject> envObjects = new LinkedHashSet<EnvironmentObject>();

	protected Set<Agent<A,P>> agents = new LinkedHashSet<Agent<A,P>>();

	protected Set<EnvironmentView<A,P>> views = new LinkedHashSet<EnvironmentView<A,P>>();

	protected Map<Agent<A,P>, Double> performanceMeasures = new LinkedHashMap<Agent<A,P>, Double>();

	//
	// PRUBLIC METHODS
	//

	//
	// Methods to be implemented by subclasses.
	public abstract EnvironmentState getCurrentState();

	public abstract EnvironmentState executeAction(Agent<A,P> agent, A action);

	public abstract P getPerceptSeenBy(Agent<A,P> anAgent);

	/**
	 * Method for implementing dynamic environments in which not all changes are
	 * directly caused by agent action execution. The default implementation
	 * does nothing.
	 */
	public void createExogenousChange() {
		//nothing
	}

	//
	// START-Environment
	public List<Agent<A,P>> getAgents() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<Agent<A,P>>(agents);
	}

	public void addAgent(Agent<A,P> a) {
		addEnvironmentObject(a);
	}

	public void removeAgent(Agent<A,P> a) {
		removeEnvironmentObject(a);
	}

	public List<EnvironmentObject> getEnvironmentObjects() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<EnvironmentObject>(envObjects);
	}

	public void addEnvironmentObject(EnvironmentObject eo) {
		envObjects.add(eo);
		if (eo instanceof Agent) {
			@SuppressWarnings("unchecked")
			Agent<A,P> a = (Agent<A,P>) eo;
			if (!agents.contains(a)) {
				agents.add(a);
				this.updateEnvironmentViewsAgentAdded(a);
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
		for (Agent<A,P> agent : agents) {
			if (agent.isAlive()) {
				A anAction = agent.execute(getPerceptSeenBy(agent));
				EnvironmentState es = executeAction(agent, anAction);
				updateEnvironmentViewsAgentActed(agent, anAction, es);
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
		for (Agent<A,P> agent : agents) {
			if (agent.isAlive()) {
				return false;
			}
		}
		return true;
	}

	public double getPerformanceMeasure(Agent<A,P> forAgent) {
		Double pm = performanceMeasures.get(forAgent);
		if (null == pm) {
			pm = new Double(0);
			performanceMeasures.put(forAgent, pm);
		}

		return pm;
	}

	public void addEnvironmentView(EnvironmentView<A,P> ev) {
		views.add(ev);
	}

	public void removeEnvironmentView(EnvironmentView<A,P> ev) {
		views.remove(ev);
	}

	public void notifyViews(String msg) {
		for (EnvironmentView<A,P> ev : views) {
			ev.notify(msg);
		}
	}

	// END-Environment
	//

	//
	// PROTECTED METHODS
	//

	protected void updatePerformanceMeasure(Agent<A,P> forAgent, double addTo) {
		performanceMeasures.put(forAgent, getPerformanceMeasure(forAgent)
				+ addTo);
	}

	protected void updateEnvironmentViewsAgentAdded(Agent<A,P> agent) {
		for (EnvironmentView<A,P> view : views) {
			view.agentAdded(agent, getCurrentState());
		}
	}

	protected void updateEnvironmentViewsAgentActed(Agent<A,P> agent, A action,
			EnvironmentState state) {
		for (EnvironmentView<A,P> view : views) {
			view.agentActed(agent, action, state);
		}
	}
}
