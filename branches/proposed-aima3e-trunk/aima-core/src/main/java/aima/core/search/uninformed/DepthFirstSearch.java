package aima.core.search.uninformed;

import java.util.List;

import aima.core.search.framework.Metrics;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.Search;
import aima.core.search.nodestore.LIFONodeStore;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 75.
 * 
 * Depth-first search.
 */
public class DepthFirstSearch implements Search {

	QueueSearch search;

	public DepthFirstSearch(QueueSearch search) {

		this.search = search;

	}

	public List search(Problem p) {

		return search.search(p, new LIFONodeStore());
	}

	public Metrics getMetrics() {
		return search.getMetrics();
	}

}