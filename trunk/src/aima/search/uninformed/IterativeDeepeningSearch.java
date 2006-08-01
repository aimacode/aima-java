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
 * @author Ravi Mohan
 *  
 */
public class IterativeDeepeningSearch extends NodeExpander implements Search {

	private int limit;

	private Metrics iterationMetrics;

	public IterativeDeepeningSearch() {
		this.limit = Integer.MAX_VALUE;
		iterationMetrics = new Metrics();
		iterationMetrics.set(NODES_EXPANDED, 0);
	}

	public List search(Problem p) throws Exception {
		for (int i = 1; i <= limit; i++) {
			DepthLimitedSearch dls = new DepthLimitedSearch(i);
			List result = dls.search(p);
			iterationMetrics.set(NODES_EXPANDED, iterationMetrics
					.getInt(NODES_EXPANDED)
					+ dls.getMetrics().getInt(NODES_EXPANDED));
			if (!cutOffResult(result)) {
				return result;
			}
		}
		return new ArrayList();//failure
	}

	private boolean cutOffResult(List result) { //TODO remove this duplication

		return result.size() == 1 && result.get(0).equals("cutoff");
	}

	public Metrics getMetrics() {
		return iterationMetrics;
	}

}