package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Model;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicPercept;
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
public class ModelBasedReflexVacuumAgent extends AbstractAgent<DynamicPercept, Action> {

	public ModelBasedReflexVacuumAgent() {
		super(new ModelBasedReflexAgentProgram<DynamicPercept, Action>() {
			@Override
			protected void init() {
				setState(new DynamicState());
				setRules(getRuleSet());
			}

			protected DynamicState updateState(DynamicState state,
					Action anAction, DynamicPercept percept, Model model) {

				Object loc = percept.getAttribute(AttNames.CURRENT_LOCATION);
				Object locState = percept.getAttribute(AttNames.CURRENT_STATE);
				state.setAttribute(AttNames.CURRENT_LOCATION, loc);
				state.setAttribute(AttNames.CURRENT_STATE, locState);
				// Keep track of the state of the different locations
				if (Objects.equals(VacuumEnvironment.LOCATION_A, loc))
					state.setAttribute(AttNames.STATE_LOCATION_A, locState);
				else
					state.setAttribute(AttNames.STATE_LOCATION_B, locState);
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

		rules.add(new Rule<>(new ANDCondition
				(new EQUALCondition(AttNames.STATE_LOCATION_A, VacuumEnvironment.LocationState.Clean),
				new EQUALCondition(AttNames.STATE_LOCATION_B, VacuumEnvironment.LocationState.Clean)),
				null));
		rules.add(new Rule<>(new EQUALCondition(AttNames.CURRENT_STATE, VacuumEnvironment.LocationState.Dirty),
				VacuumEnvironment.ACTION_SUCK));
		rules.add(new Rule<>(new EQUALCondition(AttNames.CURRENT_LOCATION, VacuumEnvironment.LOCATION_A),
				VacuumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule<>(new EQUALCondition(AttNames.CURRENT_LOCATION, VacuumEnvironment.LOCATION_B),
				VacuumEnvironment.ACTION_MOVE_LEFT));

		return rules;
	}
}
