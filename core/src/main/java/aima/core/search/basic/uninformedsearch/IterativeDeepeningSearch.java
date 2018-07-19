package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.SearchUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *
 /**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result &larr; DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </pre>
 *
 * Figure ?? The iterative deepening search algorithm, which repeatedly applies
 * depth-limited search with increasing limits. It terminates when a solution is
 * found or if the depth-limited search returns failure, meaning that no
 * solution exists.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * @author samagra
 */
public class IterativeDeepeningSearch<A,S> implements GenericSearchInterface<A,S>, SearchForActionsFunction<A,S> {
    DepthLimitedSearch<A,S> depthLimitedSearch = new DepthLimitedSearch<>();

    /**
     * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
     * @param problem The search problem
     * @return The solution if exists else continues to run
     */
    @Override
    public Node<A, S> search(Problem<A, S> problem) {
        //for depth = 0 to infinity  do
        for(int depth =0;depth>=0;depth++){
            //result &larr; DEPTH-LIMITED-SEARCH(problem, depth)
            Node<A,S> result = depthLimitedSearch.search(problem,depth);
            // if result != cutoff then return result
            if (result!=null)
                return result;
        }
        return null;
    }

    @Override
    public List<A> apply(Problem<A, S> problem) {
        Node<A,S> solution = this.search(problem);
        return SearchUtils.generateActions(solution);
    }
}
