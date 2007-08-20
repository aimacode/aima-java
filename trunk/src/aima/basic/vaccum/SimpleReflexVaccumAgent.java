package aima.basic.vaccum;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.basic.Agent;
import aima.basic.simplerule.EQUALCondition;
import aima.basic.simplerule.Rule;

/**
 * An example of using the aima.basic.SimpleReflexAgentProgram.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */

public class SimpleReflexVaccumAgent extends Agent {

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

		rules.add(new Rule(new EQUALCondition("status", "Dirty"), "Suck"));
		rules.add(new Rule(new EQUALCondition("location", "A"), "Right"));
		rules.add(new Rule(new EQUALCondition("location", "B"), "Left"));

		return rules;
	}
}
