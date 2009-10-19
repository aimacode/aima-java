package aima.core.search.uninformed;

import java.util.List;

import aima.core.search.framework.Metrics;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.Search;
import aima.core.search.nodestore.FIFONodeStore;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 73.
 * 
 * Breadth-first search.
 */
public class BreadthFirstSearch implements Search {

	private final QueueSearch search;

	public BreadthFirstSearch(QueueSearch search) {
		this.search = search;
	}

	public List search(Problem p) {
		return search.search(p, new FIFONodeStore());
	}

	public Metrics getMetrics() {
		return search.getMetrics();
	}

}