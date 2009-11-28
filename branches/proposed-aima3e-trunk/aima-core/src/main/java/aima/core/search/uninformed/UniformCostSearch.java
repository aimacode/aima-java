package aima.core.search.uninformed;

import java.util.Comparator;

import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Node;
import aima.core.search.framework.PrioritySearch;
import aima.core.search.framework.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.14, page 84. 
 * <code>
 * function UNIFORM-COST-SEARCH(problem) returns a solution, or failure
 *   node <- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
 *   frontier <- a priority queue ordered by PATH-COST, with node as the only element
 *   explored <- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node <- POP(frontier) // chooses the lowest-cost node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child <- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *             frontier <- INSERT(child, frontier)
 *          else if child.STATE is in frontier with higher PATH-COST then
 *             replace that frontier node with child
 * </code> 
 * Figure 3.14 Uniform-cost search on a graph. The algorithm is identical to the general
 * graph search algorithm in Figure 3.7, except for the use of a priority queue and the addition
 * of an extra check in case a shorter path to a frontier state is discovered. The data structure
 * for frontier needs to support efficient membership testing, so it should combine the capabilities
 * of a priority queue and a hash table.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class UniformCostSearch extends PrioritySearch {
	private static final Comparator<Node> _g = new Comparator<Node>() {
		public int compare(Node node1, Node node2) {
			return (new Double(node1.getPathCost()).compareTo(new Double(node2
					.getPathCost())));
		}
	};

	public UniformCostSearch() {
		this(new GraphSearch());
	}

	public UniformCostSearch(QueueSearch search) {
		this.search = search;
		if (search instanceof GraphSearch) {
			((GraphSearch) search)
					.setReplaceFrontierNodeAtStateCostFunction(_g);
		}
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected Comparator<Node> getComparator() {
		return _g;
	}
}