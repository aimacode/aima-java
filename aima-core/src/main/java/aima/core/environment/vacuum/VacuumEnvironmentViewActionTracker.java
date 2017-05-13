package aima.core.environment.vacuum;

import aima.core.agent.*;

public class VacuumEnvironmentViewActionTracker implements EnvironmentView {
	private StringBuilder actions = null;

	public VacuumEnvironmentViewActionTracker(StringBuilder envChanges) {
		this.actions = envChanges;
	}

	//
	// START-EnvironmentView
	public void notify(String msg) {
		// Do nothing by default.
	}

	public void agentAdded(Agent agent, Environment source) {
		// Do nothing by default.
	}

	public void agentActed(Agent agent, Percept percept, Action action, Environment source) {
		actions.append(action);
	}

	// END-EnvironmentView
	//
}
