package aima.core.agent.basic;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import aima.core.agent.api.Agent;
import aima.core.agent.api.Rule;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function SIMPLE-RELEX-AGENT(percept) returns an action
 *   persistent: rules, a set of condition-action rules
 *
 *   state  &larr; INTERPRET-INPUT(percept)
 *   rule   &larr; RULE-MATCH(state, rules)
 *   action &larr; rule.ACTION
 *   return action
 * </pre>
 *
 * Figure ?? A simple reflex agent. It acts according to a rule whose condition
 * matches the current state, as defined by the percept.
 *
 * @param <S>
 *            the type of internal state representation used by the agent.
 *
 * @author Ciaran O'Reilly
 */
public class SimpleReflexAgent<A, P, S> implements Agent<A, P> {
	// persistent: rules, a set of condition-action rules
	private Set<Rule<A, S>> rules = new LinkedHashSet<>();

	// function SIMPLE-RELEX-AGENT(percept) returns an action
	@Override
	public A perceive(P percept) {
		// state <- INTERPRET-INPUT(percept)
		S state = interpretInput(percept);
		// rule <- RULE-MATCH(state, rules)
		Optional<Rule<A, S>> rule = ruleMatch(state, getRules());
		// action <- rule.ACTION
		A action = rule.isPresent() ? rule.get().action() : null;
		// return action
		return action;
	}

	// state <- INTERPRET-INPUT(percept)
	public S interpretInput(P percept) {
		return this.interpretInputFn.apply(percept);
	}

	// rule <- RULE-MATCH(state, rules)
	public Optional<Rule<A, S>> ruleMatch(S state, Set<Rule<A, S>> rules) {
		return getRules().stream().filter(rule -> rule.condition().test(state)).findFirst();
	}
	
	//
	// Supporting Code
	//
	// Make composable the interpret input logic
	// state <- INTERPRET-INPUT(percept)
	private Function<P, S> interpretInputFn = null;
	
	public SimpleReflexAgent(Collection<Rule<A, S>> rules, Function<P, S> interpretInput) {
		this.rules.addAll(rules);
		this.interpretInputFn = interpretInput;
	}

	//
	// Getters
	public Set<Rule<A, S>> getRules() {
		return rules;
	}
}
