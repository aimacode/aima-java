package aima.core.search.informed.graph;

import aima.core.api.search.Node;
import aima.core.api.search.informed.graph.AStarGraphSearch;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicAStarGraphSearch<A, S> extends BasicBestFirstGraphSearch<A, S> implements AStarGraphSearch<A, S> {
    private ToDoubleFunction<Node<A, S>> h;

    public BasicAStarGraphSearch(ToDoubleFunction<Node<A, S>> h) {
        super(n -> n.pathCost() + h.applyAsDouble(n));  // g + h
        this.h = h;
    }

    @Override
    public ToDoubleFunction<Node<A, S>> h() {
      return h;
    }
}
