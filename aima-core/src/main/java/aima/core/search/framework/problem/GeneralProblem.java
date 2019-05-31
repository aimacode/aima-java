package aima.core.search.framework.problem;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Configurable problem which uses objects to explicitly represent the required
 * functions.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 */
public class GeneralProblem<S, A> implements Problem<S, A> {

    private S initialState;
    private Function<S, List<A>> actionsFn;
    private BiFunction<S, A, S> resultFn;
    private Predicate<S> goalTest;
    private StepCostFunction<S, A> stepCostFn;

    /**
     * Constructs a problem with the specified components, which includes a step
     * cost function.
     *
     * @param initialState
     *            the initial state of the agent.
     * @param actionsFn
     *            a description of the possible actions available to the agent.
     * @param resultFn
     *            a description of what each action does; the formal name for
     *            this is the transition model, specified by a function
     *            RESULT(s, a) that returns the state that results from doing
     *            action a in state s.
     * @param goalTest
     *            test determines whether a given state is a goal state.
     * @param stepCostFn
     *            a path cost function that assigns a numeric cost to each path.
     *            The problem-solving-agent chooses a cost function that
     *            reflects its own performance measure.
     */
    public GeneralProblem(S initialState, Function<S, List<A>> actionsFn,  BiFunction<S, A, S> resultFn,
                          Predicate<S> goalTest, StepCostFunction<S, A> stepCostFn) {
        this.initialState = initialState;
        this.actionsFn = actionsFn;
        this.resultFn = resultFn;
        this.goalTest = goalTest;
        this.stepCostFn = stepCostFn;
    }

    /**
     * Constructs a problem with the specified components, and a default step
     * cost function (i.e. 1 per step).
     *
     * @param initialState
     *            the initial state that the agent starts in.
     * @param actionsFn
     *            a description of the possible actions available to the agent.
     * @param resultFn
     *            a description of what each action does; the formal name for
     *            this is the transition model, specified by a function
     *            RESULT(s, a) that returns the state that results from doing
     *            action a in state s.
     * @param goalTest
     *            test determines whether a given state is a goal state.
     */
    public GeneralProblem(S initialState, Function<S, List<A>> actionsFn,  BiFunction<S, A, S> resultFn,
                          Predicate<S> goalTest) {
        this(initialState, actionsFn, resultFn, goalTest, (s, a, sPrimed) -> 1.0);
    }

    public S getInitialState() {
        return initialState;
    }

    public List<A> getActions(S state) {
        return actionsFn.apply(state);
    }

    public S getResult(S state, A action) {
        return resultFn.apply(state, action);
    }

    public boolean testGoal(S state) {
        return goalTest.test(state);
    }

    public double getStepCosts(S state, A action, S statePrimed) {
        return stepCostFn.applyAsDouble(state, action, statePrimed);
    }
}
