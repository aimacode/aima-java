package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.GeneralTreeSearch;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGeneralTreeSearch<A, S> extends BasicSearchFunction<A, S> implements GeneralTreeSearch<A, S> {

    private Supplier<Queue<Node<A, S>>> frontierSupplier;

    public BasicGeneralTreeSearch() {
       this(LinkedList::new);
    }

    public BasicGeneralTreeSearch(Supplier<Queue<Node<A, S>>> frontierSupplier) {
        this.frontierSupplier = frontierSupplier;
    }


    @Override
    public Queue<Node<A, S>> newFrontier() {
        return frontierSupplier.get();
    }
}
