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
 * @author Ravi Mohan
 * 
 */

public class GreedyBestFirstSearch extends BestFirstSearch {

	public GreedyBestFirstSearch(QueueSearch search, final HeuristicFunction hf) {
		super(search, new EvaluationFunction() {
			public Double getValue(Node n) {
				return new Double(hf.getHeuristicValue(n.getState()));
			}
		});
	}
}