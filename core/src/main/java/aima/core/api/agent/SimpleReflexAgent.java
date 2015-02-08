package aima.core.api.agent;

import java.util.Optional;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function SIMPLE-RELEX-AGENT(percept) returns an action
 *   persistent: rules, a set of condition-action rules
 *
 *   state  <- INTERPRET-INPUT(percept)
 *   rule   <- RULE-MATCH(state, rules)
 *   action <- rule.ACTION
 *   return action
 * </pre>
 *
 * Figure 2.10 A simple reflex agent. It acts according to a rule whose
 * condition matches the current state, as defined by the percept.
 *
 * @param <S> the type of internal state representation used by the agent.
 *
 * @author Ciaran O'Reilly
 */
public interface SimpleReflexAgent<P extends Percept, S> extends Agent<P> {
    // persistent: rules, a set of condition-action rules
    Set<Rule<S>> rules();

    // function SIMPLE-RELEX-AGENT(percept) returns an action
    @Override
    default Action perceive(P percept) {
        // state  <- INTERPRET-INPUT(percept)
        S state = interpretInput(percept);
        // rule   <- RULE-MATCH(state, rules)
        Optional<Rule<S>> rule = ruleMatch(state, rules());
        // action <- rule.ACTION
        Action action = rule.isPresent() ? rule.get().action() : Action.NoOp;
        // return action
        return action;
    }

    S interpretInput(P percept);

    default Optional<Rule<S>> ruleMatch(S state, Set<Rule<S>> rules) {
        return rules.stream().filter(r -> r.condition().test(state)).findFirst();
    }
}
