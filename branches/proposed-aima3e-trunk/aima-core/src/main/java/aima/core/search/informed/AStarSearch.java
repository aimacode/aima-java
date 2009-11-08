package aima.core.search.informed;

import aima.core.search.framework.QueueSearch;

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

	public AStarSearch(QueueSearch search) {
		super(search, new AStarEvaluationFunction());
	}
}