package aima.core.search.uninformed.graph;

import aima.core.api.search.uninformed.graph.UniformCostGraphSearch;
import aima.core.search.BasicGeneralGraphSearch;
import aima.core.search.support.BasicPriorityFrontierQueue;

/**
 * @author Ciaran O'Reilly
 */
public class BasicUniformCostGraphSearch<S> extends BasicGeneralGraphSearch<S> implements UniformCostGraphSearch<S> {

    public BasicUniformCostGraphSearch() {
        super(() -> new BasicPriorityFrontierQueue<>((n1, n2) -> Double.compare(n1.pathCost(), n2.pathCost())));
    }
}