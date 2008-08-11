/*
 * Created on Sep 8, 2004
 *
 */
package aima.search.uninformed;

import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Metrics;
import aima.search.framework.NodeExpander;
import aima.search.framework.Problem;
import aima.search.framework.Search;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 3.14, page
 * 78.
 * 
 * <code>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   inputs: problem, a problem
 *   
 *   for depth <- to infinity  do
 *     result <- DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </code>
 * Figure 3.14 The iterative deepening search algorithm, which repeatedly
 * applies depth- limited search with increasing limits. It terminates when a
 * solution is found or if the depth- limited search returns failure, meaning
 * that no solution exists.
 */

/**
 * @author Ravi Mohan
 * 
 */
public class IterativeDeepeningSearch extends NodeExpander implements Search {
	private static String PATH_COST = "pathCost";

	private final int limit;

	private final Metrics iterationMetrics;

	public IterativeDeepeningSearch() {
		this.limit = Integer.MAX_VALUE;
		iterationMetrics = new Metrics();
		iterationMetrics.set(NODES_EXPANDED, 0);
		iterationMetrics.set(PATH_COST, 0);
	}

	// function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or
	// failure
	// inputs: problem, a problem
	public List search(Problem p) throws Exception {
		iterationMetrics.set(NODES_EXPANDED, 0);
		iterationMetrics.set(PATH_COST, 0);
		// for depth <- to infinity do
		for (int i = 1; i <= limit; i++) {
			// result <- DEPTH-LIMITED-SEARCH(problem, depth)
			DepthLimitedSearch dls = new DepthLimitedSearch(i);
			List result = dls.search(p);
			iterationMetrics.set(NODES_EXPANDED, iterationMetrics
					.getInt(NODES_EXPANDED)
					+ dls.getMetrics().getInt(NODES_EXPANDED));
			// if result != cutoff then return result
			if (!cutOffResult(result)) {
				iterationMetrics.set(PATH_COST, dls.getPathCost());
				return result;
			}
		}
		return new ArrayList();// failure
	}

	private boolean cutOffResult(List result) { // TODO remove this duplication

		return result.size() == 1 && result.get(0).equals("cutoff");
	}

	@Override
	public Metrics getMetrics() {
		return iterationMetrics;
	}

}