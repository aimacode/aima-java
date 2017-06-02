package aima.core.environment.vacuum;

import aima.core.agent.Action;

import java.util.*;

/**
 * Contains useful functions for the vacuum cleaner world.
 *
 * @author Ruediger Lunde
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
}
