package aima.core.search.informed.tree;

import aima.core.api.search.informed.tree.RecursiveBestFirstTreeSearch;
import aima.core.search.BasicSearchFunction;
import aima.core.search.api.Node;

import java.util.function.Function;

/**
 * @author Ciaran O'Reilly
 */
public class BasicRecursiveBestFirstTreeSearch<A, S> extends BasicSearchFunction<A, S> implements RecursiveBestFirstTreeSearch<A, S> {

    private Function<Node<A, S>, Double> heuristicFn;

    public BasicRecursiveBestFirstTreeSearch(Function<Node<A, S>, Double> heuristicFn) {
        this.heuristicFn = heuristicFn;
    }

    @Override
    public double h(Node<A, S> node) {
        return heuristicFn.apply(node);
    }
}
