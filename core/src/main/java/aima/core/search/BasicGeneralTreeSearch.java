package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.GeneralTreeSearch;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGeneralTreeSearch<S> extends BasicSearchFunction<S> implements GeneralTreeSearch<S> {

    private Supplier<Queue<Node<S>>> frontierSupplier;

    public BasicGeneralTreeSearch() {
       this(LinkedList::new);
    }

    public BasicGeneralTreeSearch(Supplier<Queue<Node<S>>> frontierSupplier) {
        this.frontierSupplier = frontierSupplier;
    }


    @Override
    public Queue<Node<S>> newFrontier() {
        return frontierSupplier.get();
    }
}
