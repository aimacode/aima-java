package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.GeneralGraphSearch;
import aima.core.search.support.BasicFrontierQueue;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGeneralGraphSearch<S> extends BasicSearchFunction<S> implements GeneralGraphSearch<S> {

    private Supplier<Queue<Node<S>>> frontierSupplier;
    private Supplier<Set<S>>         exploredSupplier;

    public BasicGeneralGraphSearch() {
        this(BasicFrontierQueue::new, HashSet::new);
    }

    public BasicGeneralGraphSearch(Supplier<Queue<Node<S>>> frontierSupplier) {
        this(frontierSupplier, HashSet::new);
    }

    public BasicGeneralGraphSearch(Supplier<Queue<Node<S>>> frontierSupplier, Supplier<Set<S>> exploredSupplier) {
        this.frontierSupplier = frontierSupplier;
        this.exploredSupplier = exploredSupplier;
    }

    @Override
    public Queue<Node<S>> newFrontier() {
        return frontierSupplier.get();
    }

    @Override
    public Set<S> newExplored() {
        return exploredSupplier.get();
    }

}
