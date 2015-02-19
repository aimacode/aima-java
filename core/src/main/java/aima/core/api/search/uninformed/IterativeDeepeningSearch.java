package aima.core.api.search.uninformed;

import aima.core.api.agent.Action;
import aima.core.api.search.Problem;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result &lt;- DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </pre>
 *
 * Figure ?? The iterative deepening search algorithm, which repeatedly
 * applies depth-limited search with increasing limits. It terminates when a
 * solution is found or if the depth-limited search returns failure, meaning
 * that no solution exists.
 *
 * @author Ciaran O'Reilly
 */
public interface IterativeDeepeningSearch<S> extends SearchFunction<S> {

    // function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
    @Override
    default List<Action> apply(Problem<S> problem) {
        // for depth = 0 to infinity  do
        for (int depth = 0; true; depth++) {
            // result <- DEPTH-LIMITED-SEARCH(problem, depth)
            List<Action> result = dls().depthLimitedSearch(problem, depth);
            // if result != cutoff then return result
            if (result != dls().cutoff()) { return result; }
        }
    }

    DepthLimitedSearch<S> dls();
}
