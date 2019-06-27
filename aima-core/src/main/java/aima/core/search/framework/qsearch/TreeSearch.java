package aima.core.search.framework.qsearch;

import java.util.Optional;
import java.util.Queue;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeFactory;
import aima.core.search.framework.problem.Problem;
import aima.core.util.Tasks;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77.
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
 * Figure 3.7 An informal description of the general tree-search algorithm.
 * 
 * <br>
 * This class provides an implementation of the abstract method
 * {@link #findNode(Problem, Queue)} from superclass {@link QueueSearch}.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class TreeSearch<S, A> extends QueueSearch<S, A> {

	protected Queue<Node<S, A>> frontier;

	public TreeSearch() {
		this(new NodeFactory<>());
	}

	public TreeSearch(NodeFactory<S, A> nodeFactory) {
		super(nodeFactory);
	}

	/**
	 * Receives a problem and a queue implementing the search strategy and
	 * computes a node referencing a goal state, if such a state was found.
	 * This template method provides a base for tree and graph search
	 * implementations. It can be customized by overriding some primitive
	 * operations, especially {@link #addToFrontier(Node)},
	 * {@link #removeFromFrontier()}, and {@link #isFrontierEmpty()}.
	 *
	 * @param problem
	 *            the search problem
	 * @param frontier
	 *            the data structure for nodes that are waiting to be expanded
	 *
	 * @return a node referencing a goal state, if the goal was found, otherwise empty;
	 */
	@Override
	public Optional<Node<S, A>> findNode(Problem<S, A> problem, Queue<Node<S, A>> frontier) {
		this.frontier = frontier;
		clearMetrics();
		// initialize the frontier using the initial state of the problem
		Node<S, A> root = nodeFactory.createNode(problem.getInitialState());
		addToFrontier(root);
		if (earlyGoalTest && problem.testSolution(root))
			return asOptional(root);

		while (!isFrontierEmpty() && !Tasks.currIsCancelled()) {
			// choose a leaf node and remove it from the frontier
			Node<S, A> node = removeFromFrontier();
			// if the node contains a goal state then return the corresponding solution
			if (!earlyGoalTest && problem.testSolution(node))
				return asOptional(node);

			// expand the chosen node and add the successor nodes to the frontier
			for (Node<S, A> successor : nodeFactory.getSuccessors(node, problem)) {
				addToFrontier(successor);
				if (earlyGoalTest && problem.testSolution(successor))
					return asOptional(successor);
			}
		}
		// if the frontier is empty then return failure
		return Optional.empty();
	}

	/**
	 * Primitive operation which inserts the node at the tail of the frontier.
	 */
	protected void addToFrontier(Node<S, A> node) {
		frontier.add(node);
		updateMetrics(frontier.size());
	}

	/**
	 * Primitive operation which removes and returns the node at the head of the frontier.
	 *
	 * @return the node at the head of the frontier.
	 */
	protected Node<S, A> removeFromFrontier() {
		Node<S, A> result = frontier.remove();
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Primitive operation which checks whether the frontier contains not yet expanded nodes.
	 */
	protected boolean isFrontierEmpty() {
		return frontier.isEmpty();
	}
}