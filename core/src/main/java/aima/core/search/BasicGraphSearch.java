package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.GraphSearch;
import aima.core.search.support.BasicFrontierQueue;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGraphSearch<S> extends BasicSearchFunction<S> implements GraphSearch<S> {

    private Supplier<Queue<Node<S>>> frontierSupplier;
    private Supplier<Set<S>>         exploredSupplier;

    public BasicGraphSearch() {
        this(BasicFrontierQueue::new, HashSet::new);
    }

    public BasicGraphSearch(Supplier<Queue<Node<S>>> frontierSupplier) {
        this(frontierSupplier, HashSet::new);
    }

    public BasicGraphSearch(Supplier<Queue<Node<S>>> frontierSupplier, Supplier<Set<S>> exploredSupplier) {
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
