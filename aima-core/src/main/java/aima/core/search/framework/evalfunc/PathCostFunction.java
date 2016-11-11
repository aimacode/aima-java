package aima.core.search.framework.evalfunc;

import aima.core.search.framework.Node;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 78.<br>
 * <br>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class PathCostFunction {
	public PathCostFunction() {
	}

	/**
	 * 
	 * @param n
	 * @return the cost, traditionally denoted by g(n), of the path from the
	 *         initial state to the node, as indicated by the parent pointers.
	 */
	public double g(Node n) {
		return n.getPathCost();
	}
}
