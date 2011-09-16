package aima.core.search.informed;

import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 93.<br>
 * <br>
 * The most widely known form of best-first search is called A* Search
 * (pronounced "A-star search"). It evaluates nodes by combining g(n), the cost
 * to reach the node, and h(n), the cost to get from the node to the goal:<br>
 * f(n) = g(n) + h(n).<br>
 * <br>
 * Since g(n) gives the path cost from the start node to node n, and h(n) is the
 * estimated cost of the cheapest path from n to the goal, we have<br>
 * f(n) = estimated cost of the cheapest solution through n.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class AStarSearch extends BestFirstSearch {

	/**
	 * Constructs an A* search from the specified search problem and heuristic
	 * function
	 * 
	 * @param search
	 *            a search problem
	 * @param hf
	 *            a heuristic function <em>h(n)</em>, which estimates the cost
	 *            of the cheapest path from the state at node <em>n</em> to a
	 *            goal state.
	 */
	public AStarSearch(QueueSearch search, HeuristicFunction hf) {
		super(search, new AStarEvaluationFunction(hf));
	}
}