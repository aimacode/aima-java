package aima.core.agent.impl.vacuum;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.aprog.SimpleReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class SimpleReflexVaccumAgent extends AbstractAgent {

	public SimpleReflexVaccumAgent() {
		super(new SimpleReflexAgentProgram(getRuleSet()));
	}

	//
	// PRIVATE METHODS
	//
	private static Set<Rule> getRuleSet() {
		// Note: Using a LinkedHashSet so that the iteration order (i.e. implied
		// precedence) of rules can be guaranteed.
		Set<Rule> rules = new LinkedHashSet<Rule>();

		// Rules based on REFLEX-VACUUM-AGENT:
		// Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.8,
		// page 46.

		rules.add(new Rule(new EQUALCondition(VaccumEnvPercept.ATTRIBUTE_STATE,
				VaccumEnvironment.LocationState.Dirty),
				VaccumEnvironment.ACTION_SUCK));
		rules.add(new Rule(new EQUALCondition(
				VaccumEnvPercept.ATTRIBUTE_AGENT_LOCATION,
				VaccumEnvironment.Location.A),
				VaccumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule(new EQUALCondition(
				VaccumEnvPercept.ATTRIBUTE_AGENT_LOCATION,
				VaccumEnvironment.Location.B),
				VaccumEnvironment.ACTION_MOVE_LEFT));

		return rules;
	}
}
