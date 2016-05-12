package aima.core.search.local;

import aima.core.api.search.local.HillClimbingSearch;
import aima.core.search.BasicSearchFunction;
import aima.core.search.api.Node;

import java.util.function.Function;

/**
 * @author Paul Anton
 */
public class BasicHillClimbingSearch<A, S> extends BasicSearchFunction<A, S> implements HillClimbingSearch<A, S> {

    private Function<Node<A, S>, Double> heuristicFn;

    public BasicHillClimbingSearch(Function<Node<A, S>, Double> heuristicFn) {
        this.heuristicFn = heuristicFn;
    }

    @Override
    public double h(Node<A, S> node) {
        return heuristicFn.apply(node);
    }
}
