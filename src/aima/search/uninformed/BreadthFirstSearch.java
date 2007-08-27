package aima.search.uninformed;

import java.util.List;

import aima.search.framework.Metrics;
import aima.search.framework.Problem;
import aima.search.framework.QueueSearch;
import aima.search.framework.Search;
import aima.search.nodestore.FIFONodeStore;

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