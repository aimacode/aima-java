package aima.core.agent;

import aima.core.api.agent.Rule;
import aima.core.api.agent.SimpleReflexAgent;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Ciaran O'Reilly
 */
public class BasicSimpleReflexAgent<P, S> implements SimpleReflexAgent<P, S> {
    private Function<P, S> interpretInputFn = null;
    private Set<Rule<S>> rules = new LinkedHashSet<>();

    public BasicSimpleReflexAgent(Function<P, S> interpretInput, Collection<Rule<S>> rules) {
        this.interpretInputFn = interpretInput;
        this.rules.addAll(rules);
    }

    @Override
    public Set<Rule<S>> rules() {
        return rules;
    }

    @Override
    public S interpretInput(P percept) {
        return this.interpretInputFn.apply(percept);
    }
}
