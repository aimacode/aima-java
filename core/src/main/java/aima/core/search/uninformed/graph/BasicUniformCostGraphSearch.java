package aima.core.search.uninformed.graph;

import aima.core.api.search.Node;
import aima.core.api.search.uninformed.graph.UniformCostGraphSearch;
import aima.core.search.BasicGeneralGraphSearch;
import aima.core.search.support.BasicPriorityFrontierQueue;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicUniformCostGraphSearch<S> extends BasicGeneralGraphSearch<S> implements UniformCostGraphSearch<S> {

    public BasicUniformCostGraphSearch() {
        // For UNIFORM-COST-SEARCH a priority queue ordered by PATH-COST
        this(Node::pathCost);
    }

    public BasicUniformCostGraphSearch(ToDoubleFunction<Node<S>> nodeCost) {
        super(() -> new BasicPriorityFrontierQueue<>((n1, n2) -> Double.compare(nodeCost.applyAsDouble(n1), nodeCost.applyAsDouble(n2))));
    }
}