package aima.core.search.basic.uninformed;

import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.TreeShortestPathPrioritySearch;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;
import aima.core.search.basic.support.BasicSearchController;

/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class UniformCostTreeSearch<A, S> extends TreeShortestPathPrioritySearch<A, S> {
	public UniformCostTreeSearch() {
    	this(new BasicSearchController<>(), new BasicNodeFactory<>());
    }
	
	public UniformCostTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory) {
    	super(searchController, nodeFactory, () -> new BasicPriorityFrontierQueue<A, S>((n1, n2) -> Double.compare(n1.pathCost(), n2.pathCost())));
    }
}