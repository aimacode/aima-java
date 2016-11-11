package aima.core.search.informed;

import aima.core.search.framework.Node;
import aima.core.search.framework.evalfunc.HeuristicFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Greedy best-first search tries to expand the node that is closest to the
 * goal, on the grounds that this is likely to lead to a solution quickly. Thus,
 * it evaluates nodes by using just the heuristic function; that is, f(n) = h(n)
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class GreedyBestFirstEvaluationFunction extends HeuristicEvaluationFunction {

	public GreedyBestFirstEvaluationFunction(HeuristicFunction hf) {
		this.hf = hf;
	}

	/** Note: Do not forget to heuristic function before starting the search! */
	public GreedyBestFirstEvaluationFunction() {
	}

	@Override
	public double f(Node n) {
		// f(n) = h(n)
		return hf.h(n.getState());
	}
}
