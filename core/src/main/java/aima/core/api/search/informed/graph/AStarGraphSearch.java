package aima.core.api.search.informed.graph;

import aima.core.api.search.Node;

import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * The algorithm is identical to UNIFORM-COST graph search except tht A* uses <em>g + h</em> instead of g.
 * g(n) : cost to reach node n.
 * h(n) : cost to get from node to goal.
 *
 * @author Ciaran O'Reilly
 */
public interface AStarGraphSearch<S> extends BestFirstGraphSearch<S> {

    /**
     *
     * @return the heuristic function.
     */
    ToDoubleFunction<Node<S>> h();

    @Override
    default ToDoubleFunction<Node<S>> f() {
        // g + h
        return n -> n.pathCost() + h().applyAsDouble(n);
    }
}
