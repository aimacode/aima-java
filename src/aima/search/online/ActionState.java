package aima.search.online;

import aima.basic.Percept;

public class ActionState {
	private final Object action;
	private final Percept state;

	public ActionState(Object action, Percept state) {
		this.action = action;
		this.state = state;
	}

	public Object getAction() {
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
