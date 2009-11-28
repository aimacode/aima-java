package aima.core.search.informed;

import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Node;
import aima.core.search.framework.PathCostFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 93.
 * 
 * The most widely known form of best-first search is called A* search (pronounced "A-star
 * Search"). It evaluates nodes by combining g(n), the cost to reach the node, and h(n), the cost
 * to get from the node to the goal:<br>
 * <code>
 *        f(n) = g(n) + h(n).
 * </code>
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class AStarEvaluationFunction implements EvaluationFunction {

	private PathCostFunction gf = new PathCostFunction();
	private HeuristicFunction hf = null;

	public AStarEvaluationFunction(HeuristicFunction hf) {
		this.hf = hf;
	}

	public double f(Node n) {
		// f(n) = g(n) + h(n)
		return gf.g(n) + hf.h(n.getState());
	}
}
