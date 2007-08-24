package aima.basic.vaccum;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.basic.Agent;
import aima.basic.ObjectWithDynamicAttributes;
import aima.basic.Percept;
import aima.basic.simplerule.ANDCondition;
import aima.basic.simplerule.EQUALCondition;
import aima.basic.simplerule.Rule;

/**
 * An example of using the aima.basic.ReflexAgentWithStateProgram.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ReflexVaccumAgentWithState extends Agent {

	public ReflexVaccumAgentWithState() {
		super(new ReflexAgentWithStateProgram() {
			@Override
			protected void init() {
				setState(new ObjectWithDynamicAttributes());
				setRules(getRuleSet());
			}

			@Override
			protected ObjectWithDynamicAttributes updateState(
					ObjectWithDynamicAttributes envState, String anAction,
					Percept percept) {
				envState.setAttribute("currentLocation", percept
						.getAttribute("location"));
				envState.setAttribute("currentStatus", percept
						.getAttribute("status"));
				// Keep track of the status of the different locations
				envState.setAttribute((new StringBuffer("statusLocation")
						.append(percept.getAttribute("location"))).toString(),
						percept.getAttribute("status"));

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

		rules
				.add(new Rule(new ANDCondition(new EQUALCondition(
						"statusLocationA", "Clean"), new EQUALCondition(
						"statusLocationB", "Clean")),
						ReflexAgentWithStateProgram.NO_OP));
		rules
				.add(new Rule(new EQUALCondition("currentStatus", "Dirty"),
						"Suck"));
		rules
				.add(new Rule(new EQUALCondition("currentLocation", "A"),
						"Right"));
		rules.add(new Rule(new EQUALCondition("currentLocation", "B"), "Left"));

		return rules;
	}
}
