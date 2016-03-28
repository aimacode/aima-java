package aima.core.search.informed.graph;

import aima.core.api.search.Node;
import aima.core.api.search.informed.graph.GreedyBestFirstGraphSearch;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGreedyBestFirstGraphSearch<A, S> extends BasicBestFirstGraphSearch<A, S> implements GreedyBestFirstGraphSearch<A, S> {
    private ToDoubleFunction<Node<A, S>> h;

    public BasicGreedyBestFirstGraphSearch(ToDoubleFunction<Node<A, S>> h) {
        super(h);
        this.h = h;
    }

    @Override
    public ToDoubleFunction<Node<A, S>> h() {
      return h;
    }
}
