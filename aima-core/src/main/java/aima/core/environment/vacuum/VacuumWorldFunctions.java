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
     * Returns possible results
     */
    private static class VacuumWorldResults implements ResultsFunction<VacuumEnvironmentState, Action> {

        private Agent agent;

        VacuumWorldResults(Agent agent) {
            this.agent = agent;
        }

        /**
         * Returns a list of possible results for a given state and action
         *
         * @return a list of possible results for a given state and action.
         */
        @Override
        public List<VacuumEnvironmentState> results(VacuumEnvironmentState state, Action action) {
            // Ensure order is consistent across platforms.
            List<VacuumEnvironmentState> results = new ArrayList<>();
            String currentLocation = state.getAgentLocation(agent);
            String adjacentLocation = (currentLocation
                    .equals(VacuumEnvironment.LOCATION_A)) ? VacuumEnvironment.LOCATION_B
                    : VacuumEnvironment.LOCATION_A;
            // case: move right
            if (VacuumEnvironment.ACTION_MOVE_RIGHT == action) {
                VacuumEnvironmentState s = new VacuumEnvironmentState();
                s.setLocationState(currentLocation,
                        state.getLocationState(currentLocation));
                s.setLocationState(adjacentLocation,
                        state.getLocationState(adjacentLocation));
                s.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_B);
                results.add(s);
            } // case: move left
            else if (VacuumEnvironment.ACTION_MOVE_LEFT == action) {
                VacuumEnvironmentState s = new VacuumEnvironmentState();
                s.setLocationState(currentLocation,
                        state.getLocationState(currentLocation));
                s.setLocationState(adjacentLocation,
                        state.getLocationState(adjacentLocation));
                s.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
                results.add(s);
            } // case: suck
            else if (VacuumEnvironment.ACTION_SUCK == action) {
                // case: square is dirty
                if (VacuumEnvironment.LocationState.Dirty == state
                        .getLocationState(state.getAgentLocation(this.agent))) {
                    // always clean current
                    VacuumEnvironmentState s1 = new VacuumEnvironmentState();
                    s1.setLocationState(currentLocation,
                            VacuumEnvironment.LocationState.Clean);
                    s1.setLocationState(adjacentLocation,
                            state.getLocationState(adjacentLocation));
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
                            state.getLocationState(currentLocation));
                    s1.setLocationState(adjacentLocation,
                            state.getLocationState(adjacentLocation));
                    s1.setAgentLocation(this.agent, currentLocation);
                    results.add(s1);
                    // sometimes deposit dirt
                    VacuumEnvironmentState s2 = new VacuumEnvironmentState();
                    s2.setLocationState(currentLocation,
                            VacuumEnvironment.LocationState.Dirty);
                    s2.setLocationState(adjacentLocation,
                            state.getLocationState(adjacentLocation));
                    s2.setAgentLocation(this.agent, currentLocation);
                    results.add(s2);
                }
            }
            return results;
        }
    }
}
