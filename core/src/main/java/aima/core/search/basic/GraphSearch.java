package aima.core.search.basic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.FrontierQueueWithStateTracking;
import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicFrontierQueueWithStateTracking;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function GRAPH-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of problem
 *   initialize the explored set to be empty
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     add the node to the explored set
 *     expand the chosen node, adding the resulting nodes to the frontier
 *       only if not in the frontier or explored set
 * </pre>
 *
 * Figure ?? An informal description of the general graph-search algorithm.
 *
 *
 * @author Ciaran O'Reilly
 */
public class GraphSearch<A, S> implements SearchForActionsFunction<A, S> {

	// function GRAPH-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of problem
		FrontierQueueWithStateTracking<A, S> frontier = frontierSupplier.get();
		frontier.add(nodeFactory.newRootNode(problem.initialState(), 0));
		// initialize the explored set to be empty
		Set<S> explored = exploredSupplier.get();
		// loop do
		while (searchController.isExecuting()) {
			// if the frontier is empty then return failure
			if (frontier.isEmpty()) { return searchController.failure(); }
			// choose a leaf node and remove it from the frontier
			Node<A, S> node = frontier.remove();
			// if the node contains a goal state then return the corresponding solution
			if (searchController.isGoalState(node, problem)) { return searchController.solution(node); }
			// add the node to the explored set
			explored.add(node.state());
			// expand the chosen node, adding the resulting nodes to the frontier
			for (A action : problem.actions(node.state())) {
				Node<A, S> child = nodeFactory.newChildNode(problem, node, action);
				// only if not in the frontier or explored set
				if (!(frontier.containsState(child.state()) || explored.contains(child.state()))) {
					frontier.add(child);
				}
			}
		}
		return searchController.failure();
	}
	
	//
	// Supporting Code
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;
	private Supplier<FrontierQueueWithStateTracking<A, S>> frontierSupplier;
	private Supplier<Set<S>> exploredSupplier;

	public GraphSearch() {
		this(new BasicSearchController<>(), new BasicNodeFactory<>(), BasicFrontierQueueWithStateTracking::new,
				HashSet::new);
	}

	public GraphSearch(Supplier<FrontierQueueWithStateTracking<A, S>> frontierSupplier) {
		this(new BasicSearchController<>(), new BasicNodeFactory<>(), frontierSupplier, HashSet::new);
	}

	public GraphSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory,
			Supplier<FrontierQueueWithStateTracking<A, S>> frontierSupplier, Supplier<Set<S>> exploredSupplier) {
		setSearchController(searchController);
		setNodeFactory(nodeFactory);
		setFrontierSupplier(frontierSupplier);
		setExploredSupplier(exploredSupplier);
	}

	public void setSearchController(SearchController<A, S> searchController) {
		this.searchController = searchController;
	}

	public void setNodeFactory(NodeFactory<A, S> nodeFactory) {
		this.nodeFactory = nodeFactory;
	}

	public void setFrontierSupplier(Supplier<FrontierQueueWithStateTracking<A, S>> frontierSupplier) {
		this.frontierSupplier = frontierSupplier;
	}

	public void setExploredSupplier(Supplier<Set<S>> exploredSupplier) {
		this.exploredSupplier = exploredSupplier;
	}
}
