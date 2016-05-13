package aima.core.search.basic.uninformed;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.Search;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &lt;- a FIFO queue with node as the only element
 *   explored &lt;- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the shallowest node in frontier
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *              if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *              frontier &lt;- INSERT(child, frontier)
 * </pre>
 *
 * Figure ?? Breadth-first search on a graph.<br>
 *
 * An instance of the general graph-search algorithm (Figure ?.?) in which the
 * shallowest unexpanded node is chosen for expansion. This is achieved very simply
 * by using a FIFO queue for the frontier. Thus, new nodes (which are always deeper
 * than their parent) go to the back of the queue, and old nodes, which are shallower
 * than the new nodes, get expanded first. There is one slight tweak on the general
 * graph-search algorithm, which is that the goal test is applied to each node
 * when it is generated rather than when it is selected for expansion.
 *
 * @author Ciaran O'Reilly
 */
public class BreadthFirstGraphSearch<A, S> implements Search<A, S> {

	private NodeFactory<A, S>           nodeFactory;
    private Supplier<Queue<Node<A, S>>> frontierSupplier;
    private Supplier<Set<S>>            exploredSupplier;
    
    public BreadthFirstGraphSearch() {
    	this(new BasicNodeFactory<>(), BasicFrontierQueue::new, HashSet::new);
    }
    
    public BreadthFirstGraphSearch(Supplier<Queue<Node<A, S>>> frontierSupplier) {
    	this(new BasicNodeFactory<>(), frontierSupplier, HashSet::new);
    }
    
    public BreadthFirstGraphSearch(NodeFactory<A, S> nodeFactory, Supplier<Queue<Node<A, S>>> frontierSupplier, Supplier<Set<S>> exploredSupplier) {
    	this.nodeFactory      = nodeFactory;
    	this.frontierSupplier = frontierSupplier;
    	this.exploredSupplier = exploredSupplier;
    }
	
	// function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
    @Override
    public List<A> apply(Problem<A, S> problem) {
        // node <- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
        Node<A, S> node = nodeFactory.newRootNode(problem.initialState(), 0);
        // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
        if (isGoalState(node, problem)) { return solution(node); }
        // frontier <- a FIFO queue with node as the only element
        Queue<Node<A, S>> frontier = frontierSupplier.get();
        frontier.add(node);
        // explored <- an empty set
        Set<S> explored = exploredSupplier.get();
        // loop do
        while (true) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty()) { return failure(); }
            // node <- POP(frontier) // chooses the shallowest node in frontier
            node = frontier.remove();
            // add node.STATE to explored
            explored.add(node.state());
            // for each action in problem.ACTIONS(node.STATE) do
            for (A action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<A, S> child = nodeFactory.newChildNode(problem, node, action);
                // if child.STATE is not in explored or frontier then
                if (!(explored.contains(child.state()) || frontier.contains(child.state()))) {
                    // if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
                    if (isGoalState(child, problem)) { return solution(child); }
                    // frontier <- INSERT(child, frontier)
                    frontier.add(child);
                }
            }
        }
    }
}
