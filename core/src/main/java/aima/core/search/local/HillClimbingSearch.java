package aima.core.search.local;

import aima.core.api.search.Node;
import aima.core.search.BasicSearchFunction;

import java.util.function.Function;

/**
 * @author Paul Anton
 */
public class HillClimbingSearch<S> extends BasicSearchFunction<S> implements aima.core.api.search.local.HillClimbingSearch<S> {

    private Function<Node<S>, Double> heuristicFn;

    public HillClimbingSearch(Function<Node<S>, Double> heuristicFn) {
        this.heuristicFn = heuristicFn;
    }

    @Override
    public double h(Node<S> node) {
        return heuristicFn.apply(node);
    }
}
