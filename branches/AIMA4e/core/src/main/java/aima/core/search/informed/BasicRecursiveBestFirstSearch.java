package aima.core.search.informed;

import aima.core.api.search.Node;
import aima.core.api.search.informed.RecursiveBestFirstSearch;
import aima.core.search.BasicSearchFunction;

import java.util.function.Function;

/**
 * @author Ciaran O'Reilly
 */
public class BasicRecursiveBestFirstSearch<S> extends BasicSearchFunction<S> implements RecursiveBestFirstSearch<S> {

    private Function<Node<S>, Double> heuristicFn;

    public BasicRecursiveBestFirstSearch(Function<Node<S>, Double> heuristicFn) {
        this.heuristicFn = heuristicFn;
    }

    @Override
    public double h(Node<S> node) {
        return heuristicFn.apply(node);
    }
}
