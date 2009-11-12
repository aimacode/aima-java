package aima.core.search.informed;

import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Node;
import aima.core.search.framework.PathCostFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page ??.
 * 
 * Greedy best-first search tries to expand the node that is closest to the goal,
 * on the grounds that this is likely to lead to a solution quickly. Thus, it evaluates
 * nodes by using just the heuristic function; that is, f(n) = h(n)
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
