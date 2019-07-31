package aima.core.search.uninformed;

import java.util.Comparator;

import aima.core.search.framework.Node;
import aima.core.search.framework.QueueBasedSearch;
import aima.core.search.framework.QueueFactory;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.14, page
 * 84.<br>
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
 * Figure 3.14 Uniform-cost search on a graph. The algorithm is identical to the
 * general graph search algorithm in Figure 3.7, except for the use of a
 * priority queue and the addition of an extra check in case a shorter path to a
 * frontier state is discovered.
 * 
 * </br>
 * This implementation is more general. It supports TreeSearch, GraphSearch, and
 * BidirectionalSearch by delegating search execution to an instance
 * of type {@link QueueSearch}.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 */
public class UniformCostSearch<S, A> extends QueueBasedSearch<S, A> {

	/** Creates a UniformCostSearch instance using GraphSearch */
	public UniformCostSearch() {
		this(new GraphSearch<>());
	}

	/**
	 * Combines UniformCostSearch queue definition with the specified
	 * search execution strategy.
	 */
	public UniformCostSearch(QueueSearch<S, A> impl) {
		super(impl, QueueFactory.createPriorityQueue(Comparator.comparing(Node::getPathCost)));
	}
}