package aima.core.api.search.uninformed;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;
import aima.core.api.search.SearchFunction;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function DEPTH-LIMITED-SEARCH(problem, limit) returns a solution, or failure/cutoff
 *   return RECURSIVE-DLS(MAKE-NODE(problem.INITIAL-STATE), problem, limit)
 *
 * function RECURSIVE-DLS(node, problem, limit) returns a solution, or failure/cutoff
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   else if limit = 0 then return cutoff
 *   else
 *       cutoff_occurred? &lt;- false
 *       for each action in problem.ACTIONS(node.STATE) do
 *           child &lt;- CHILD-NODE(problem, node, action)
 *           result &lt;- RECURSIVE-DLS(child, problem, limit - 1)
 *           if result = cutoff then cutoff_occurred? &lt;- true
 *           else if result != failure then return result
 *       if cutoff_occurred? then return cutoff else return failure
 * </pre>
 *
 * Figure ?? A recursive implementation of depth-limited tree search.
 *
 * @author Ciaran O'Reilly
 */
public interface DepthLimitedSearch<S> extends SearchFunction<S> {

    // function DEPTH-LIMITED-SEARCH(problem, limit) returns a solution, or failure/cutoff
    default List<Action> depthLimitedSearch(Problem<S> problem, int limit) {
        // return RECURSIVE-DLS(MAKE-NODE(problem.INITIAL-STATE), problem, limit)
        return recursiveDLS(newNode(problem.initialState()), problem, limit);
    }

    // function RECURSIVE-DLS(node, problem, limit) returns a solution, or failure/cutoff
    default List<Action> recursiveDLS(Node<S> node, Problem<S> problem, int limit) {
        // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
        if (isGoalState(node, problem)) {
            return solution(node);
        } // else if limit = 0 then return cutoff
        else if (limit == 0) {
            return cutoff();
        } // else
        else {
            // cutoff_occurred? <- false
            boolean cutoff_occurred = false;
            // for each action in problem.ACTIONS(node.STATE) do
            for (Action action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<S> child = childNode(problem, node, action);
                // result <- RECURSIVE-DLS(child, problem, limit - 1)
                List<Action> result = recursiveDLS(child, problem, limit-1);
                // if result = cutoff then cutoff_occurred? <- true
                if (result == cutoff()) {
                    cutoff_occurred = true;
                } // else if result != failure then return result
                else if (result != failure()) { return result; }
            }
            // if cutoff_occurred? then return cutoff else return failure
            if (cutoff_occurred) { return cutoff(); } else { return failure(); }
        }
    }

    @Override
    default List<Action> apply(Problem<S> problem) {
        return depthLimitedSearch(problem, limit());
    }

    int limit();
    List<Action> cutoff();
}
