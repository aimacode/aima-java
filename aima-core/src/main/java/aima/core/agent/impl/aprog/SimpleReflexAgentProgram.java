package aima.core.agent.impl.aprog;

import aima.core.agent.AgentProgram;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.ObjectWithDynamicAttributes;
import aima.core.agent.impl.aprog.simplerule.Rule;

import java.util.Optional;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.10, page
 * 49.<br>
 * <br>
 * 
 * <pre>
 * function SIMPLE-RELEX-AGENT(percept) returns an action
 *   persistent: rules, a set of condition-action rules
 *   
 *   state  <- INTERPRET-INPUT(percept);
 *   rule   <- RULE-MATCH(state, rules);
 *   action <- rule.ACTION;
 *   return action
 * </pre>
 * 
 * Figure 2.10 A simple reflex agent. It acts according to a rule whose
 * condition matches the current state, as defined by the percept.
 *
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 * 
 */
public abstract class SimpleReflexAgentProgram<P, A> implements AgentProgram<P, A> {
	//
	// persistent: rules, a set of condition-action rules
	private Set<Rule<A>> rules;

	/**
	 * Constructs a SimpleReflexAgentProgram with a set of condition-action
	 * rules.
	 * 
	 * @param ruleSet
	 *            a set of condition-action rules
	 */
	protected SimpleReflexAgentProgram(Set<Rule<A>> ruleSet) {
		rules = ruleSet;
	}

	//
	// START-AgentProgram

	// function SIMPLE-RELEX-AGENT(percept) returns an action
	@Override
	public Optional<A> execute(P percept) {

		// state <- INTERPRET-INPUT(percept);
		DynamicState state = interpretInput(percept);
		// rule <- RULE-MATCH(state, rules);
		Rule<A> rule = ruleMatch(state, rules);
		// action <- rule.ACTION;
		// return action
		return Optional.ofNullable(ruleAction(rule));
	}

	// END-AgentProgram
	//

	//
	// PROTECTED METHODS
	//
	protected abstract DynamicState interpretInput(P p);

	protected Rule<A> ruleMatch(ObjectWithDynamicAttributes state, Set<Rule<A>> rulesSet) {
		for (Rule<A> r : rulesSet) {
			if (r.evaluate(state))
				return r;
		}
		return null;
	}

	protected A ruleAction(Rule<A> r) {
		return r != null ? r.getAction() : null;
	}
}
