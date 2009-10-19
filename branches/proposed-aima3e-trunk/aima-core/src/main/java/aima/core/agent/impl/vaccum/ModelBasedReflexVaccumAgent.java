package aima.core.agent.impl.vaccum;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicModel;
import aima.core.agent.impl.NoOpAction;
import aima.core.agent.impl.aprog.ModelBasedReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.ANDCondition;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ModelBasedReflexVaccumAgent extends AbstractAgent {

	private static final String ATTRIBUTE_CURRENT_LOCATION = "currentLocation";
	private static final String ATTRIBUTE_CURRENT_STATE = "currentState";
	private static final String ATTRIBUTE_STATE_LOCATION_A = "stateLocationA";
	private static final String ATTRIBUTE_STATE_LOCATION_B = "stateLocationB";

	public ModelBasedReflexVaccumAgent() {
		super(new ModelBasedReflexAgentProgram() {
			@Override
			protected void init() {
				setState(new DynamicModel());
				setRules(getRuleSet());
			}

			protected DynamicModel updateState(DynamicModel envState,
					Action anAction, Percept percept) {

				VaccumEnvPercept vep = (VaccumEnvPercept) percept;

				envState.setAttribute(ATTRIBUTE_CURRENT_LOCATION, vep
						.getAgentLocation());
				envState.setAttribute(ATTRIBUTE_CURRENT_STATE, vep
						.getLocationState());
				// Keep track of the state of the different locations
				if (VaccumEnvironment.Location.A == vep.getAgentLocation()) {
					envState.setAttribute(ATTRIBUTE_STATE_LOCATION_A, vep
							.getLocationState());
				} else {
					envState.setAttribute(ATTRIBUTE_STATE_LOCATION_B, vep
							.getLocationState());
				}
				return envState;
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
				VaccumEnvironment.LocationState.Clean), new EQUALCondition(
				ATTRIBUTE_STATE_LOCATION_B,
				VaccumEnvironment.LocationState.Clean)), NoOpAction.NO_OP));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_CURRENT_STATE,
				VaccumEnvironment.LocationState.Dirty),
				VaccumEnvironment.ACTION_SUCK));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_CURRENT_LOCATION,
				VaccumEnvironment.Location.A),
				VaccumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_CURRENT_LOCATION,
				VaccumEnvironment.Location.B),
				VaccumEnvironment.ACTION_MOVE_LEFT));

		return rules;
	}
}
