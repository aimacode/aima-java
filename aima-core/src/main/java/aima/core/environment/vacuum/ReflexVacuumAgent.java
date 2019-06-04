package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.impl.SimpleAgent;
import aima.core.agent.impl.DynamicPercept;

import java.util.Objects;
import java.util.Optional;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.8, page 48.<br>
 * <br>
 * 
 * <pre>
 * function REFLEX-VACUUM-AGENT([location, status]) returns an action
 *   
 *   if status = Dirty then return Suck
 *   else if location = A then return Right
 *   else if location = B then return Left
 * </pre>
 * 
 * Figure 2.8 The agent program for a simple reflex agent in the two-state
 * vacuum environment. This program implements the action function tabulated in
 * Figure 2.3.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class ReflexVacuumAgent extends SimpleAgent<DynamicPercept, Action> {

	public ReflexVacuumAgent() {
		super(new AgentProgram<DynamicPercept, Action>() {
			// function REFLEX-VACUUM-AGENT([location, status]) returns an action
			@Override
			public Optional<Action> apply(DynamicPercept percept) {
				Action action = null;
				// if status = Dirty then return Suck
				if (VacuumEnvironment.LocationState.Dirty == percept.getAttribute(AttNames.CURRENT_STATE)) {
					action = VacuumEnvironment.ACTION_SUCK;
					// else if location = A then return Right
				} else if (Objects.equals(VacuumEnvironment.LOCATION_A, percept.getAttribute(AttNames.CURRENT_LOCATION))) {
					action = VacuumEnvironment.ACTION_MOVE_RIGHT;
				} else if (Objects.equals(VacuumEnvironment.LOCATION_B, percept.getAttribute(AttNames.CURRENT_LOCATION))) {
					// else if location = B then return Left
					action = VacuumEnvironment.ACTION_MOVE_LEFT;
				}
				// Note: Empty should not be returned if the environment is correct
				return Optional.ofNullable(action);
			}
		});
	}
}
