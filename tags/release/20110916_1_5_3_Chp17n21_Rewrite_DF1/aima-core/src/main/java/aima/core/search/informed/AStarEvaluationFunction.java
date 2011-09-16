package aima.core.search.informed;

import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Node;
import aima.core.search.framework.PathCostFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 93.<br>
 * <br>
 * The most widely known form of best-first search is called A* search
 * (pronounced "A-star Search"). It evaluates nodes by combining g(n), the cost
 * to reach the node, and h(n), the cost to get from the node to the goal:<br>
 * 
 * <pre>
 *        f(n) = g(n) + h(n).
 * </pre>
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class AStarEvaluationFunction implements EvaluationFunction {

	private PathCostFunction gf = new PathCostFunction();
	private HeuristicFunction hf = null;

	public AStarEvaluationFunction(HeuristicFunction hf) {
		this.hf = hf;
	}

	/**
	 * Returns <em>g(n)</em> the cost to reach the node, plus <em>h(n)</em> the
	 * heuristic cost to get from the specified node to the goal.
	 * 
	 * @param n
	 *            a node
	 * @return g(n) + h(n)
	 */
	public double f(Node n) {
		// f(n) = g(n) + h(n)
		return gf.g(n) + hf.h(n.getState());
	}
}
