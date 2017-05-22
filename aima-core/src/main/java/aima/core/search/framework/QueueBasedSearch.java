package aima.core.search.framework;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.QueueSearch;

/**
 * Base class for all search algorithms which use a queue to manage not yet
 * explored nodes. Subclasses are responsible for node prioritization. They
 * define the concrete queue to be used as frontier in their constructor.
 * Search space exploration control is always delegated to some
 * <code>QueueSearch</code> implementation.
 *
 * @author Ruediger Lunde
 */
public abstract class QueueBasedSearch implements SearchForActions, SearchForStates {
	protected final QueueSearch impl;
	private final Queue<Node> frontier;

	protected QueueBasedSearch(QueueSearch impl, Queue<Node> queue) {
		this.impl = impl;
		this.frontier = queue;
	}

	@Override
	public List<Action> findActions(Problem p) {
		impl.getNodeExpander().useParentLinks(true);
		frontier.clear();
		Node node = impl.findNode(p, frontier);
		return node == null ? SearchUtils.failure() : SearchUtils.getSequenceOfActions(node);
	}

	@Override
	public Object findState(Problem p) {
		impl.getNodeExpander().useParentLinks(false);
		frontier.clear();
		Node node = impl.findNode(p, frontier);
		return node == null ? null : node.getState();
	}

	@Override
	public Metrics getMetrics() {
		return impl.getMetrics();
	}

	@Override
	public void addNodeListener(Consumer<Node> listener)  {
		impl.getNodeExpander().addNodeListener(listener);
	}

	@Override
	public boolean removeNodeListener(Consumer<Node> listener) {
		return impl.getNodeExpander().removeNodeListener(listener);
	}
}