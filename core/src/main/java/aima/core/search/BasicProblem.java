package aima.core.search;

import aima.core.api.search.Problem;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Basic implementation of the Problem interface.
 *
 * @author Ciaran O'Reilly
 */
public class BasicProblem<A, S> implements Problem<A, S> {
    @FunctionalInterface
    public interface StepCost<A, S> {
        double c(S s, A a, S sPrime);
    }

    private S initialState;
    private Function<S, Set<A>> actionsFn;
    private BiFunction<S, A, S> resultFn;
    private Predicate<S> goalTestPredicate;
    private StepCost<A, S> stepCostFn;

    public BasicProblem(S initialState,
                        Function<S, Set<A>> actionsFn,
                        BiFunction<S, A, S> resultFn,
                        Predicate<S> goalTestPredicate) {
        this(initialState,
                actionsFn,
                resultFn,
                goalTestPredicate,
                (s, a, sPrime) -> 1.0  // Default step cost function.
        );
    }

    public BasicProblem(S initialState,
                        Function<S, Set<A>> actionsFn,
                        BiFunction<S, A, S> resultFn,
                        Predicate<S> goalTestPredicate,
                        StepCost<A, S> stepCostFn) {
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
    public Set<A> actions(S s) {
        return actionsFn.apply(s);
    }

    @Override
    public S result(S s, A a) {
        return resultFn.apply(s, a);
    }

    @Override
    public boolean isGoalState(S state) {
        return goalTestPredicate.test(state);
    }

    @Override
    public double stepCost(S s, A a, S sPrime) {
        return stepCostFn.c(s, a, sPrime);
    }
}
