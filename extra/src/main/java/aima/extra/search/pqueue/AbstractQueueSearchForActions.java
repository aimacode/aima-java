package aima.extra.search.pqueue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

public abstract class AbstractQueueSearchForActions<A, S> implements QueueSearchForActions<A, S> {
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;
	private Supplier<Queue<Node<A, S>>> frontierSupplier;
	private Supplier<Set<S>> exploredSupplier;
	private boolean frontierSupportsStateContainmentCheck = false;

	@Override
	public SearchController<A, S> getSearchController() {
		if (searchController == null) {
			searchController = new BasicSearchController<>();
		}
		return searchController;
	}

	@Override
	public void setSearchController(SearchController<A, S> searchController) {
		this.searchController = searchController;
	}

	@Override
	public NodeFactory<A, S> getNodeFactory() {
		if (nodeFactory == null) {
			nodeFactory = new BasicNodeFactory<>();
		}
		return nodeFactory;
	}

	@Override
	public void setNodeFactory(NodeFactory<A, S> nodeFactory) {
		this.nodeFactory = nodeFactory;
	}

	@Override
	public Supplier<Queue<Node<A, S>>> getFrontierSupplier() {
		if (frontierSupplier == null) {
			if (this instanceof QueueSearchForActions.DoesStateContainmentCheckingOnFrontier) {				
				frontierSupplier = getDefaultFrontierSupplierWithStateContainmentCheckingSupport();
			}
			else {				
				frontierSupplier = getDefaultFrontierSupplierWithoutStateContainmentCheckingSupport();
			}
		}
		return frontierSupplier;
	}
	
	public Supplier<Queue<Node<A, S>>> getDefaultFrontierSupplierWithStateContainmentCheckingSupport() {
		// Basic frontier queue supports doing containment checking of a node's state.
		return BasicFrontierQueue::new;
	}
	
	public Supplier<Queue<Node<A, S>>> getDefaultFrontierSupplierWithoutStateContainmentCheckingSupport() {
		// Otherwise the standard java queue implementation is all that is needed by default.
		return LinkedList::new;
	}

	@Override
	public void setFrontierSupplier(Supplier<Queue<Node<A, S>>> frontierSupplier) {
		this.frontierSupplier = frontierSupplier;
	}

	@Override
	public Supplier<Set<S>> getExploredSupplier() {
		if (exploredSupplier == null) {
			exploredSupplier = HashSet::new;
		}
		return exploredSupplier;
	}

	@Override
	public void setExploredSupplier(Supplier<Set<S>> exploredSupplier) {
		this.exploredSupplier = exploredSupplier;
	}

	public Node<A, S> newRootNode(S initialState) {
		return getNodeFactory().newRootNode(initialState);
	}

	public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
		return getNodeFactory().newChildNode(problem, node, action);
	}

	public Queue<Node<A, S>> newFrontier(S initialState) {
		return newFrontier(getNodeFactory().newRootNode(initialState));
	}

	public Queue<Node<A, S>> newFrontier(Node<A, S> initialNode) {
		Queue<Node<A, S>> frontier = getFrontierSupplier().get();
		frontier.add(initialNode);
		// Track if this is supported by the underlying implementation
		frontierSupportsStateContainmentCheck = frontier.contains(initialNode.state());
		return frontier;
	}

	public boolean loopDo() {
		return getSearchController().isExecuting();
	}

	public Set<S> newExploredSet() {
		return new HashSet<>();
	}

	public List<A> failure() {
		return searchController.failure();
	}

	public boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
		return getSearchController().isGoalState(node, problem);
	}

	public List<A> solution(Node<A, S> node) {
		return searchController.solution(node);
	}

	public boolean containsState(Queue<Node<A, S>> frontier, S state) {
		if (!frontierSupportsStateContainmentCheck) {
			throw new IllegalStateException(
					"Algorithm has been configured with a frontier queue that does not support contains(state).");
		}
		return frontier.contains(state);
	}

	public boolean removedNodeFromFrontierWithSameStateAndLowerPriority(Node<A, S> child, Queue<Node<A, S>> frontier) {
		// NOTE: Not very efficient (i.e. linear in the size of the frontier)
		// NOTE: by Java's PriorityQueue convention, nodes that compare lower
		// (i.e. cost) have a higher priority.
		return frontier.removeIf(n -> child.state().equals(n.state()) && getNodeComparator().compare(child, n) < 0);
	}
}