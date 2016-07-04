package aima.core.environment.vacuum;

import aima.core.agent.api.Agent;

import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_LEFT;
import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_RIGHT;
import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_SUCK;
import static aima.core.environment.vacuum.VacuumEnvironment.LOCATION_A;
import static aima.core.environment.vacuum.VacuumEnvironment.LOCATION_B;
import static aima.core.environment.vacuum.VacuumEnvironment.Status.Dirty;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function REFLEX-VACUUM-AGENT([location, status]) returns an action
 *   if status = Dirty then return Suck
 *   else if location = A then return Right
 *   else if location = B then return Left
 * </pre>
 *
 * Figure ?? The agent program for a simple reflex agent in the two-state vacuum
 * environment. This program implements the action function tabulated in Figure
 * ??.
 *
 * @author Ciaran O'Reilly
 */
public class ReflexVacuumAgent implements Agent<String, VEPercept> {

	// function REFLEX-VACUUM-AGENT([location, status]) returns an action
	@Override
	public String perceive(VEPercept percept) {
		// if status = Dirty then return Suck
		if (percept.status == Dirty) {
			return ACTION_SUCK;
		} // else if location = A then return Right
		else if (percept.location.equals(LOCATION_A)) {
			return ACTION_RIGHT;
		} // else if location = B then return Left
		else if (percept.location.equals(LOCATION_B)) {
			return ACTION_LEFT;
		}

		// NOTE: if environment is as described in aima4e this should not be
		// reached
		return null;
	}
}
