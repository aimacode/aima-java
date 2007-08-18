package aima.search.uninformed;

import java.util.Comparator;
import java.util.List;

import aima.search.framework.Metrics;
import aima.search.framework.Node;
import aima.search.framework.Problem;
import aima.search.framework.QueueSearch;
import aima.search.framework.Search;
import aima.search.nodestore.PriorityNodeStore;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 75.
 * Uniform-cost search.
 */

/**
 * @author Ciaran O'Reilly
 *
 */
public class UniformCostSearch implements Search {

	private QueueSearch search;

	public UniformCostSearch(QueueSearch search) {
		this.search = search;
	}

	public List search(Problem p) {
		return search.search(p, new PriorityNodeStore(new Comparator<Node>() {
			public int compare(Node o1, Node o2)  {
				return (new Double(o1.getPathCost()).compareTo(new Double(o2.getPathCost())));
			}
		}));
	}

	public Metrics getMetrics() {
		return search.getMetrics();
	}
}