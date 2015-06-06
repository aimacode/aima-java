package aima.core.search.informed.tree;

import aima.core.api.search.Node;
import aima.core.api.search.informed.tree.RecursiveBestFirstTreeSearch;
import aima.core.search.BasicSearchFunction;

import java.util.function.Function;

/**
 * @author Ciaran O'Reilly
 */
public class BasicRecursiveBestFirstTreeSearch<S> extends BasicSearchFunction<S> implements RecursiveBestFirstTreeSearch<S> {

    private Function<Node<S>, Double> heuristicFn;

    public BasicRecursiveBestFirstTreeSearch(Function<Node<S>, Double> heuristicFn) {
        this.heuristicFn = heuristicFn;
    }

    @Override
    public double h(Node<S> node) {
        return heuristicFn.apply(node);
    }
}
