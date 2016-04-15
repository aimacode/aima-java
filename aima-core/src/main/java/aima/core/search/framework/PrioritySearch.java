package aima.core.search.framework;

import java.util.Comparator;
import java.util.List;

import aima.core.agent.Action;
import aima.core.util.datastructure.PriorityQueue;

/**
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class PrioritySearch implements Search {
	private final QueueSearch implementation;
	private final Comparator<Node> comparator;

	public PrioritySearch(QueueSearch impl, Comparator<Node> comparator) {
		this.implementation = impl;
		this.comparator = comparator;
	}
	
	public List<Action> search(Problem p) throws Exception {
		return implementation.search(p, new PriorityQueue<Node>(5, comparator));
	}
	
	public Comparator<Node> getComparator() {
		return comparator;
	}

	public Metrics getMetrics() {
		return implementation.getMetrics();
	}
}