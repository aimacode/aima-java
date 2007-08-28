package aima.search.informed;

import aima.search.framework.EvaluationFunction;
import aima.search.framework.Node;
import aima.search.framework.Problem;

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
