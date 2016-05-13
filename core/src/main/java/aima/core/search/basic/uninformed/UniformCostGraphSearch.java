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
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function UNIFORM-COST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
 *   frontier &lt;- a priority queue ordered by PATH-COST, with node as the only element
 *   explored &lt;- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the lowest-cost node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *             frontier &lt;- INSERT(child, frontier)
 *          else if child.STATE is in frontier with higher PATH-COST then
 *             replace that frontier node with child
 * </pre>
 *
 * Figure ?? Uniform-cost search on a graph. The algorithm is identical to the
 * general graph search algorithm in Figure ??, except for the use of a
 * priority queue and the addition of an extra check in case a shorter path to a
 * frontier state is discovered. The data structure for frontier needs to
 * support efficient membership testing, so it should combine the capabilities
 * of a priority queue and a hash table.
 *
 * @author Ciaran O'Reilly
 */
public class UniformCostGraphSearch<A, S> implements Search<A, S> {
	private NodeFactory<A, S>           nodeFactory;
    private Supplier<Queue<Node<A, S>>> frontierSupplier;
    private Supplier<Set<S>>            exploredSupplier;
    
	public UniformCostGraphSearch() {
    	this(new BasicNodeFactory<>(), () -> new BasicPriorityFrontierQueue<>((n1, n2) -> Double.compare(n1.pathCost(), n2.pathCost())), HashSet::new);
    }
	
	public UniformCostGraphSearch(NodeFactory<A, S> nodeFactory, Supplier<Queue<Node<A, S>>> frontierSupplier, Supplier<Set<S>> exploredSupplier) {
    	this.nodeFactory      = nodeFactory;
    	this.frontierSupplier = frontierSupplier;
    	this.exploredSupplier = exploredSupplier;
    }
	
	// function UNIFORM-COST-SEARCH(problem) returns a solution, or failure
    @Override
    public List<A> apply(Problem<A, S> problem) {
        // node <- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
        Node<A, S> node = nodeFactory.newRootNode(problem.initialState(), 0);
        // frontier <- a priority queue ordered by PATH-COST, with node as the only element
        Queue<Node<A, S>> frontier = frontierSupplier.get();
        frontier.add(node);
        // explored <- an empty set
        Set<S> explored = exploredSupplier.get();
        // loop do
        while (true) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty()) { return failure(); }
            // node <- POP(frontier) // chooses the lowest-cost node in frontier
            node = frontier.remove();
            // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
            if (isGoalState(node, problem)) { return solution(node); }
            // add node.STATE to explored
            explored.add(node.state());
            // for each action in problem.ACTIONS(node.STATE) do
            for (A action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<A, S> child = nodeFactory.newChildNode(problem, node, action);
                // if child.STATE is not in explored or frontier then
                boolean childStateInFrontier = frontier.contains(child.state());
                if (!(childStateInFrontier || explored.contains(child.state()))) {
                    // frontier <- INSERT(child, frontier)
                    frontier.add(child);
                } // else if child.STATE is in frontier with higher PATH-COST then
                else if (childStateInFrontier && frontier.removeIf(n -> n.state().equals(child.state()) && n.pathCost() > child.pathCost())) {
                    // replace that frontier node with child
                    frontier.add(child);
                }
            }
        }
    }
}
