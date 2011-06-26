package aima.core.agent.impl.aprog;

import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Model;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.NoOpAction;
import aima.core.agent.impl.aprog.simplerule.Rule;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.12, page
 * 51.<br>
 * <br>
 * 
 * <pre>
 * function MODEL-BASED-REFLEX-AGENT(percept) returns an action
 *   persistent: state, the agent's current conception of the world state
 *               model, a description of how the next state depends on current state and action
 *               rules, a set of condition-action rules
 *               action, the most recent action, initially none
 *               
 *   state  <- UPDATE-STATE(state, action, percept, model)
 *   rule   <- RULE-MATCH(state, rules)
 *   action <- rule.ACTION
 *   return action
 * </pre>
 * 
 * Figure 2.12 A model-based reflex agent. It keeps track of the current state
 * of the world using an internal model. It then chooses an action in the same
 * way as the reflex agent.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * 
 */
public abstract class ModelBasedReflexAgentProgram implements AgentProgram {
	//
	// persistent: state, the agent's current conception of the world state
	private DynamicState state = null;

	// model, a description of how the next state depends on current state and
	// action
	private Model model = null;

	// rules, a set of condition-action rules
	private Set<Rule> rules = null;

	// action, the most recent action, initially none
	private Action action = null;

	public ModelBasedReflexAgentProgram() {
		init();
	}

	/**
	 * Set the agent's current conception of the world state.
	 * 
	 * @param aState
	 *            the agent's current conception of the world state.
	 */
	public void setState(DynamicState aState) {
		state = aState;
	}

	/**
	 * Set the program's description of how the next state depends on the state
	 * and action.
	 * 
	 * @param aModel
	 *            a description of how the next state depends on the current
	 *            state and action.
	 */
	public void setModel(Model aModel) {
		model = aModel;
	}

	/**
	 * Set the program's condition-action rules
	 * 
	 * @param aRuleSet
	 *            a set of condition-action rules
	 */
	public void setRules(Set<Rule> aRuleSet) {
		rules = aRuleSet;
	}

	//
	// START-AgentProgram

	// function MODEL-BASED-REFLEX-AGENT(percept) returns an action
	public Action execute(Percept percept) {
		// state <- UPDATE-STATE(state, action, percept, model)
		state = updateState(state, action, percept, model);
		// rule <- RULE-MATCH(state, rules)
		Rule rule = ruleMatch(state, rules);
		// action <- rule.ACTION
		action = ruleAction(rule);
		// return action
		return action;
	}

	// END-AgentProgram
	//

	//
	// PROTECTED METHODS
	//

	/**
	 * Realizations of this class should implement the init() method so that it
	 * calls the setState(), setModel(), and setRules() method.
	 */
	protected abstract void init();

	protected abstract DynamicState updateState(DynamicState state,
			Action action, Percept percept, Model model);

	protected Rule ruleMatch(DynamicState state, Set<Rule> rules) {
		for (Rule r : rules) {
			if (r.evaluate(state)) {
				return r;
			}
		}
		return null;
	}

	protected Action ruleAction(Rule r) {
		return null == r ? NoOpAction.NO_OP : r.getAction();
	}
}
