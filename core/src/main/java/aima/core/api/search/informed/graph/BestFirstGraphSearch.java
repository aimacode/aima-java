package aima.core.api.search.informed.graph;

import aima.core.api.search.uninformed.graph.UniformCostGraphSearch;
import aima.core.search.api.Node;

import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * Best-first graph search is an instance of the general GRAPH-SEARCH algorithm in
 * which a node is selected for expansion based on an evaluation function, <em>f(n)</em>.
 * The evaluation function is construed as a cost estimate, so the node with the lowest
 * evaluation is expanded first. The implementation of best-first graph search is identical
 * to that for uniform-cost search (Figure ?.??), except for the use of <em>f</em> instead
 * of <em>g</em> to order the priority queue.
 *
 * @author Ciaran O'Reilly
 */
public interface BestFirstGraphSearch<A, S> extends UniformCostGraphSearch<A, S> {

    /**
     *
     * @return the evaluation function.
     */
    ToDoubleFunction<Node<A, S>> f();
}
