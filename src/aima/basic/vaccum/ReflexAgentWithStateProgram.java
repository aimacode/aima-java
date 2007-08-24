package aima.basic.vaccum;

import java.util.Set;

import aima.basic.AgentProgram;
import aima.basic.ObjectWithDynamicAttributes;
import aima.basic.Percept;
import aima.basic.simplerule.Rule;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.12, page 49.
 * <code>
 * function REFLEX-AGENT-WITH-STATE(percept) returns an action
 *   static state, a description of the current world state
 *          rules, a set of condition-action rules
 *          action, the most recent action, initially none
 *   
 *   state  <- UPDATE-STATE(state, action, percept)
 *   rule   <- RULE-MATCH(state, rules)
 *   action <- RULE-ACTION(rule)
 *   return action
 * </code>
 * Figure 2.12 A model-based reflex agent. It keeps track of the current state of the world
 * using an internal model. It then chooses an action in the same way as the reflex agent.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class ReflexAgentWithStateProgram extends AgentProgram {
	// Used to define No Operations/Action is to be performed.
	public static final String NO_OP = "NoOP";

	//
	// static state, a description of the current world state
	private ObjectWithDynamicAttributes state = null;

	// rules, a set of condition-action rules
	private Set<Rule> rules = null;

	// action, the most recent action, initially none
	private String action = null;

	public ReflexAgentWithStateProgram() {
		init();
		// Implementors of the init() method should have ensured the state and
		// rules are setup
		assert (null != state);
		assert (null != rules);
	}

	public void setState(ObjectWithDynamicAttributes aState) {
		state = aState;
	}

	public void setRules(Set<Rule> aRuleSet) {
		rules = aRuleSet;
	}

	// function REFLEX-AGENT-WITH-STATE(percept) returns an action
	@Override
	public String execute(Percept percept) {
		// state <- UPDATE-STATE(state, action, percept)
		state = updateState(state, action, percept);
		// rule <- RULE-MATCH(state, rules)
		Rule rule = ruleMatch(state, rules);
		// action <- RULE-ACTION(rule)
		action = ruleAction(rule);
		// return action
		return action;
	}

	//
	// PROTECTED METHODS
	//

	/**
	 * Realizations of this class should implement the init() method so that it
	 * calls the setState() and setRules() method.
	 */
	protected abstract void init();

	protected abstract ObjectWithDynamicAttributes updateState(
			ObjectWithDynamicAttributes envState, String anAction,
			Percept percept);

	protected Rule ruleMatch(ObjectWithDynamicAttributes envState,
			Set<Rule> rulesSet) {
		for (Rule r : rulesSet) {
			if (r.evaluate(envState)) {
				return r;
			}
		}
		return null;
	}

	protected String ruleAction(Rule r) {
		return null == r ? NO_OP : r.getAction();
	}
}
