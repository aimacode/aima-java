package aima.core.search.basic.uninformed;

import aima.core.search.api.*;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

import java.util.*;

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
 * <p>
 * Figure ?? An informal description of the general graph-search algorithm.
 *
 * @author Ciaran O'Reilly
 */
public class GraphSearch<A, S> implements SearchForActionsFunction<A, S> {
	
	//
	// Supporting Code
	protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
	protected SearchController<A, S> searchController = new BasicSearchController<A, S>();
	
	public GraphSearch() {
	}

	// function GRAPH-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of problem
		Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
		// initialize the reached table to be empty
		Map<S, Node<A, S>> reached = new HashMap<>();
		// initialize the solution
		List<A> solution = failure();
		
		// if the frontier is empty then return failure
		if (frontier.isEmpty()) {
			return failure();
		}
		// loop do
		while (!frontier.isEmpty()) {
			// choose a leaf node and remove it from the frontier
			Node<A, S> parent = frontier.remove();
			// expand the chosen node
			for (Node<A, S> child : expand(problem, parent)) {
				// only if the child is not in reached or child is a cheaper path than reached[child.state()]
				if (!reached.containsKey(child.state()) || child.pathCost() < reached.get(child.state()).pathCost()) {
					// add child in reached and frontier
					reached.put(child.state(), child);
					frontier.add(child);
					// if child is a goal and is cheaper than the best solution found so far then update the solution
					if (isGoalState(child, problem)){
						solution = getSolution(child);
					}
				}
			}
		}
		return solution;
	}
	
	public List<Node<A, S>> expand(Problem<A, S> problem, Node<A, S> parent) {
		List<Node<A, S>> nodes = new ArrayList<>();
		for (A action : problem.actions(parent.state())) {
			Node<A, S> node = newChildNode(problem, parent, action);
			nodes.add(node);
		}
		return nodes;
	}
	
	public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
		return nodeFactory.newChildNode(problem, node, action);
	}
	
	public Queue<Node<A, S>> newFrontier(S initialState) {
		Queue<Node<A, S>> frontier = new LinkedList<>();
		frontier.add(nodeFactory.newRootNode(initialState));
		return frontier;
	}
	
	public List<A> failure() {
		return searchController.failure();
	}

	public List<A> getSolution(Node<A, S> node) {
		return searchController.solution(node);
	}
	
	public boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
		return searchController.isGoalState(node, problem);
	}
	
}
