package aima.core.search;

import aima.core.api.agent.Action;
import aima.core.api.search.Problem;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ciaran O'Reilly
 */
public class BasicProblem<S> implements Problem<S> {
    @FunctionalInterface
    public interface StepCost<S> {
        double c(S s, Action a, S sPrime);
    }

    private S initialState;
    private Function<S, Set<Action>> actionsFn;
    private BiFunction<S, Action, S> resultFn;
    private Predicate<S> goalTestPredicate;
    private StepCost<S> stepCostFn;

    public BasicProblem(S initialState,
                        Function<S, Set<Action>> actionsFn,
                        BiFunction<S, Action, S> resultFn,
                        Predicate<S> goalTestPredicate) {
        this(initialState,
                actionsFn,
                resultFn,
                goalTestPredicate,
                (s, a, sPrime) -> 1.0  // Default step cost function.
        );
    }

    public BasicProblem(S initialState,
                        Function<S, Set<Action>> actionsFn,
                        BiFunction<S, Action, S> resultFn,
                        Predicate<S> goalTestPredicate,
                        StepCost<S> stepCostFn) {
        this.initialState      = initialState;
        this.actionsFn         = actionsFn;
        this.resultFn          = resultFn;
        this.goalTestPredicate = goalTestPredicate;
        this.stepCostFn        = stepCostFn;
    }

    @Override
    public S initialState() {
        return initialState;
    }

    @Override
    public Set<Action> actions(S s) {
        return actionsFn.apply(s);
    }

    @Override
    public S result(S s, Action a) {
        return resultFn.apply(s, a);
    }

    @Override
    public boolean isGoalState(S state) {
        return goalTestPredicate.test(state);
    }

    @Override
    public double stepCost(S s, Action a, S sPrime) {
        return stepCostFn.c(s, a, sPrime);
    }
}
