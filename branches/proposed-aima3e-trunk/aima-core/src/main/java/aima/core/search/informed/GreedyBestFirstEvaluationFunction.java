package aima.core.search.informed;

import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.Node;
import aima.core.search.framework.Problem;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class GreedyBestFirstEvaluationFunction implements EvaluationFunction {

	public GreedyBestFirstEvaluationFunction() {
	}

	public Double getValue(Problem p, Node n) {
		// f(n) = h(n)
		return new Double(p.getHeuristicFunction().getHeuristicValue(
				n.getState()));
	}
}
