package aima.core.search.uninformed.graph;

import aima.core.search.BasicGeneralGraphSearch;
import aima.core.search.basic.support.BasicFrontierQueue;

import java.util.Collections;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * The depth-first algorithm is an instance of the graph-search algorithm
 * in Figure ??; whereas breadth-first-search uses a FIFO queue, depth-first
 * search uses a LIFO queue.
 *
 * @author Ciaran O'Reilly
 */
public class BasicDepthFirstGraphSearch<A, S> extends BasicGeneralGraphSearch<A, S> {

    public BasicDepthFirstGraphSearch() {
        super(() -> Collections.asLifoQueue(new BasicFrontierQueue<>()));
    }
}
