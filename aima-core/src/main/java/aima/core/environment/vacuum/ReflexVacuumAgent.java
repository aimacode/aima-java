package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;

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
public class ReflexVacuumAgent extends AbstractAgent<Percept, Action> {

	public ReflexVacuumAgent() {
		super(new AgentProgram<Percept, Action>() {
			// function REFLEX-VACUUM-AGENT([location, status]) returns an
			// action
			public Optional<Action> execute(Percept percept) {
				LocalVacuumEnvironmentPercept vep = (LocalVacuumEnvironmentPercept) percept;
				Action action = null;
				// if status = Dirty then return Suck
				if (VacuumEnvironment.LocationState.Dirty == vep
						.getLocationState()) {
					action = VacuumEnvironment.ACTION_SUCK;
					// else if location = A then return Right
				} else if (Objects.equals(VacuumEnvironment.LOCATION_A, vep
                        .getAgentLocation())) {
					action = VacuumEnvironment.ACTION_MOVE_RIGHT;
				} else if (Objects.equals(VacuumEnvironment.LOCATION_B, vep
						.getAgentLocation())) {
					// else if location = B then return Left
					action = VacuumEnvironment.ACTION_MOVE_LEFT;
				}
				// Note: Empty should not be returned if the environment is correct
				return Optional.ofNullable(action);
			}
		});
	}
}
