package aima.core.search.uninformed;

import java.util.Collections;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.18, page 89.
 * 
 * <code>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result <- DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </code>
 * Figure 3.18 The iterative deepening search algorithm, which repeatedly
 * applies depth-limited search with increasing limits. It terminates when a
 * solution is found or if the depth- limited search returns failure, meaning
 * that no solution exists.
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class IterativeDeepeningSearch extends NodeExpander implements Search {
	public static final String PATH_COST = "pathCost";

	// Not infinity, but will do, :-)
	private final int infinity = Integer.MAX_VALUE;

	private final Metrics iterationMetrics;

	public IterativeDeepeningSearch() {
		iterationMetrics = new Metrics();
		iterationMetrics.set(METRIC_NODES_EXPANDED, 0);
		iterationMetrics.set(PATH_COST, 0);
	}

	// function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or
	// failure
	public List<Action> search(Problem p) throws Exception {
		iterationMetrics.set(METRIC_NODES_EXPANDED, 0);
		iterationMetrics.set(PATH_COST, 0);
		// for depth = 0 to infinity do
		for (int i = 0; i <= infinity; i++) {
			// result <- DEPTH-LIMITED-SEARCH(problem, depth)
			DepthLimitedSearch dls = new DepthLimitedSearch(i);
			List<Action> result = dls.search(p);
			iterationMetrics.set(METRIC_NODES_EXPANDED, iterationMetrics
					.getInt(METRIC_NODES_EXPANDED)
					+ dls.getMetrics().getInt(METRIC_NODES_EXPANDED));
			// if result != cutoff then return result
			if (!dls.isCutOff(result)) {
				iterationMetrics.set(PATH_COST, dls.getPathCost());
				return result;
			}
		}
		return failure();
	}

	@Override
	public Metrics getMetrics() {
		return iterationMetrics;
	}

	//
	// PRIVATE METHODS
	//

	private List<Action> failure() {
		return Collections.emptyList();
	}
}