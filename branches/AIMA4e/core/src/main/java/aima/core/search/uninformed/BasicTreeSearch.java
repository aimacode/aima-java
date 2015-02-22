package aima.core.search.uninformed;

import aima.core.api.search.Node;
import aima.core.api.search.uninformed.TreeSearch;
import aima.core.search.BasicSearchFunction;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Ciaran O'Reilly
 */
public class BasicTreeSearch<S> extends BasicSearchFunction<S> implements TreeSearch<S> {

    @Override
    public Queue<Node<S>> newFrontier() {
        return new LinkedList<>();
    }
}
