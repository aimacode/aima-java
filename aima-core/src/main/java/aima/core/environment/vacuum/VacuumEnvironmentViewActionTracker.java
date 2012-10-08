package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;

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

	public void agentAdded(Agent agent, EnvironmentState state) {
		// Do nothing by default.
	}

	public void agentActed(Agent agent, Action action, EnvironmentState state) {
		actions.append(action);
	}

	// END-EnvironmentView
	//
}
