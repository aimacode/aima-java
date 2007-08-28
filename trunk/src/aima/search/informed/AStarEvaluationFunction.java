package aima.search.informed;

import aima.search.framework.EvaluationFunction;
import aima.search.framework.Node;
import aima.search.framework.Problem;

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
