package aima.basic.vaccum;

import java.util.Set;

import aima.basic.AgentProgram;
import aima.basic.ObjectWithDynamicAttributes;
import aima.basic.Percept;
import aima.basic.simplerule.Rule;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.10, page 47.
 * <code>
 * function SIMPLE-RELEX-AGENT(percept) returns an action
 *   static: rules, a set of condition-action rules
 *   
 *   state  <- INTERPRET-INPUT(percept);
 *   rule   <- RULE-MATCH(state, rules);
 *   action <- RULE-ACTION(rule);
 *   return action
 * </code>
 * Figure 2.10 A simple reflex agent. It acts according to a rule whose condition matches
 * the current state, as defined by the percept.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */

public class SimpleReflexAgentProgram extends AgentProgram {
	// Used to define No Operations/Action is to be performed.
	public static final String NO_OP = "NoOP";

	//
	// static: rules, a set of condition-action rules
	private Set<Rule> rules;

	public SimpleReflexAgentProgram(Set<Rule> aRuleSet) {
		rules = aRuleSet;
	}

	// function SIMPLE-RELEX-AGENT(percept) returns an action
	@Override
	public String execute(Percept percept) {

		// state <- INTERPRET-INPUT(percept);
		ObjectWithDynamicAttributes state = interpretInput(percept);
		// rule <- RULE-MATCH(state, rules);
		Rule rule = ruleMatch(state, rules);
		// action <- RULE-ACTION(rule);
		// return action
		return ruleAction(rule);
	}

	//
	// PROTECTED METHODS
	//
	protected ObjectWithDynamicAttributes interpretInput(Percept p) {
		return p;
	}

	protected Rule ruleMatch(ObjectWithDynamicAttributes state,
			Set<Rule> rulesSet) {
		for (Rule r : rulesSet) {
			if (r.evaluate(state)) {
				return r;
			}
		}
		return null;
	}

	protected String ruleAction(Rule r) {
		return null == r ? NO_OP : r.getAction();
	}
}
