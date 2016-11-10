package aima.core.search.informed;

import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.SearchUtils;

/**
 * Informed search algorithms use special evaluation functions which are based
 * on heuristics to estimate remaining costs to reach a goal state from a given
 * node. They only differ in the way how they combine the estimated remaining
 * costs with the costs of the already known path to the node.
 * 
 * @author Ruediger Lunde
 *
 */
public abstract class HeuristicEvaluationFunction implements EvaluationFunction {
	protected HeuristicFunction hf = SearchUtils.getZeroHeuristic();
	
	public HeuristicFunction getHeuristicFunction() {
		return hf;
	}

	public void setHeuristicFunction(HeuristicFunction hf) {
		this.hf = hf;
	}
}
