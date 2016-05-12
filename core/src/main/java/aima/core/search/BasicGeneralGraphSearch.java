package aima.core.search;

import aima.core.api.search.GeneralGraphSearch;
import aima.core.search.api.Node;
import aima.core.search.basic.support.BasicFrontierQueue;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGeneralGraphSearch<A, S> extends BasicSearchFunction<A, S> implements GeneralGraphSearch<A, S> {

    private Supplier<Queue<Node<A, S>>> frontierSupplier;
    private Supplier<Set<S>>            exploredSupplier;

    public BasicGeneralGraphSearch() {
        this(BasicFrontierQueue::new, HashSet::new);
    }

    public BasicGeneralGraphSearch(Supplier<Queue<Node<A, S>>> frontierSupplier) {
        this(frontierSupplier, HashSet::new);
    }

    public BasicGeneralGraphSearch(Supplier<Queue<Node<A, S>>> frontierSupplier, Supplier<Set<S>> exploredSupplier) {
        this.frontierSupplier = frontierSupplier;
        this.exploredSupplier = exploredSupplier;
    }

    @Override
    public Queue<Node<A, S>> newFrontier() {
        return frontierSupplier.get();
    }

    @Override
    public Set<S> newExplored() {
        return exploredSupplier.get();
    }

}
