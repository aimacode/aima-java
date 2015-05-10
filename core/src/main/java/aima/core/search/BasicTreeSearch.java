package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.TreeSearch;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * @author Ciaran O'Reilly
 */
public class BasicTreeSearch<S> extends BasicSearchFunction<S> implements TreeSearch<S> {

    private Supplier<Queue<Node<S>>> frontierSupplier;

    public BasicTreeSearch() {
       this(LinkedList::new);
    }

    public BasicTreeSearch(Supplier<Queue<Node<S>>> frontierSupplier) {
        this.frontierSupplier = frontierSupplier;
    }


    @Override
    public Queue<Node<S>> newFrontier() {
        return frontierSupplier.get();
    }
}
