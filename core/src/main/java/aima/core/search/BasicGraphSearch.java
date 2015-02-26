package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.GraphSearch;
import aima.core.search.support.BasicFrontierQueue;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGraphSearch<S> extends BasicSearchFunction<S> implements GraphSearch<S> {

    @Override
    public Queue<Node<S>> newFrontier() {
        return new BasicFrontierQueue<>();
    }

    @Override
    public Set<S> newExplored() {
        return new HashSet<>();
    }

}
