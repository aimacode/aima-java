package aima.core.environment.vacuum.agent;

import aima.core.api.agent.Agent;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.perceive.LocalPercept;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function REFLEX-VACUUM-AGENT([location, status]) returns an action
 *   if status = Dirty then return Suck
 *   else if location = A then return Right
 *   else if location = B then return Left
 * </pre>
 *
 * Figure ?? The agent program for a simple reflex agent in the two-state
 * vacuum environment. This program implements the action function tabulated in
 * Figure ??.
 *
 * @author Ciaran O'Reilly
 */
public class ReflexVacuumAgent implements Agent<String, LocalPercept> {

    // function REFLEX-VACUUM-AGENT([location, status]) returns an action
    @Override
    public String perceive(LocalPercept percept) {
        // if status = Dirty then return Suck
        if (percept.status == VacuumEnvironment.Status.Dirty) {
            return VacuumEnvironment.ACTION_SUCK;
        } // else if location = A then return Right
        else if (percept.location.equals(VacuumEnvironment.LOCATION_A)) {
            return VacuumEnvironment.ACTION_RIGHT;
        } // else if location = B then return Left
        else if (percept.location.equals(VacuumEnvironment.LOCATION_B)) {
            return VacuumEnvironment.ACTION_LEFT;
        }

        // NOTE: if environment is as described in aima4e this should not be reached
        return null;
    }
}
