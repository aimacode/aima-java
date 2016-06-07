package aima.core.search.basic;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function TREE-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of the problem
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     expand the chosen node, adding the resulting nodes to the frontier
 * </pre>
 *
 * Figure ?? An informal description of the general tree-search algorithm.
 *
 * @author Ciaran O'Reilly
 */
public class TreeSearch<A, S> implements SearchForActionsFunction<A, S> {
	// function TREE-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of the problem
		Queue<Node<A, S>> frontier = frontierSupplier.get();
		frontier.add(nodeFactory.newRootNode(problem.initialState()));
		// loop do
		while (searchController.isExecuting()) {
			// if the frontier is empty then return failure
			if (frontier.isEmpty()) { return searchController.failure(); }
			// choose a leaf node and remove it from the frontier
			Node<A, S> node = frontier.remove();
			// if the node contains a goal state then return the corresponding solution
			if (searchController.isGoalState(node, problem)) { return searchController.solution(node);}
			// expand the chosen node, adding the resulting nodes to the frontier
			for (A action : problem.actions(node.state())) {
				frontier.add(nodeFactory.newChildNode(problem, node, action));
			}
		}
		return searchController.failure();
	}

	//
	// Supporting Code
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;
	private Supplier<Queue<Node<A, S>>> frontierSupplier;

	public TreeSearch() {
		this(new BasicSearchController<>(), new BasicNodeFactory<>(), LinkedList::new);
	}

	public TreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory,
			Supplier<Queue<Node<A, S>>> frontierSupplier) {
		setSearchController(searchController);
		setNodeFactory(nodeFactory);
		setFrontierSupplier(frontierSupplier);
	}

	public void setSearchController(SearchController<A, S> searchController) {
		this.searchController = searchController;
	}

	public void setNodeFactory(NodeFactory<A, S> nodeFactory) {
		this.nodeFactory = nodeFactory;
	}

	public void setFrontierSupplier(Supplier<Queue<Node<A, S>>> frontierSupplier) {
		this.frontierSupplier = frontierSupplier;
	}
}