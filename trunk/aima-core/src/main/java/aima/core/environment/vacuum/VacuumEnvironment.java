package aima.core.environment.vacuum;

import java.util.Random;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicAction;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class VacuumEnvironment extends AbstractEnvironment {
	// Allowable Actions within the Vaccum Environment
	public static final Action ACTION_MOVE_LEFT = new DynamicAction("Left");
	public static final Action ACTION_MOVE_RIGHT = new DynamicAction("Right");
	public static final Action ACTION_SUCK = new DynamicAction("Suck");
	public static final String LOCATION_A = "A";
	public static final String LOCATION_B = "B";

	public enum LocationState {
		Clean, Dirty
	};

	//
	protected VacuumEnvironmentState envState = null;
	protected boolean isDone = false;

	public VacuumEnvironment() {
		Random r = new Random();
		envState = new VacuumEnvironmentState(
				0 == r.nextInt(2) ? LocationState.Clean : LocationState.Dirty,
				0 == r.nextInt(2) ? LocationState.Clean : LocationState.Dirty);
	}

	public VacuumEnvironment(LocationState locAState, LocationState locBState) {
		envState = new VacuumEnvironmentState(locAState, locBState);
	}

	@Override
	public EnvironmentState getCurrentState() {
		return envState;
	}

	@Override
	public EnvironmentState executeAction(Agent a, Action agentAction) {

		if (ACTION_MOVE_RIGHT == agentAction) {
			envState.setAgentLocation(a, LOCATION_B);
			updatePerformanceMeasure(a, -1);
		} else if (ACTION_MOVE_LEFT == agentAction) {
			envState.setAgentLocation(a, LOCATION_A);
			updatePerformanceMeasure(a, -1);
		} else if (ACTION_SUCK == agentAction) {
			if (LocationState.Dirty == envState.getLocationState(envState
					.getAgentLocation(a))) {
				envState.setLocationState(envState.getAgentLocation(a),
						LocationState.Clean);
				updatePerformanceMeasure(a, 10);
			}
		} else if (agentAction.isNoOp()) {
			// In the Vacuum Environment we consider things done if
			// the agent generates a NoOp.
			isDone = true;
		}

		return envState;
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		String agentLocation = envState.getAgentLocation(anAgent);
		return new VacuumEnvPercept(agentLocation, envState
				.getLocationState(agentLocation));
	}

	@Override
	public boolean isDone() {
		return super.isDone() || isDone;
	}

	@Override
	public void addAgent(Agent a) {
		int idx = new Random().nextInt(2);
		envState.setAgentLocation(a, idx == 0 ? LOCATION_A : LOCATION_B);
		super.addAgent(a);
	}

	public void addAgent(Agent a, String location) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		envState.setAgentLocation(a, location);
		super.addAgent(a);
	}

	public LocationState getLocationState(String location) {
		return envState.getLocationState(location);
	}

	public String getAgentLocation(Agent a) {
		return envState.getAgentLocation(a);
	}
}