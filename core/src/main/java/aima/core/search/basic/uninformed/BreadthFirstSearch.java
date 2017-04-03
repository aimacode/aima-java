package aima.core.search.basic.uninformed;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * <pre>
 * function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &larr; a FIFO queue with node as the only element
 *   explored &larr; an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &larr; POP(frontier) // chooses the shallowest node in frontier 
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &larr; CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *              if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *              frontier &larr; INSERT(child, frontier)
 * </pre>
 *
 * Figure ?? Breadth-first graph search.
 *
 * Breadth-first search is an instance of the general graph-search algorithm
 * (Figure ??) in which the shallowest unexpanded node is chosen for expansion.
 * This is achieved very simply by using a FIFO queue for the frontier. Thus,
 * new nodes (which are always deeper than their parents) fo to the back of the
 * queue, and old nodes, which are shallower than the new nodes, get expanded
 * first. There is one slight tweak on the general graph-search algorithm, which
 * is that the goal test is applied to each node when it is generated rather
 * than when it is selected for expansion.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class BreadthFirstSearch<A, S> implements SearchForActionsFunction<A, S> {
	// function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// node <- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
		Node<A, S> node = newRootNode(problem.initialState(), 0);
		// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
		if (isGoalState(node, problem)) {
			return solution(node);
		}
		// frontier <- a FIFO queue with node as the only element
		Queue<Node<A, S>> frontier = newFIFOQueue(node);
		// explored <- an empty set
		Set<S> explored = newExploredSet();
		// loop do
		while (true) {
			// if EMPTY?(frontier) then return failure
			if (frontier.isEmpty()) {
				return failure();
			}
			// node <- POP(frontier) // chooses the shallowest node in frontier
			node = frontier.remove();
			// add node.STATE to explored
			explored.add(node.state());
			// for each action in problem.ACTIONS(node.STATE) do
			for (A action : problem.actions(node.state())) {
				// child <- CHILD-NODE(problem, node, action)
				Node<A, S> child = newChildNode(problem, node, action);
				// if child.STATE is not in explored or frontier then
				if (!(explored.contains(child.state()) || containsState(frontier, child.state()))) {
					// if problem.GOAL-TEST(child.STATE) then return
					// SOLUTION(child)
					if (isGoalState(child, problem)) {
						return solution(child);
					}
					// frontier <- INSERT(child, frontier)
					frontier.add(child);
				}
			}
		}
	}

	//
	// Supporting Code
	protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
	protected SearchController<A, S> searchController = new BasicSearchController<A, S>();

	public BreadthFirstSearch() {
	}

	public Node<A, S> newRootNode(S initialState, double pathCost) {
		return nodeFactory.newRootNode(initialState, pathCost);
	}

	public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
		return nodeFactory.newChildNode(problem, node, action);
	}

	public Queue<Node<A, S>> newFIFOQueue(Node<A, S> initialNode) {
		// NOTE: LinkedList has FIFO queue semantics by default.
		Queue<Node<A, S>> frontier = new LinkedList<>();
		frontier.add(initialNode);
		return frontier;
	}

	public Set<S> newExploredSet() {
		return new HashSet<>();
	}

	public List<A> failure() {
		return searchController.failure();
	}

	public List<A> solution(Node<A, S> node) {
		return searchController.solution(node);
	}
	
	public boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
		return searchController.isGoalState(node, problem);
	}

	public boolean containsState(Queue<Node<A, S>> frontier, S state) {
		// NOTE: Not very efficient (i.e. linear in the size of the frontier)
		return frontier.stream().anyMatch(frontierNode -> frontierNode.state().equals(state));
	}
}
