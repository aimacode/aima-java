package aima.core.api.search.informed.graph;

import aima.core.api.search.Node;
import aima.core.api.search.uninformed.graph.UniformCostGraphSearch;

import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * Greedy best-first graph search tryes to expand the node that is closest to the goal, on the grounds
 * that this is likely to lead to a solution quickly. Thus, it evaluates nodes by using just the heuristic
 * function; that is <em>f(n) = h(n)</em>.
 *
 * @author Ciaran O'Reilly
 */
public interface GreedyBestFirstGraphSearch<S> extends BestFirstGraphSearch<S> {

    /**
     *
     * @return the heuristic function.
     */
    ToDoubleFunction<Node<S>> h();

    @Override
    default ToDoubleFunction<Node<S>> f() {
        return h();  // f(n) = h(n)
    }
}
