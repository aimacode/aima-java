package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Model;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.aprog.ModelBasedReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.ANDCondition;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class ModelBasedReflexVacuumAgent extends AbstractAgent<Percept, Action> {

	public ModelBasedReflexVacuumAgent() {
		super(new ModelBasedReflexAgentProgram<Percept, Action>() {
			@Override
			protected void init() {
				setState(new DynamicState());
				setRules(getRuleSet());
			}

			protected DynamicState updateState(DynamicState state,
					Action anAction, Percept percept, Model model) {

				LocalVacuumEnvironmentPercept vep = (LocalVacuumEnvironmentPercept) percept;

				state.setAttribute(AttributeNames.CURRENT_LOCATION,
						vep.getAgentLocation());
				state.setAttribute(AttributeNames.CURRENT_STATE,
						vep.getLocationState());
				// Keep track of the state of the different locations
				if (Objects.equals(VacuumEnvironment.LOCATION_A, vep.getAgentLocation())) {
					state.setAttribute(AttributeNames.STATE_LOCATION_A,
							vep.getLocationState());
				} else {
					state.setAttribute(AttributeNames.STATE_LOCATION_B,
							vep.getLocationState());
				}
				return state;
			}
		});
	}

	//
	// PRIVATE METHODS
	//
	private static Set<Rule<Action>> getRuleSet() {
		// Note: Using a LinkedHashSet so that the iteration order (i.e. implied
		// precedence) of rules can be guaranteed.
		Set<Rule<Action>> rules = new LinkedHashSet<>();

		rules.add(new Rule<>(new ANDCondition(new EQUALCondition(
				AttributeNames.STATE_LOCATION_A,
				VacuumEnvironment.LocationState.Clean), new EQUALCondition(
				AttributeNames.STATE_LOCATION_B,
				VacuumEnvironment.LocationState.Clean)), null));
		rules.add(new Rule<>(new EQUALCondition(AttributeNames.CURRENT_STATE,
				VacuumEnvironment.LocationState.Dirty),
				VacuumEnvironment.ACTION_SUCK));
		rules.add(new Rule<>(new EQUALCondition(AttributeNames.CURRENT_LOCATION,
				VacuumEnvironment.LOCATION_A),
				VacuumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule<>(new EQUALCondition(AttributeNames.CURRENT_LOCATION,
				VacuumEnvironment.LOCATION_B),
				VacuumEnvironment.ACTION_MOVE_LEFT));

		return rules;
	}
}
