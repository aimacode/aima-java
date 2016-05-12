package aima.core.search.uninformed.graph;

import aima.core.api.search.uninformed.graph.UniformCostGraphSearch;
import aima.core.search.BasicGeneralGraphSearch;
import aima.core.search.api.Node;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicUniformCostGraphSearch<A, S> extends BasicGeneralGraphSearch<A, S> implements UniformCostGraphSearch<A, S> {

    public BasicUniformCostGraphSearch() {
        // For UNIFORM-COST-SEARCH a priority queue ordered by PATH-COST
        this(Node::pathCost);
    }

    public BasicUniformCostGraphSearch(ToDoubleFunction<Node<A, S>> nodeCost) {
        super(() -> new BasicPriorityFrontierQueue<>((n1, n2) -> Double.compare(nodeCost.applyAsDouble(n1), nodeCost.applyAsDouble(n2))));
    }
}