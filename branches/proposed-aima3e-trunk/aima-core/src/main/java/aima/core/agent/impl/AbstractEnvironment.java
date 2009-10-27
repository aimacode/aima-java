package aima.core.agent.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentObject;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.agent.Percept;


/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 32.
 * 
 * Environment.
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public abstract class AbstractEnvironment implements Environment {

	// Note: Use LinkedHashSet's in order to ensure order is respected as provide
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
	public abstract EnvironmentState executeAction(Agent agent, Action action);

	public abstract Percept getPerceptSeenBy(Agent anAgent);

	//
	// START-Environment
	public List<Agent> getAgents() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<Agent>(agents);
	}
	
	public void addAgent(Agent a) {
		agents.add(a);
		envObjects.add(a);
	}
	
	public void removeAgent(Agent a) {
		agents.remove(a);
		envObjects.remove(a);
	}

	public List<EnvironmentObject> getEnvironmentObjects() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<EnvironmentObject>(envObjects);
	}
	
	public void addEnvironmentObject(EnvironmentObject eo) {
		envObjects.add(eo);
		if (eo instanceof Agent) {
			agents.add((Agent)eo);
		}
	}
	
	public void removeEnvironmentObject(EnvironmentObject eo) {
		envObjects.remove(eo);
		agents.remove(eo);
	}
	
	public void step() {
		if (!isDone()) {
			for (Agent agent : agents) {

				Action anAction = agent.execute(getPerceptSeenBy(agent));

				EnvironmentState es = executeAction(agent, anAction);
				
				updateEnvironmentViews(agent, anAction, es);
			}
		}
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
	
	public void updatePerformanceMeasure(Agent forAgent, double addTo) {
		performanceMeasures.put(forAgent, getPerformanceMeasure(forAgent)+addTo);
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

	protected void updateEnvironmentViews(Agent agent, Action action, EnvironmentState state) {
		for (EnvironmentView view : views) {
			view.envChanged(agent, action, state);
		}
	}
}