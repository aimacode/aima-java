package aima.core.search.online;

import aima.core.agent.Action;

/**
 * @author Ciaran O'Reilly
 */
public class StateAction {
	private final Object state;
	private final Action action;

	public StateAction(Object state, Action action) {
		this.state = state;
		this.action = action;
	}

	public Object getState() {
		return state;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof StateAction)) {
			return super.equals(o);
		}
		return (state.equals(((StateAction) o).state) && action
				.equals(((StateAction) o).action));
	}

	@Override
	public int hashCode() {
		return state.hashCode() + action.hashCode();
	}

	@Override
	public String toString() {
		return "(" + state + ", " + action + ")";
	}
}
