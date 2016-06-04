package aima.core.search.basic;

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
 * <pre>
 * function TREE-GOAL-TESTED-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &lt;- a queue with node as the only element
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the shallowest node in frontier
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *          frontier &lt;- INSERT(child, frontier)
 * </pre>
 *
 * An instance of the general tree-search algorithm (Figure ?.?) in which the
 * node is chosen for expansion by using a queue for the frontier. There is one 
 * slight tweak on the general tree-search algorithm, which is that the goal test 
 * is applied to each node when it is generated rather than when it is selected for 
 * expansion.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class TreeGoalTestedFirstSearch<A, S> implements SearchForActionsFunction<A, S> {
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;
    private Supplier<Queue<Node<A, S>>> frontierSupplier;
    
    public TreeGoalTestedFirstSearch(Supplier<Queue<Node<A, S>>> frontierSupplier) {
    	this(new BasicSearchController<>(), new BasicNodeFactory<>(), frontierSupplier);
    }
    
    public TreeGoalTestedFirstSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory, Supplier<Queue<Node<A, S>>> frontierSupplier) {
    	setSearchController(searchController);
    	setNodeFactory(nodeFactory);
    	setFrontierSupplier(frontierSupplier);
    }
	
	// function TREE-GOAL-TESTED-FIRST-SEARCH(problem) returns a solution, or failure
    @Override
    public List<A> apply(Problem<A, S> problem) {
        // node <- a node with STATE = problem.INITIAL-STATE
        Node<A, S> node = nodeFactory.newRootNode(problem.initialState(), 0);
        // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
        if (searchController.isGoalState(node, problem)) { return searchController.solution(node); }
        // frontier <- a queue with node as the only element
        Queue<Node<A, S>> frontier = frontierSupplier.get();
        frontier.add(node);
        // loop do
        while (searchController.isExecuting()) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty()) { return searchController.failure(); }
            // node <- POP(frontier) // chooses the shallowest node in frontier
            node = frontier.remove();
            // for each action in problem.ACTIONS(node.STATE) do
            for (A action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<A, S> child = nodeFactory.newChildNode(problem, node, action);
                // if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
                if (searchController.isGoalState(child, problem)) { return searchController.solution(child); }
                // frontier <- INSERT(child, frontier)
                frontier.add(child);
            }
        }
        return searchController.failure();
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
