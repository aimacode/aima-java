package aima.core.search.framework;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * 
 */
public class Successor {

	private Action action;

	private Object state;

	public Successor(Action action, Object state) {
		this.action = action;
		this.state = state;
	}

	public Action getAction() {
		return action;
	}

	public Object getState() {
		return state;
	}
}