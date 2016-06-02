package aima.core.search.basic.uninformed;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.GraphPrioritySearch;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;
import aima.core.search.basic.support.BasicSearchController;

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
 * @author Ruediger Lunde
 */
public class UniformCostGraphSearch<A, S> extends GraphPrioritySearch<A, S> {
	public UniformCostGraphSearch() {
    	this(new BasicSearchController<>(), new BasicNodeFactory<>(), HashSet::new);
    }
	
	public UniformCostGraphSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory, Supplier<Set<S>> exploredSupplier) {
    	super(searchController, nodeFactory, () -> new BasicPriorityFrontierQueue<A, S>((n1, n2) -> Double.compare(n1.pathCost(), n2.pathCost())), exploredSupplier);
    }
}