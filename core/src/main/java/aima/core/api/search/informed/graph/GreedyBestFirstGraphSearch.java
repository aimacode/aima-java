package aima.core.api.search.informed.graph;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;

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
public interface GreedyBestFirstGraphSearch<A, S> extends BestFirstGraphSearch<A, S> {

    /**
     *
     * @return the heuristic function.
     */
    ToDoubleFunction<Node<A, S>> h();

    @Override
    default ToDoubleFunction<Node<A, S>> f() {
        return h();  // f(n) = h(n)
    }
}
