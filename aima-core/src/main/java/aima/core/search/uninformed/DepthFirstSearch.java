package aima.core.search.uninformed;

import java.util.List;
import java.util.function.Consumer;

import aima.core.agent.Action;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.QueueFactory;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.SearchForStates;
import aima.core.search.framework.SearchUtils;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 85.<br>
 * <br>
 * Depth-first search always expands the deepest node in the current frontier of
 * the search tree. <br>
 * <br>
 * <b>Note:</b> Supports TreeSearch, GraphSearch, and BidirectionalSearch. Just
 * provide an instance of the desired QueueSearch implementation to the
 * constructor!
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * 
 */
public class DepthFirstSearch implements SearchForActions, SearchForStates {

	private QueueSearch implementation;

	public DepthFirstSearch(QueueSearch impl) {
		implementation = impl;
	}

	@Override
	public List<Action> findActions(Problem p) {
		implementation.getNodeExpander().useParentLinks(true);
		Node node = implementation.findNode(p, QueueFactory.createLifoQueue());
		return node == null ? SearchUtils.failure() : SearchUtils.getSequenceOfActions(node);
	}
	
	@Override
	public Object findState(Problem p) {
		implementation.getNodeExpander().useParentLinks(false);
		Node node = implementation.findNode(p, QueueFactory.createLifoQueue());
		return node == null ? null : node.getState();
	}
	
	@Override
	public Metrics getMetrics() {
		return implementation.getMetrics();
	}

	@Override
	public void addNodeListener(Consumer<Node> listener)  {
		implementation.getNodeExpander().addNodeListener(listener);
	}

	@Override
	public boolean removeNodeListener(Consumer<Node> listener) {
		return implementation.getNodeExpander().removeNodeListener(listener);
	}
}