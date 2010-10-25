package aima.core.environment.vacuum;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Model;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.NoOpAction;
import aima.core.agent.impl.aprog.ModelBasedReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.ANDCondition;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ModelBasedReflexVacuumAgent extends AbstractAgent {

	private static final String ATTRIBUTE_CURRENT_LOCATION = "currentLocation";
	private static final String ATTRIBUTE_CURRENT_STATE = "currentState";
	private static final String ATTRIBUTE_STATE_LOCATION_A = "stateLocationA";
	private static final String ATTRIBUTE_STATE_LOCATION_B = "stateLocationB";

	public ModelBasedReflexVacuumAgent() {
		super(new ModelBasedReflexAgentProgram() {
			@Override
			protected void init() {
				setState(new DynamicState());
				setRules(getRuleSet());
			}

			protected DynamicState updateState(DynamicState state,
					Action anAction, Percept percept, Model model) {

				VacuumEnvPercept vep = (VacuumEnvPercept) percept;

				state.setAttribute(ATTRIBUTE_CURRENT_LOCATION, vep
						.getAgentLocation());
				state.setAttribute(ATTRIBUTE_CURRENT_STATE, vep
						.getLocationState());
				// Keep track of the state of the different locations
				if (VacuumEnvironment.LOCATION_A == vep.getAgentLocation()) {
					state.setAttribute(ATTRIBUTE_STATE_LOCATION_A, vep
							.getLocationState());
				} else {
					state.setAttribute(ATTRIBUTE_STATE_LOCATION_B, vep
							.getLocationState());
				}
				return state;
			}
		});
	}

	//
	// PRIVATE METHODS
	//
	private static Set<Rule> getRuleSet() {
		// Note: Using a LinkedHashSet so that the iteration order (i.e. implied
		// precedence) of rules can be guaranteed.
		Set<Rule> rules = new LinkedHashSet<Rule>();

		rules.add(new Rule(new ANDCondition(new EQUALCondition(
				ATTRIBUTE_STATE_LOCATION_A,
				VacuumEnvironment.LocationState.Clean), new EQUALCondition(
				ATTRIBUTE_STATE_LOCATION_B,
				VacuumEnvironment.LocationState.Clean)), NoOpAction.NO_OP));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_CURRENT_STATE,
				VacuumEnvironment.LocationState.Dirty),
				VacuumEnvironment.ACTION_SUCK));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_CURRENT_LOCATION,
				VacuumEnvironment.LOCATION_A),
				VacuumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_CURRENT_LOCATION,
				VacuumEnvironment.LOCATION_B),
				VacuumEnvironment.ACTION_MOVE_LEFT));

		return rules;
	}
}
