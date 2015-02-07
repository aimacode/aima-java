package aima.core.environment.vacuum;

import aima.core.api.agent.Action;
import aima.core.api.agent.Environment;
import aima.core.environment.support.NamedAction;

/**
 * @author Ciaran O'Reilly
 */
public class VacuumEnvironment implements Environment {
    //
    // Allowable actions in the Vacuum World Environment
    public static final Action ACTION_MOVE_LEFT  = new NamedAction("Left");
    public static final Action ACTION_MOVE_RIGHT = new NamedAction("Right");
    public static final Action ACTION_SUCK       = new NamedAction("Suck");
    //
    // Two default locations mentioned in examples given in AIMA4e
    public static final String LOCATION_A = "A";
    public static final String LOCATION_B = "B";
    //
    //
    public enum LocationState {
        Clean, Dirty
    };
}
