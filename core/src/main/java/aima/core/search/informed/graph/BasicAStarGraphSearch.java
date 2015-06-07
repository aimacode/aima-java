package aima.core.search.informed.graph;

import aima.core.api.search.Node;
import aima.core.api.search.informed.graph.AStarGraphSearch;
import aima.core.api.search.informed.graph.GreedyBestFirstGraphSearch;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicAStarGraphSearch<S> extends BasicBestFirstGraphSearch<S> implements AStarGraphSearch<S> {
    private ToDoubleFunction<Node<S>> h;

    public BasicAStarGraphSearch(ToDoubleFunction<Node<S>> h) {
        super(n -> n.pathCost() + h.applyAsDouble(n));  // g + h
        this.h = h;
    }

    @Override
    public ToDoubleFunction<Node<S>> h() {
      return h;
    }
}
