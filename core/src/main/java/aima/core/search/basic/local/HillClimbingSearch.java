package aima.core.search.basic.local;

import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;

import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <p>
 *
 * <pre>
 * function HILL-CLIMBING(problem) returns a state that is a local maximum
 *  current ← problem.INITIAL-STATE
 *  loop do
 *    neighbor ← a highest-valued successor of current
 *    if VALUE(neighbour) ≤ VALUE(current) then return current
 *    current ← neighbor</pre>
 * <p>
 * Figure ?? The hill-climbing search algorithm, which is the most basic local
 * search technique. At each step the current node is replaced by the best
 * neighbor; in this version, that means the neighbor with the highest VALUE,
 * but if a heuristic cost estimate h is used, we would find the neighbor with
 * the lowest h.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Paul Anton
 * @author Mike Stampone
 * @author samagra
 */
public class HillClimbingSearch<A, S> implements SearchForStateFunction<A, S> {

    /*
     * Represents an objective (higher better) or cost/heuristic (lower better)
     * function. If a cost/heuristic function is passed in the
     * 'isSteepestAscentVersion' should be set to false for the algorithm to
     * search for minimums.
     */
    protected ToDoubleFunction<S> stateValueFn;

    // Constructors.
    public HillClimbingSearch(ToDoubleFunction<S> stateValueFn) {
        this(stateValueFn, true);
    }

    public HillClimbingSearch(ToDoubleFunction<S> stateValueFn, boolean isSteepestAscentVersion) {
        this.stateValueFn = stateValueFn;
        if (!isSteepestAscentVersion) {
            // Convert from one to the other by switching the sign
            this.stateValueFn = (state) -> stateValueFn.applyAsDouble(state) * -1;
        }
    }

    // function HILL-CLIMBING(problem) returns a state that is a local maximum
    @Override
    public S apply(Problem<A, S> problem) {
        S current = problem.initialState();
        while (true) {
            S neighbor = highestValuedSuccessor(current, problem);
            if (stateValueFn.applyAsDouble(neighbor) <= stateValueFn.applyAsDouble(current)) {
                return current;
            }
            current = neighbor;
        }
    }

    /**
     * Calculates the successor with the highest value.
     *
     * @param current
     * @param problem
     * @return
     */
    public S highestValuedSuccessor(S current, Problem<A, S> problem) {
        S highestValueSuccessor = null;
        for (A action : problem.actions(current)) {
            S successor = problem.result(current, action);
            if (highestValueSuccessor == null || stateValueFn.applyAsDouble(successor)
                    > stateValueFn.applyAsDouble(highestValueSuccessor)) {
                highestValueSuccessor = successor;
            }
        }
        // If no successor then just be our own neighbor
        // as the calling code will just return 'current' as
        // the <= test will be true
        if (highestValueSuccessor == null) {
            highestValueSuccessor = current;
        }
        return highestValueSuccessor;
    }
}