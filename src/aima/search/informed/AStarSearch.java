/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.informed;

import aima.search.framework.BestFirstSearch;
import aima.search.framework.EvaluationFunction;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Node;
import aima.search.framework.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 97.
 * 
 * A* search: Minimizing the total estimated solution cost.
 */

/**
 * @author Ravi Mohan
 * 
 */
public class AStarSearch extends BestFirstSearch {

	public AStarSearch(QueueSearch search, final HeuristicFunction hf) {
		super(search, new EvaluationFunction() {
			public Double getValue(Node n) {
				// f(n) = g(n) + h(n)
				return new Double(n.getPathCost()
						+ hf.getHeuristicValue(n.getState()));
			}
		});
	}
}