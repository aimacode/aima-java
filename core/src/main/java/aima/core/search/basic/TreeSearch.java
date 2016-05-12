package aima.core.search.basic;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.Search;
import aima.core.search.basic.support.BasicNodeFactory;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
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
public class TreeSearch<A, S> implements Search<A, S> {
	private NodeFactory<A, S> nodeFactory;
    private Supplier<Queue<Node<A, S>>> frontierSupplier;
    
    public TreeSearch() {
    	this(new BasicNodeFactory<>(), LinkedList::new);
    }
    
	public TreeSearch(NodeFactory<A, S> nodeFactory, Supplier<Queue<Node<A, S>>> frontierSupplier) {
		this.nodeFactory      = nodeFactory;
		this.frontierSupplier = frontierSupplier;
	}
	
    // function TREE-SEARCH(problem) returns a solution, or failure
    @Override
    public List<A> apply(Problem<A, S> problem) {
        // initialize the frontier using the initial state of the problem
        Queue<Node<A, S>> frontier = frontierSupplier.get();
        frontier.add(nodeFactory.newRootNode(problem.initialState(), 0));
        // loop do
        while (true) {
            // if the frontier is empty then return failure
            if (frontier.isEmpty()) { return failure(); }
            // choose a leaf node and remove it from the frontier
            Node<A, S> node = frontier.remove();
            // if the node contains a goal state then return the corresponding solution
            if (isGoalState(node, problem)) { return solution(node); }
            // expand the chosen node, adding the resulting nodes to the frontier
            for (A action : problem.actions(node.state())) {
                frontier.add(nodeFactory.newChildNode(problem, node, action));
            }
        }
    }
}
