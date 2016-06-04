package aima.core.search.basic.uninformed;

import java.util.LinkedList;

import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.TreeGoalTestedFirstSearch;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class BreadthFirstTreeSearch<A, S> extends TreeGoalTestedFirstSearch<A, S> {
	public BreadthFirstTreeSearch() {
		super(LinkedList::new);
	}
	
	public BreadthFirstTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory) {
		super(searchController, nodeFactory, LinkedList::new);
	}
}