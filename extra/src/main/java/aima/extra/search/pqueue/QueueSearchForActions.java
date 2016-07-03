package aima.extra.search.pqueue;

import java.util.Comparator;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.api.SearchForActionsFunction;

public interface QueueSearchForActions<A, S> extends SearchForActionsFunction<A, S> {
	// A marker interface, where implementations of QueueSearchForActions should
	// implement this sub-interface if they need to do state containment
	// checks on the frontier.
	interface DoesStateContainmentCheckingOnFrontier {
	}

	SearchController<A, S> getSearchController();

	void setSearchController(SearchController<A, S> searchController);

	NodeFactory<A, S> getNodeFactory();

	void setNodeFactory(NodeFactory<A, S> nodeFactory);
	
	default Comparator<Node<A, S>> getNodeComparator() {
		return getNodeFactory().getNodeComparator();
	}

	Supplier<Queue<Node<A, S>>> getFrontierSupplier();

	void setFrontierSupplier(Supplier<Queue<Node<A, S>>> frontierSupplier);

	Supplier<Set<S>> getExploredSupplier();

	void setExploredSupplier(Supplier<Set<S>> exploredSupplier);
}
