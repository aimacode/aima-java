package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleAgent;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.aprog.SimpleReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 * @auhtor Ruediger Lunde
 * 
 */
public class SimpleReflexVacuumAgent extends SimpleAgent<DynamicPercept, Action> {

	public SimpleReflexVacuumAgent() {
		super(new SimpleReflexAgentProgram<DynamicPercept, Action>(getRuleSet()){
			@Override
			protected DynamicState interpretInput(DynamicPercept percept) {
				DynamicState state = new DynamicState();
				state.setAttribute(AttNames.CURRENT_LOCATION, percept.getAttribute(AttNames.CURRENT_LOCATION));
				state.setAttribute(AttNames.CURRENT_STATE, percept.getAttribute(AttNames.CURRENT_STATE));
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

		// Rules based on REFLEX-VACUUM-AGENT:
		// Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.8,
		// page 48.

		rules.add(new Rule<>(new EQUALCondition(AttNames.CURRENT_STATE, VacuumEnvironment.LocationState.Dirty),
				VacuumEnvironment.ACTION_SUCK));
		rules.add(new Rule<>(new EQUALCondition(AttNames.CURRENT_LOCATION, VacuumEnvironment.LOCATION_A),
				VacuumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule<>(new EQUALCondition(AttNames.CURRENT_LOCATION, VacuumEnvironment.LOCATION_B),
				VacuumEnvironment.ACTION_MOVE_LEFT));

		return rules;
	}
}
