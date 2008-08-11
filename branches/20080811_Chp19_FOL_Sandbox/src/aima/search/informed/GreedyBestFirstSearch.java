/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.informed;

import aima.search.framework.BestFirstSearch;
import aima.search.framework.QueueSearch;

/**
 * @author Ravi Mohan
 * 
 */

public class GreedyBestFirstSearch extends BestFirstSearch {

	public GreedyBestFirstSearch(QueueSearch search) {
		super(search, new GreedyBestFirstEvaluationFunction());
	}
}