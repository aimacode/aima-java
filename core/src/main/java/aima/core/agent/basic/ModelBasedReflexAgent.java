package aima.core.agent.basic;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import aima.core.agent.api.Agent;
import aima.core.agent.api.Rule;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function MODEL-BASED-REFLEX-AGENT(percept) returns an action
 *   persistent: state, the agent's current conception of the world state
 *               model, a description of how the next state depends on current state and action
 *               rules, a set of condition-action rules
 *               action, the most recent action, initially none
 *
 *   state  &larr; UPDATE-STATE(state, action, percept, model)
 *   rule   &larr; RULE-MATCH(state, rules)
 *   action &larr; rule.ACTION
 *   return action
 * </pre>
 *
 * Figure ?? A model-based reflex agent. It keeps track of the current state of
 * the world using an internal model. It then chooses an action in the same way
 * as the reflex agent.
 *
 * @param <S>
 *            the type of internal state representation used by the agent.
 * @param <M>
 *            the type of internal model representation used by the agent.
 *
 * @author Ciaran O'Reilly
 */
public class ModelBasedReflexAgent<A, P, S, M> implements Agent<A, P> {
	// persistent: 
	private S state; // the agent's current conception of the world state
	private M model; // a description of how the next state depends on current state and action
	private Set<Rule<A, S>> rules = new LinkedHashSet<>(); // a set of condition-action rules
	private A action = null; // the most recent action, initially none

	// function MODEL-BASED-REFLEX-AGENT(percept) returns an action
	@Override
	public A perceive(P percept) {
		// state <- UPDATE-STATE(state, action, percept, model)
		setState(updateState(getState(), getAction(), percept, getModel()));
		// rule <- RULE-MATCH(state, rules)
		Optional<Rule<A, S>> rule = ruleMatch(getState(), getRules());
		// action <- rule.ACTION
		setAction(rule.isPresent() ? rule.get().action() : null);
		// return action
		return getAction();
	}

	// state <- UPDATE-STATE(state, action, percept, model)
	public S updateState(S currentState, A mostRecentAction, P percept, M model) {
		return this.updateStateFn.apply(currentState, mostRecentAction, percept, model);
	}

	// rule <- RULE-MATCH(state, rules)
	public Optional<Rule<A, S>> ruleMatch(S state, Set<Rule<A, S>> rules) {
		return getRules().stream().filter(rule -> rule.condition().test(state)).findFirst();
	}

	//
	// Supporting Code
	@FunctionalInterface
	public interface UpdateStateFunction<S, A, P, M> {
		S apply(S currentState, A mostRecentAction, P percept, M model);
	}
	
	// Make composable the update state logic
	// state <- UPDATE-STATE(state, action, percept, model)
	private UpdateStateFunction<S, A, P, M> updateStateFn; 

	public ModelBasedReflexAgent(S state, M model, Collection<Rule<A, S>> rules,
			UpdateStateFunction<S, A, P, M> updateStateFn) {
		this.state = state;
		this.model = model;
		this.rules.addAll(rules);
		//
		this.updateStateFn = updateStateFn;
	}

	//
	// Getters and Setters
	public S getState() {
		return state;
	}

	public void setState(S state) {
		this.state = state;
	}

	//
	public M getModel() {
		return model;
	}

	//
	public Set<Rule<A, S>> getRules() {
		return rules;
	}

	//
	public A getAction() {
		return action;
	}

	public void setAction(A action) {
		this.action = action;
	}
}
