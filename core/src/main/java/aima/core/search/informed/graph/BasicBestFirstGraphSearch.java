package aima.core.search.informed.graph;

import aima.core.api.search.Node;
import aima.core.api.search.informed.graph.BestFirstGraphSearch;
import aima.core.search.uninformed.graph.BasicUniformCostGraphSearch;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicBestFirstGraphSearch<S> extends BasicUniformCostGraphSearch<S> implements BestFirstGraphSearch<S> {
    private ToDoubleFunction<Node<S>> f;

    public BasicBestFirstGraphSearch(ToDoubleFunction<Node<S>> f) {
        super(f);
        this.f = f;
    }

    @Override
    public ToDoubleFunction<Node<S>> f() {
      return f;
    }
}
