package aima.core.agent;

import aima.core.api.agent.ModelBasedReflexAgent;
import aima.core.api.agent.Rule;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 */
public class BasicModelBasedReflexAgent<A, P, S, M> implements ModelBasedReflexAgent<A, P, S, M> {
    @FunctionalInterface
    public interface UpdateState<A, S, P, M> {
        S apply(S currentState, A mostRecentAction, P percept, M model);
    }

    private UpdateState<A, S, P, M> updateStateFn;
    private S state;
    private M model;
    private Set<Rule<A, S>> rules = new LinkedHashSet<>();
    private A action = null;

    public BasicModelBasedReflexAgent(UpdateState<A, S, P, M> updateStateFn,
                                      S state,
                                      M model,
                                      Collection<Rule<A, S>> rules) {
        this.updateStateFn = updateStateFn;
        this.state         = state;
        this.model         = model;
        this.rules.addAll(rules);
    }

    @Override
    public S getState() {
        return state;
    }

    @Override
    public void setState(S state) {
        this.state = state;
    }

    @Override
    public M model() {
        return model;
    }

    @Override
    public Set<Rule<A, S>> rules() {
        return rules;
    }

    @Override
    public A getAction() {
        return action;
    }

    @Override
    public void setAction(A action) {
        this.action = action;
    }

    @Override
    public S updateState(S currentState, A mostRecentAction, P percept, M model) {
        return this.updateStateFn.apply(currentState, mostRecentAction, percept, model);
    }
}
