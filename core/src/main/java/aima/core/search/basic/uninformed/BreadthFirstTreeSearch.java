package aima.core.search.basic.uninformed;

import java.util.HashSet;
import java.util.LinkedList;

import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.TreeGoalTestedFirstSearch;
import aima.core.search.basic.support.BasicFrontierQueueWithStateTracking;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class BreadthFirstTreeSearch<A, S> extends TreeGoalTestedFirstSearch<A, S> {
	public BreadthFirstTreeSearch() {
		super(() -> new BasicFrontierQueueWithStateTracking<A, S>(LinkedList::new, HashSet::new));
	}
	
	public BreadthFirstTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory) {
		super(searchController, nodeFactory, () -> new BasicFrontierQueueWithStateTracking<A, S>(LinkedList::new, HashSet::new));
	}
}