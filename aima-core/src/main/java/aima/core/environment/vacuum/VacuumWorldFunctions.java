package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.search.nondeterministic.ResultsFunction;

import java.util.*;

/**
 * Contains useful functions for the vacuum cleaner world.
 *
 * @author Ruediger Lunde
 * @author Andrew Brown
 */
public class VacuumWorldFunctions {

    /**
     * Specifies the actions available to the agent at state s
     */
    public static List<Action> getActions(VacuumEnvironmentState state) {
        List<Action> actions = new ArrayList<>();
        actions.add(VacuumEnvironment.ACTION_SUCK);
        actions.add(VacuumEnvironment.ACTION_MOVE_LEFT);
        actions.add(VacuumEnvironment.ACTION_MOVE_RIGHT);
        // Ensure cannot be modified.
       return Collections.unmodifiableList(actions);
    }

    public static boolean testGoal(VacuumEnvironmentState state) {
        return state.getLocationState(VacuumEnvironment.LOCATION_A) == VacuumEnvironment.LocationState.Clean
                && state.getLocationState(VacuumEnvironment.LOCATION_B) == VacuumEnvironment.LocationState.Clean;
    }

    public static ResultsFunction<VacuumEnvironmentState, Action> createResultsFunction(Agent agent) {
        return new VacuumWorldResults(agent);
    }

    /**
     * Returns possible results.
     */
    private static class VacuumWorldResults implements ResultsFunction<VacuumEnvironmentState, Action> {
        private Agent agent;

        VacuumWorldResults(Agent agent) {
            this.agent = agent;
        }

        /**
         * Returns a list of possible results for a given state and action.
         */
        @Override
        public List<VacuumEnvironmentState> results(VacuumEnvironmentState state, Action action) {
            List<VacuumEnvironmentState> results = new ArrayList<>();
            // add clone of state to results, modify later...
            VacuumEnvironmentState s = state.clone();
            results.add(s);

            String currentLocation = state.getAgentLocation(agent);
            String adjacentLocation = (currentLocation.equals(VacuumEnvironment.LOCATION_A))
                    ? VacuumEnvironment.LOCATION_B : VacuumEnvironment.LOCATION_A;

            if (action == VacuumEnvironment.ACTION_MOVE_RIGHT) {
                s.setAgentLocation(agent, VacuumEnvironment.LOCATION_B);

            } else if (action == VacuumEnvironment.ACTION_MOVE_LEFT) {
                s.setAgentLocation(agent, VacuumEnvironment.LOCATION_A);

            } else if (action == VacuumEnvironment.ACTION_SUCK) {
                if (state.getLocationState(currentLocation) == VacuumEnvironment.LocationState.Dirty) {
                    // always clean current
                    s.setLocationState(currentLocation, VacuumEnvironment.LocationState.Clean);
                    // sometimes clean adjacent as well
                    VacuumEnvironmentState s2 = s.clone();
                    s2.setLocationState(adjacentLocation, VacuumEnvironment.LocationState.Clean);
                    if (!s2.equals(s))
                        results.add(s2);
                } else {
                    // sometimes do nothing (-> s unchanged)
                    // sometimes deposit dirt
                    VacuumEnvironmentState s2 = s.clone();
                    s2.setLocationState(currentLocation, VacuumEnvironment.LocationState.Dirty);
                    if (!s2.equals(s))
                        results.add(s2);
                }
            }
            return results;
        }
    }
}
