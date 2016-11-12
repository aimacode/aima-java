package aima.core.search.framework;

import java.util.Comparator;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.QueueSearch;

/**
 * Performs search by creating a priority queue based on a given
 * <code>Comparator</code> and feeding it to a given <code>QueueSearch</code>
 * implementation which finally controls the simulated search space exploration.
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class PrioritySearch implements SearchForActions, SearchForStates {
	private final QueueSearch implementation;
	private final Comparator<Node> comparator;

	public PrioritySearch(QueueSearch impl, Comparator<Node> comp) {
		implementation = impl;
		comparator = comp;
	}

	@Override
	public List<Action> findActions(Problem p) {
		implementation.getNodeExpander().useParentLinks(true);
		Node node = implementation.findNode(p, QueueFactory.<Node>createPriorityQueue(comparator));
		return node == null ? SearchUtils.failure() : SearchUtils.getSequenceOfActions(node);
	}

	@Override
	public Object findState(Problem p) {
		implementation.getNodeExpander().useParentLinks(false);
		Node node = implementation.findNode(p, QueueFactory.<Node>createPriorityQueue(comparator));
		return node == null ? null : node.getState();
	}

	public Comparator<Node> getComparator() {
		return comparator;
	}

	@Override
	public NodeExpander getNodeExpander() {
		return implementation.getNodeExpander();
	}

	@Override
	public Metrics getMetrics() {
		return implementation.getMetrics();
	}
}