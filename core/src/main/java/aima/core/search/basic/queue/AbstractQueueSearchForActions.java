package aima.core.search.basic.queue;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

public abstract class AbstractQueueSearchForActions<A, S> implements QueueSearchForActions<A, S> {
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;
	private Supplier<Queue<Node<A, S>>> frontierSupplier;
	private Supplier<Set<S>> exploredSupplier;
	
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
			frontierSupplier = BasicFrontierQueue::new;
		}
		return frontierSupplier;
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
	
	public Queue<Node<A, S>> newFrontier(S initialState) {
		return newFrontier(getNodeFactory().newRootNode(initialState));
	}
	
	public Queue<Node<A, S>> newFrontier(Node<A, S> initialNode) {
		Queue<Node<A, S>> frontier = getFrontierSupplier().get();
		frontier.add(initialNode);
		return frontier;
	}
	
	public boolean containsState(Queue<Node<A, S>> frontier, S state) {
		return frontier.contains(state);
	}
}