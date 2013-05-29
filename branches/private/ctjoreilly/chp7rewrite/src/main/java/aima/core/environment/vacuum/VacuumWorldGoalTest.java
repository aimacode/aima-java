package aima.core.environment.vacuum;

import aima.core.agent.Agent;
import aima.core.search.framework.GoalTest;

/**
 * Tests for goals states
 * 
 * @author Andrew Brown
 */
public class VacuumWorldGoalTest implements GoalTest {

	private Agent agent;

	/**
	 * Constructor
	 * 
	 * @param agent
	 */
	public VacuumWorldGoalTest(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Tests whether the search has identified a goal state
	 * 
	 * @param state
	 * @return true if the state is a goal state, false otherwise.
	 */
	@Override
	public boolean isGoalState(Object state) {
		// setup
		VacuumEnvironmentState vacEnvState = (VacuumEnvironmentState) state;
		String currentLocation = vacEnvState.getAgentLocation(this.agent);
		String adjacentLocation = (currentLocation
				.equals(VacuumEnvironment.LOCATION_A)) ? VacuumEnvironment.LOCATION_B
				: VacuumEnvironment.LOCATION_A;
		// test goal state
		if (VacuumEnvironment.LocationState.Clean != vacEnvState
				.getLocationState(currentLocation)) {
			return false;
		} else if (VacuumEnvironment.LocationState.Clean != vacEnvState
				.getLocationState(adjacentLocation)) {
			return false;
		} else {
			return true;
		}
	}
}