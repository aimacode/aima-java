package aima.core.search.api;

/**
 * @author manthan
 */
public interface BidirectionalProblem<A, S> extends
        // S initialState()
        InitialStateFunction<S>,
        // List<A> actions(S s)
        ActionsFunction<A, S>,
        // List<S> results(S s, A a)
        ResultFunction<A, S>,
        // boolean isGoalState(S s)
        GoalTestPredicate<S>,
        // double stepCost(S s, A a, S s')
        StepCostFunction<A, S> {
    Problem<A,S> getOriginalProblem();

    Problem<A,S> getReverseProblem();
}
