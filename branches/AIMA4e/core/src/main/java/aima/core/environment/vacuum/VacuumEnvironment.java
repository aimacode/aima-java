package aima.core.environment.vacuum;

import aima.core.api.agent.Action;
import aima.core.api.agent.Environment;

/**
 * @author Ciaran O'Reilly
 */
public class VacuumEnvironment implements Environment {
    //
    // Allowable actions in the Vacuum World Environment
    public static final Action Left  = Action.newNamedAction("Left");
    public static final Action Right = Action.newNamedAction("Right");
    public static final Action Suck  = Action.newNamedAction("Suck");
    //
    // Two default locations mentioned in examples given in AIMA4e
    public static final String LOCATION_A = "A";
    public static final String LOCATION_B = "B";
    //
    // The status of a location in the environment
    public enum Status {
        Clean, Dirty
    }
}
