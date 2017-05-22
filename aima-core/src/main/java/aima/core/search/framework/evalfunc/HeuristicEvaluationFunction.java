package aima.core.search.framework.evalfunc;

import aima.core.search.framework.Node;
import aima.core.search.framework.SearchUtils;

import java.util.function.Function;

/**
 * Super class for all evaluation functions which make use of heuristics.
 * Informed search algorithms use heuristics to estimate remaining costs to
 * reach a goal state from a given node. Their evaluation functions only differ
 * in the way how they combine the estimated remaining costs with the costs of
 * the already known path to the node.
 * 
 * @author Ruediger Lunde
 *
 */
public abstract class HeuristicEvaluationFunction implements EvaluationFunction {
	protected Function<Object, Double> hf = (state) -> 0.0;

	public Function<Object, Double> getHeuristicFunction() {
		return hf;
	}

	public void setHeuristicFunction(Function<Object, Double> hf) {
		this.hf = hf;
	}
}
