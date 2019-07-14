package aima.core.agent.impl.aprog;

import aima.core.agent.AgentProgram;
import aima.core.agent.Model;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.aprog.simplerule.Rule;

import java.util.Optional;
import java.util.Set;

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
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 * 
 */
public abstract class ModelBasedReflexAgentProgram<P, A> implements AgentProgram<P, A> {
	/// persistent: state, the agent's current conception of the world state
	private DynamicState state = null;
	/// model, a description of how the next state depends on current state and action
	private Model model = null;
	/// rules, a set of condition-action rules
	private Set<Rule<A>> rules = null;
	/// action, the most recent action, initially none
	private A action = null;

	protected ModelBasedReflexAgentProgram() {
		init();
	}

	/// function MODEL-BASED-REFLEX-AGENT(percept) returns an action
	public final Optional<A> apply(P percept) {
		state = updateState(state, action, percept, model);
		Rule<A> rule = ruleMatch(state, rules);
		action = (rule != null) ? rule.getAction() : null;
		return Optional.ofNullable(action);
	}

	/**
	 * Realizations of this class should implement the init() method so that it
	 * calls the setState(), setModel(), and setRules() method.
	 */
	protected abstract void init();

	protected abstract DynamicState updateState(DynamicState state, A action, P percept, Model model);

	private Rule<A> ruleMatch(DynamicState state, Set<Rule<A>> rules) {
		return rules.stream().filter(r -> r.evaluate(state)).findFirst().orElse(null);
	}

	/**
	 * Set the agent's current conception of the world state.
	 * 
	 * @param state
	 *            the agent's current conception of the world state.
	 */
	public void setState(DynamicState state) {
		this.state = state;
	}

	/**
	 * Set the program's description of how the next state depends on the state
	 * and action.
	 * 
	 * @param model
	 *            a description of how the next state depends on the current
	 *            state and action.
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * Set the program's condition-action rules
	 * 
	 * @param rules
	 *            a set of condition-action rules
	 */
	public void setRules(Set<Rule<A>> rules) {
		this.rules = rules;
	}
}
