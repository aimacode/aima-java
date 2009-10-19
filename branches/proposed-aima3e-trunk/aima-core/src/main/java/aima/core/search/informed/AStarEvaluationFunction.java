package aima.core.search.informed;

import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.Node;
import aima.core.search.framework.Problem;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class AStarEvaluationFunction implements EvaluationFunction {

	public AStarEvaluationFunction() {
	}

	public Double getValue(Problem p, Node n) {
		// f(n) = g(n) + h(n)
		return new Double(n.getPathCost()
				+ p.getHeuristicFunction().getHeuristicValue(n.getState()));
	}
}
