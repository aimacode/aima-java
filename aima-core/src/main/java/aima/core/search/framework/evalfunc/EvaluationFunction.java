package aima.core.search.framework.evalfunc;

import aima.core.search.framework.Node;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * The evaluation function is construed as a cost estimate, so the node with the
 * lowest evaluation is expanded first.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface EvaluationFunction {
	double f(Node n);
}
