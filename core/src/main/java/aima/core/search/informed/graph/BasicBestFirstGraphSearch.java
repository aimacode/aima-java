package aima.core.search.informed.graph;

import aima.core.api.search.informed.graph.BestFirstGraphSearch;
import aima.core.search.api.Node;
import aima.core.search.uninformed.graph.BasicUniformCostGraphSearch;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicBestFirstGraphSearch<A, S> extends BasicUniformCostGraphSearch<A, S> implements BestFirstGraphSearch<A, S> {
    private ToDoubleFunction<Node<A, S>> f;

    public BasicBestFirstGraphSearch(ToDoubleFunction<Node<A, S>> f) {
        super(f);
        this.f = f;
    }

    @Override
    public ToDoubleFunction<Node<A, S>> f() {
      return f;
    }
}
