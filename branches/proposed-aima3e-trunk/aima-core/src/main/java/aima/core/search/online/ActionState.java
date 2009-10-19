package aima.core.search.online;

import aima.core.agent.Action;
import aima.core.agent.Percept;

public class ActionState {
	private final Action action;
	private final Percept state;

	public ActionState(Action action, Percept state) {
		this.action = action;
		this.state = state;
	}

	public Action getAction() {
		return action;
	}

	public Percept getState() {
		return state;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ActionState)) {
			return super.equals(o);
		}
		return (action.equals(((ActionState) o).action) && state
				.equals(((ActionState) o).state));
	}

	@Override
	public int hashCode() {
		return action.hashCode() + state.hashCode();
	}

	@Override
	public String toString() {
		return "(" + action + ", " + state + ")";
	}
}
