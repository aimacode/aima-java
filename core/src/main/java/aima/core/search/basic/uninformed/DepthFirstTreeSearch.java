package aima.core.search.basic.uninformed;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.TreeGoalTestedFirstSearch;
import aima.core.search.basic.support.BasicFrontierQueueWithStateTracking;

/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class DepthFirstTreeSearch<A, S> extends TreeGoalTestedFirstSearch<A, S> {
	public DepthFirstTreeSearch() {
		super(() -> new BasicFrontierQueueWithStateTracking<A, S>(() -> Collections.asLifoQueue(new LinkedList<>()), HashSet::new));
	}
	
	public DepthFirstTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory) {
		super(searchController, nodeFactory, () -> new BasicFrontierQueueWithStateTracking<A, S>(() -> Collections.asLifoQueue(new LinkedList<>()), HashSet::new));
	}
}
