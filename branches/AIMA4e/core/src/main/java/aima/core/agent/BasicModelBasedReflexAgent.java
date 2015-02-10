package aima.core.agent;

import aima.core.api.agent.Action;
import aima.core.api.agent.ModelBasedReflexAgent;
import aima.core.api.agent.Percept;
import aima.core.api.agent.Rule;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 */
public class BasicModelBasedReflexAgent<P extends Percept, S, M> implements ModelBasedReflexAgent<P, S, M> {
    @FunctionalInterface
    public interface UpdateState<S, P, M> {
        S apply(S currentState, Action mostRecentAction, P percept, M model);
    }

    private UpdateState<S, P, M> updateStateFn;
    private S state;
    private M model;
    private Set<Rule<S>> rules = new LinkedHashSet<>();
    private Action action = null;

    public BasicModelBasedReflexAgent(UpdateState<S, P, M> updateStateFn,
                                      S state,
                                      M model,
                                      Collection<Rule<S>> rules) {
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
    public Set<Rule<S>> rules() {
        return rules;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public S updateState(S currentState, Action mostRecentAction, P percept, M model) {
        return this.updateStateFn.apply(currentState, mostRecentAction, percept, model);
    }
}
