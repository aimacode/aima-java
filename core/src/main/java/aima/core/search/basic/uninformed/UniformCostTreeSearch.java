package aima.core.search.basic.uninformed;

import aima.core.search.api.NodeFactory;
import aima.core.search.basic.TreeShortestPathPrioritySearch;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;

/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class UniformCostTreeSearch<A, S> extends TreeShortestPathPrioritySearch<A, S> {
	public UniformCostTreeSearch() {
    	this(new BasicNodeFactory<>());
    }
	
	public UniformCostTreeSearch(NodeFactory<A, S> nodeFactory) {
    	super(nodeFactory, () -> new BasicPriorityFrontierQueue<A, S>((n1, n2) -> Double.compare(n1.pathCost(), n2.pathCost())));
    }
}