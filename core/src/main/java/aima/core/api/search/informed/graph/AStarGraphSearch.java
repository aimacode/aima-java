package aima.core.api.search.informed.graph;

import aima.core.api.search.Node;

import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * The algorithm is identical to UNIFORM-COST graph search except tht A* uses <em>g + h</em> instead of g.
 *
 * @author Ciaran O'Reilly
 */
public interface AStarGraphSearch<A, S> extends BestFirstGraphSearch<A, S> {

    /**
     *
     * @return the heuristic function.
     */
    ToDoubleFunction<Node<A, S>> h();

    @Override
    default ToDoubleFunction<Node<A, S>> f() {
        // g + h
        return n -> n.pathCost() + h().applyAsDouble(n);
    }
}
