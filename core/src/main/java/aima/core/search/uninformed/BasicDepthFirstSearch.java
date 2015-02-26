package aima.core.search.uninformed;

import aima.core.api.search.Node;
import aima.core.search.BasicGraphSearch;
import aima.core.search.support.BasicFrontierQueue;

import java.util.Collections;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * The depth-first algorithm is an instance of the graph-search algorithm
 * in Figure ??; whereas breadth-first-search uses a FIFO queue, depth-first
 * search uses a LIFO queue.
 *
 * @author Ciaran O'Reilly
 */
public class BasicDepthFirstSearch<S> extends BasicGraphSearch<S> {

    @Override
    public Queue<Node<S>> newFrontier() {
        return Collections.asLifoQueue(new BasicFrontierQueue<>());
    }
}
