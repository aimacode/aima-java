package aima.core.search.basic;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.PriorityFrontierQueue;
import aima.core.search.api.Problem;
import aima.core.search.api.Search;
import aima.core.search.api.SearchController;

/**
 * <pre>
 * function GRAPH-SHORTEST-PATH-PRIORITY-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE
 *   frontier &lt;- a priority queue, with node as the only element
 *   explored &lt;- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the highest priority node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *             frontier &lt;- INSERT(child, frontier)
 *          else if child.STATE is in frontier with lower priority then
 *             replace that frontier node with child
 * </pre>
 *
 * The algorithm is identical to the general graph search algorithm in Figure ??, 
 * except for the use of a priority queue and the addition of an extra check in 
 * case a higher priority to a frontier state is discovered. The data structure for 
 * frontier needs to support efficient membership testing, so it should combine the 
 * capabilities of a priority queue and a hash table.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class GraphPrioritySearch<A, S> implements Search<A, S> {
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;
    private Supplier<PriorityFrontierQueue<A, S>> frontierSupplier;
    private Supplier<Set<S>> exploredSupplier;
	
	public GraphPrioritySearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory, Supplier<PriorityFrontierQueue<A, S>> frontierSupplier, Supplier<Set<S>> exploredSupplier) {
		setSearchController(searchController);
		setNodeFactory(nodeFactory);
    	setFrontierSupplier(frontierSupplier);
    	setExploredSupplier(exploredSupplier);
    }
	
	// function GRAPH-SHORTEST-PATH-PRIORITY-SEARCH((problem) returns a solution, or failure
    @Override
    public List<A> apply(Problem<A, S> problem) {
        // node <- a node with STATE = problem.INITIAL-STATE
        Node<A, S> node = nodeFactory.newRootNode(problem.initialState(), 0);
        // frontier <- a priority queue, with node as the only element
        PriorityFrontierQueue<A, S> frontier = frontierSupplier.get();
        frontier.add(node);
        // explored <- an empty set
        Set<S> explored = exploredSupplier.get();
        // loop do
        while (searchController.isKeepSearchingTillGoalFound()) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty()) { return searchController.failure(); }
            // node <- POP(frontier) // chooses the highest priority node in frontier
            node = frontier.remove();
            // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
            if (searchController.isGoalState(node, problem)) { return searchController.solution(node); }
            // add node.STATE to explored
            explored.add(node.state());
            // for each action in problem.ACTIONS(node.STATE) do
            for (A action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<A, S> child = nodeFactory.newChildNode(problem, node, action);
                // if child.STATE is not in explored or frontier then
                boolean childStateInFrontier = frontier.containsState(child.state());
                if (!(childStateInFrontier || explored.contains(child.state()))) {
                    // frontier <- INSERT(child, frontier)
                    frontier.add(child);
                } // else if child.STATE is in frontier with lower priority then
                else if (childStateInFrontier &&
                		// NOTE: by Java's PriorityQueue convention, nodes that compare lower have a higher priority.
                        frontier.removeIf(n -> child.state().equals(n.state()) && frontier.getComparator().compare(child, n) < 0)) {                	
                    // replace that frontier node with child
                    frontier.add(child);
                }
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
    
    public void setFrontierSupplier(Supplier<PriorityFrontierQueue<A, S>> frontierSupplier) {
    	this.frontierSupplier = frontierSupplier;
    }
    
    public void setExploredSupplier(Supplier<Set<S>> exploredSupplier) {
    	this.exploredSupplier = exploredSupplier;
    }
}
