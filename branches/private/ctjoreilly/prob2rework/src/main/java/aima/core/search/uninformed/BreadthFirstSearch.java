package aima.core.search.uninformed;

import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.Search;
import aima.core.util.datastructure.FIFOQueue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.11, page
 * 82.<br>
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
 * Figure 3.11 Breadth-first search on a graph.<br>
 * <br>
 * <b>Note:</b> Supports both Tree and Graph based versions by assigning an
 * instance of TreeSearch or GraphSearch to its constructor.
 * 
 * @author Ciaran O'Reilly
 */
public class BreadthFirstSearch implements Search {

	private final QueueSearch search;

	public BreadthFirstSearch() {
		this(new GraphSearch());
	}

	public BreadthFirstSearch(QueueSearch search) {
		// Goal test is to be applied to each node when it is generated
		// rather than when it is selected for expansion.
		search.setCheckGoalBeforeAddingToFrontier(true);
		this.search = search;
	}

	public List<Action> search(Problem p) {
		return search.search(p, new FIFOQueue<Node>());
	}

	public Metrics getMetrics() {
		return search.getMetrics();
	}
}