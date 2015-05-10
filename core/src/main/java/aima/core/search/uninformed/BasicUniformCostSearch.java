package aima.core.search.uninformed;

import aima.core.api.search.Node;
import aima.core.api.search.uninformed.UniformCostSearch;
import aima.core.search.BasicGraphSearch;
import aima.core.search.support.BasicPriorityFrontierQueue;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class BasicUniformCostSearch<S> extends BasicGraphSearch<S> implements UniformCostSearch<S> {

    public BasicUniformCostSearch() {
        super(() -> new BasicPriorityFrontierQueue<>((n1, n2) -> Double.compare(n1.pathCost(), n2.pathCost())));
    }
}