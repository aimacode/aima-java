package aima.core.search.uninformed;

import java.util.Collections;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.18, page
 * 89.<br>
 * <br>
 * 
 * <pre>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result &lt;- DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </pre>
 * 
 * Figure 3.18 The iterative deepening search algorithm, which repeatedly
 * applies depth-limited search with increasing limits. It terminates when a
 * solution is found or if the depth- limited search returns failure, meaning
 * that no solution exists.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class IterativeDeepeningSearch implements Search {
	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
	public static final String METRIC_PATH_COST = "pathCost";

	// Not infinity, but will do, :-)
	private final static int INFINITY = Integer.MAX_VALUE;

	private final Metrics metrics = new Metrics();

	
	// function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or
	// failure
	public List<Action> search(Problem p) throws Exception {
		clearInstrumentation();
		// for depth = 0 to infinity do
		for (int i = 0; i <= INFINITY; i++) {
			// result <- DEPTH-LIMITED-SEARCH(problem, depth)
			DepthLimitedSearch dls = new DepthLimitedSearch(i);
			List<Action> result = dls.search(p);
			updateMetrics(dls.getMetrics());
			// if result != cutoff then return result
			if (!dls.isCutOff(result)) {
				metrics.set(METRIC_PATH_COST, dls.getPathCost());
				return result;
			}
		}
		return failure();
	}

	@Override
	public Metrics getMetrics() {
		return metrics;
	}
	
	/**
	 * Sets the nodes expanded and path cost metrics to zero.
	 */
	public void clearInstrumentation() {
		metrics.set(METRIC_NODES_EXPANDED, 0);
		metrics.set(METRIC_PATH_COST, 0);
	}

	//
	// PRIVATE METHODS
	//

	private void updateMetrics(Metrics dlsMetrics) {
		metrics.set(METRIC_NODES_EXPANDED,
				metrics.getInt(METRIC_NODES_EXPANDED)
						+ dlsMetrics.getInt(METRIC_NODES_EXPANDED));
	}
	
	private List<Action> failure() {
		return Collections.emptyList();
	}
}