package aima.core.search.basic.uninformed;

import java.util.Collections;
import java.util.LinkedList;

import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.TreeGoalTestedFirstSearch;

/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class DepthFirstTreeSearch<A, S> extends TreeGoalTestedFirstSearch<A, S> {
	public DepthFirstTreeSearch() {
		super(() -> Collections.asLifoQueue(new LinkedList<>()));
	}
	
	public DepthFirstTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory) {
		super(searchController, nodeFactory, () -> Collections.asLifoQueue(new LinkedList<>()));
	}
}
