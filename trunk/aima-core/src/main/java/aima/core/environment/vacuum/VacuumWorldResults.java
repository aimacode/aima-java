package aima.core.environment.vacuum;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.search.nondeterministic.ResultsFunction;

/**
 * Returns possible results
 * 
 * @author Andrew Brown
 */
public class VacuumWorldResults implements ResultsFunction {

	private Agent agent;

	/**
	 * Constructor
	 * 
	 * @param agent
	 */
	public VacuumWorldResults(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Returns a list of possible results for a given state and action
	 * 
	 * @param state
	 * @param action
	 * @return a list of possible results for a given state and action.
	 */
	@Override
	public Set<Object> results(Object state, Action action) {
		// setup
		VacuumEnvironmentState vacEnvState = (VacuumEnvironmentState) state;
		// Ensure order is consistent across platforms.
		Set<Object> results = new LinkedHashSet<Object>();
		String currentLocation = vacEnvState.getAgentLocation(agent);
		String adjacentLocation = (currentLocation
				.equals(VacuumEnvironment.LOCATION_A)) ? VacuumEnvironment.LOCATION_B
				: VacuumEnvironment.LOCATION_A;
		// case: move right
		if (VacuumEnvironment.ACTION_MOVE_RIGHT == action) {
			VacuumEnvironmentState s = new VacuumEnvironmentState();
			s.setLocationState(currentLocation,
					vacEnvState.getLocationState(currentLocation));
			s.setLocationState(adjacentLocation,
					vacEnvState.getLocationState(adjacentLocation));
			s.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_B);
			results.add(s);
		} // case: move left
		else if (VacuumEnvironment.ACTION_MOVE_LEFT == action) {
			VacuumEnvironmentState s = new VacuumEnvironmentState();
			s.setLocationState(currentLocation,
					vacEnvState.getLocationState(currentLocation));
			s.setLocationState(adjacentLocation,
					vacEnvState.getLocationState(adjacentLocation));
			s.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
			results.add(s);
		} // case: suck
		else if (VacuumEnvironment.ACTION_SUCK == action) {
			// case: square is dirty
			if (VacuumEnvironment.LocationState.Dirty == vacEnvState
					.getLocationState(vacEnvState.getAgentLocation(this.agent))) {
				// always clean current
				VacuumEnvironmentState s1 = new VacuumEnvironmentState();
				s1.setLocationState(currentLocation,
						VacuumEnvironment.LocationState.Clean);
				s1.setLocationState(adjacentLocation,
						vacEnvState.getLocationState(adjacentLocation));
				s1.setAgentLocation(this.agent, currentLocation);
				results.add(s1);
				// sometimes clean adjacent as well
				VacuumEnvironmentState s2 = new VacuumEnvironmentState();
				s2.setLocationState(currentLocation,
						VacuumEnvironment.LocationState.Clean);
				s2.setLocationState(adjacentLocation,
						VacuumEnvironment.LocationState.Clean);
				s2.setAgentLocation(this.agent, currentLocation);
				results.add(s2);
			} // case: square is clean
			else {
				// sometimes do nothing
				VacuumEnvironmentState s1 = new VacuumEnvironmentState();
				s1.setLocationState(currentLocation,
						vacEnvState.getLocationState(currentLocation));
				s1.setLocationState(adjacentLocation,
						vacEnvState.getLocationState(adjacentLocation));
				s1.setAgentLocation(this.agent, currentLocation);
				results.add(s1);
				// sometimes deposit dirt
				VacuumEnvironmentState s2 = new VacuumEnvironmentState();
				s2.setLocationState(currentLocation,
						VacuumEnvironment.LocationState.Dirty);
				s2.setLocationState(adjacentLocation,
						vacEnvState.getLocationState(adjacentLocation));
				s2.setAgentLocation(this.agent, currentLocation);
				results.add(s2);
			}
		} else if (action.isNoOp()) {
			// do nothing
		}
		return results;
	}
}
