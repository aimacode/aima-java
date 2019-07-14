package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Model;
import aima.core.agent.impl.SimpleAgent;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.aprog.ModelBasedReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.ANDCondition;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;
import static aima.core.environment.vacuum.VacuumEnvironment.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class ModelBasedReflexVacuumAgent extends SimpleAgent<VacuumPercept, Action> {

	private static final String ATT_CURRENT_LOCATION = "currentLocation";
	private static final String ATT_CURRENT_STATE = "currentState";
	private static final String ATT_STATE_LOCATION_A = "stateLocationA";
	private static final String ATT_STATE_LOCATION_B = "stateLocationB";
	
	public ModelBasedReflexVacuumAgent() {
		super(new ModelBasedReflexAgentProgram<VacuumPercept, Action>() {
			@Override
			protected void init() {
				setState(new DynamicState());
				setRules(getRuleSet());
				// the model attribute is not used here!
			}

			@Override
			protected DynamicState updateState(DynamicState state, Action anAction, VacuumPercept percept,
											   Model model) {
				Object currLocation = percept.getCurrLocation();
				Object currState = percept.getCurrState();
				state.setAttribute(ATT_CURRENT_LOCATION, currLocation);
				state.setAttribute(ATT_CURRENT_STATE, currState);
				// Keep track of the state of the different locations
				if (LOCATION_A.equals(currLocation))
					state.setAttribute(ATT_STATE_LOCATION_A, currState);
				if (LOCATION_B.equals(currLocation))
					state.setAttribute(ATT_STATE_LOCATION_B, currState);
				return state;
			}
		});
	}

	private static Set<Rule<Action>> getRuleSet() {
		// Note: LinkedHashSet preserves iteration order (i.e. implied precedence).
		Set<Rule<Action>> rules = new LinkedHashSet<>();

		rules.add(new Rule<>(new ANDCondition(
				new EQUALCondition(ATT_STATE_LOCATION_A, LocationState.Clean),
				new EQUALCondition(ATT_STATE_LOCATION_B, LocationState.Clean)), null)
		);
		rules.add(new Rule<>(new EQUALCondition(ATT_CURRENT_STATE, LocationState.Dirty), ACTION_SUCK));
		rules.add(new Rule<>(new EQUALCondition(ATT_CURRENT_LOCATION, LOCATION_A), ACTION_MOVE_RIGHT));
		rules.add(new Rule<>(new EQUALCondition(ATT_CURRENT_LOCATION, LOCATION_B), ACTION_MOVE_LEFT));

		return rules;
	}
}
