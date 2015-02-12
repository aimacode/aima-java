package aima.core.agent;

import aima.core.api.agent.Action;
import aima.core.api.agent.Percept;
import aima.core.api.agent.SimpleProblemSolvingAgent;
import aima.core.api.search.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ciaran O'Reilly
 */
public class BasicSimpleProblemSolvingAgent<P extends Percept, S, G> implements SimpleProblemSolvingAgent<P, S, G> {

    // state <- UPDATE-STATE(state, percept)
    private BiFunction<S, P, S> updateStateFn;
    private Function<S, G> formulateGoalFn;
    private BiFunction<S, G, Problem<S>> formulateProblemFn;
    private Function<Problem<S>, List<Action>> searchFn;
    private List<Action> seq = new ArrayList<>();
    private S state;
    private G goal;
    private Problem<S> problem;

    public BasicSimpleProblemSolvingAgent(BiFunction<S, P, S> updateStateFn,
                                          Function<S, G> formulateGoalFn,
                                          BiFunction<S, G, Problem<S>> formulateProblemFn,
                                          Function<Problem<S>, List<Action>> searchFn) {
        this.updateStateFn      = updateStateFn;
        this.formulateGoalFn    = formulateGoalFn;
        this.formulateProblemFn = formulateProblemFn;
        this.searchFn           = searchFn;
    }

    @Override
    public List<Action> getSeq() {
        return seq;
    }

    public void setSeq(List<Action> sequence) {
        this.seq = sequence;
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
    public G getGoal() {
        return goal;
    }

    @Override
    public void setGoal(G goal) {
        this.goal = goal;
    }

    @Override
    public Problem<S> getProblem() {
        return problem;
    }

    @Override
    public void setProblem(Problem<S> problem) {
        this.problem = problem;
    }

    @Override
    public S updateState(S currentState, P percept) {
        return updateStateFn.apply(currentState, percept);
    }

    @Override
    public G formulateGoal(S state) {
        return formulateGoalFn.apply(state);
    }

    @Override
    public Problem<S> formulateProblem(S state, G goal) {
        return formulateProblemFn.apply(state, goal);
    }

    @Override
    public List<Action> search(Problem<S> problem) {
        return searchFn.apply(problem);
    }
}
