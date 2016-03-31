package aima.core.agent;

import aima.core.api.agent.SimpleProblemSolvingAgent;
import aima.core.api.search.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ciaran O'Reilly
 */
public class BasicSimpleProblemSolvingAgent<A, P, S, G> implements SimpleProblemSolvingAgent<A, P, S, G> {

    // state <- UPDATE-STATE(state, percept)
    private BiFunction<S, P, S> updateStateFn;
    private Function<S, G> formulateGoalFn;
    private BiFunction<S, G, Problem<A, S>> formulateProblemFn;
    private Function<Problem<A, S>, List<A>> searchFn;
    private List<A> seq = new ArrayList<>();
    private S state;
    private G goal;
    private Problem<A, S> problem;

    public BasicSimpleProblemSolvingAgent(BiFunction<S, P, S> updateStateFn,
                                          Function<S, G> formulateGoalFn,
                                          BiFunction<S, G, Problem<A, S>> formulateProblemFn,
                                          Function<Problem<A, S>, List<A>> searchFn) {
        this.updateStateFn      = updateStateFn;
        this.formulateGoalFn    = formulateGoalFn;
        this.formulateProblemFn = formulateProblemFn;
        this.searchFn           = searchFn;
    }

    @Override
    public List<A> getSeq() {
        return seq;
    }

    public void setSeq(List<A> sequence) {
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
    public Problem<A, S> getProblem() {
        return problem;
    }

    @Override
    public void setProblem(Problem<A, S> problem) {
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
    public Problem<A, S> formulateProblem(S state, G goal) {
        return formulateProblemFn.apply(state, goal);
    }

    @Override
    public List<A> search(Problem<A, S> problem) {
        return searchFn.apply(problem);
    }
}
