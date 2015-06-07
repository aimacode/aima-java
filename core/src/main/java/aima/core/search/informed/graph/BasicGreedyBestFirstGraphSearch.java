package aima.core.search.informed.graph;

import aima.core.api.search.Node;
import aima.core.api.search.informed.graph.GreedyBestFirstGraphSearch;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGreedyBestFirstGraphSearch<S> extends BasicBestFirstGraphSearch<S> implements GreedyBestFirstGraphSearch<S> {
    private ToDoubleFunction<Node<S>> h;

    public BasicGreedyBestFirstGraphSearch(ToDoubleFunction<Node<S>> h) {
        super(h);
        this.h = h;
    }

    @Override
    public ToDoubleFunction<Node<S>> h() {
      return h;
    }
}
