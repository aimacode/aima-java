package aima.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 32.
 * 
 * Environment.
 */

/**
 * @author Ravi Mohan
 * 
 */
public abstract class Environment {

	protected ArrayList<EnvironmentObject> objects;

	protected ArrayList<Agent> agents;

	protected ArrayList<BasicEnvironmentView> views;

	public abstract void executeAction(Agent a, String act);

	public abstract Percept getPerceptSeenBy(Agent anAgent);

	protected Environment() {
		agents = new ArrayList<Agent>();
		objects = new ArrayList<EnvironmentObject>();
		views = new ArrayList<BasicEnvironmentView>();
	}

	public void registerView(BasicEnvironmentView bev) {
		views.add(bev);
	}

	public void updateViews(String command) {

		for (BasicEnvironmentView view : views) {
			view.envChanged(command);
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

	public void createExogenousChange() {

	}

	public void step() {
		if (!(this.isDone())) {

			for (Agent agent : agents) {

				String anAction = agent.execute(this.getPerceptSeenBy(agent));

				updateViews(anAction);
				this.executeAction(agent, anAction);
			}
			this.createExogenousChange();
		}
	}

	public void step(int n) {

		for (int i = 0; i < n; i++) {

			step();

		}
	}

	public void stepUntilNoOp() {
		while (!(this.isDone())) {
			step();
		}
	}

	public ArrayList getAgents() {
		return agents;
	}

	public ArrayList getObjects() {
		return objects;
	}

	public boolean alreadyContains(EnvironmentObject o) {

		boolean retval = false;

		for (EnvironmentObject eo : objects) {
			if (eo.equals(o)) {
				retval = true;
			}
		}

		return retval;
	}

	public boolean alreadyContains(Agent anAgent) {
		boolean retval = false;
		for (Agent agent : agents) {
			if (agent.equals(anAgent)) {
				retval = true;
			}
		}
		return retval;
	}

	public void addAgent(Agent a, String attributeName, Object attributeValue) {
		if (!(alreadyContains(a))) {
			a.setAttribute(attributeName, attributeValue);
			agents.add(a);
		}
	}

	public void addObject(EnvironmentObject o, String attributeName,
			Object attributeValue) {
		if (!(alreadyContains(o))) {
			o.setAttribute(attributeName, attributeValue);
			objects.add(o);
		}
	}

	public void addObject(EnvironmentObject o) {
		if (!(alreadyContains(o))) {
			objects.add(o);
		}
	}

	public void addAgent(Agent a) {
		if (!(alreadyContains(a))) {
			agents.add(a);
		}
	}

	public List<ObjectWithDynamicAttributes> getAllObjects() {
		List<ObjectWithDynamicAttributes> l = new ArrayList<ObjectWithDynamicAttributes>();
		l.addAll(objects);
		l.addAll(agents);
		return l;
	}

}