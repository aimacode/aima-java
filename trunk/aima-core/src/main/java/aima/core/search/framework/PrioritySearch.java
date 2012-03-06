package aima.core.search.framework;

import java.util.Comparator;
import java.util.List;

import aima.core.agent.Action;
import aima.core.util.datastructure.PriorityQueue;

/**
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public abstract class PrioritySearch implements Search {
	private final QueueSearch search;
	protected final Comparator<Node> comparator;

	protected PrioritySearch(QueueSearch search, Comparator<Node> comparator) {
		this.search = search;
		this.comparator = comparator;
	}
	
	public List<Action> search(Problem p) throws Exception {
		return search.search(p, new PriorityQueue<Node>(5, comparator));
	}

	public Metrics getMetrics() {
		return search.getMetrics();
	}
}