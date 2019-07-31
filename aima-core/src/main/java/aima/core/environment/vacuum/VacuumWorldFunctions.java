package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import static aima.core.environment.vacuum.VacuumEnvironment.*;
import aima.core.search.nondeterministic.ResultsFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains useful functions for the vacuum cleaner world with two locations.
 *
 * @author Ruediger Lunde
 * @author Andrew Brown
 */
public class VacuumWorldFunctions {

	/**
	 * Specifies the actions available to the agent at state s
	 */
	public static List<Action> getActions(VacuumEnvironmentState state) {
		return Arrays.asList(ACTION_SUCK, ACTION_MOVE_LEFT, ACTION_MOVE_RIGHT);
	}

	public static boolean testGoal(VacuumEnvironmentState state) {
		return state.getLocationState(LOCATION_A) == LocationState.Clean
				&& state.getLocationState(LOCATION_B) == LocationState.Clean;
	}

	/**
	 * Returns a function which maps possible state-action-pairs to a lists of possible successor states
	 * for the non-deterministic vacuum world.
	 */
	public static ResultsFunction<VacuumEnvironmentState, Action> createResultsFunctionFor(final Agent agent) {
		return (VacuumEnvironmentState state, Action action) -> {
			List<VacuumEnvironmentState> results = new ArrayList<>();
			// add clone of state to results, modify later...
			VacuumEnvironmentState s = state.clone();
			results.add(s);

			String currentLocation = state.getAgentLocation(agent);
			String adjacentLocation = (currentLocation.equals(LOCATION_A)) ? LOCATION_B : LOCATION_A;

			if (action == ACTION_MOVE_RIGHT) {
				s.setAgentLocation(agent, LOCATION_B);
			} else if (action == ACTION_MOVE_LEFT) {
				s.setAgentLocation(agent, LOCATION_A);
			} else if (action == ACTION_SUCK) {
				if (state.getLocationState(currentLocation) == LocationState.Dirty) {
					// always clean current
					s.setLocationState(currentLocation, LocationState.Clean);
					// sometimes clean adjacent as well
					VacuumEnvironmentState s2 = s.clone();
					s2.setLocationState(adjacentLocation, LocationState.Clean);
					if (!s2.equals(s))
						results.add(s2);
				} else {
					// sometimes do nothing (-> s unchanged)
					// sometimes deposit dirt
					VacuumEnvironmentState s2 = s.clone();
					s2.setLocationState(currentLocation, LocationState.Dirty);
					if (!s2.equals(s))
						results.add(s2);
				}
			}
			return results;
		};
	}

	/**
	 * Maps a vacuum world percept of an agent to the corresponding vacuum environment state.
	 * @param agent The perceiving agent.
	 */
	public static VacuumEnvironmentState getState(VacuumPercept percept, Agent<?, ?> agent) {
		VacuumEnvironmentState state = new VacuumEnvironmentState();
		state.setAgentLocation(agent, percept.getCurrLocation());
		state.setLocationState(LOCATION_A, (LocationState) percept.getAttribute(LOCATION_A));
		state.setLocationState(LOCATION_B, (LocationState) percept.getAttribute(LOCATION_B));
		return state;
	}
}
