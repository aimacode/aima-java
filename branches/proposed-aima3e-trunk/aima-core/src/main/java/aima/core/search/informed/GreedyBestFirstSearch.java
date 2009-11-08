package aima.core.search.informed;

import aima.core.search.framework.QueueSearch;

/**
 * @author Ravi Mohan
 * 
 */

public class GreedyBestFirstSearch extends BestFirstSearch {

	public GreedyBestFirstSearch(QueueSearch search) {
		super(search, new GreedyBestFirstEvaluationFunction());
	}
}