package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleAgent;
import static aima.core.environment.vacuum.VacuumEnvironment.*;

import java.util.Optional;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.8, page 48.<br>
 * <br>
 * <pre>
 * function REFLEX-VACUUM-AGENT([location, status]) returns an action
 *   if status = Dirty then return Suck
 *   else if location = A then return Right
 *   else if location = B then return Left
 * </pre>
 * Figure 2.8 The agent program for a simple reflex agent in the two-state
 * vacuum environment. This program implements the action function tabulated in
 * Figure 2.3.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class ReflexVacuumAgent extends SimpleAgent<VacuumPercept, Action> {

	public ReflexVacuumAgent() {
		super(percept -> {
			Action action = null;
			if (LocationState.Dirty == percept.getCurrState())
				action = ACTION_SUCK;
			else if (LOCATION_A.equals(percept.getCurrLocation()))
				action = ACTION_MOVE_RIGHT;
			else if (LOCATION_B.equals(percept.getCurrLocation()))
				action = ACTION_MOVE_LEFT;
			// Note: Empty should not be returned if the environment is correct
			return Optional.ofNullable(action);
		});
	}
}
