/*
 * Created on Sep 8, 2004
 *
 */
package aima.search.uninformed;

import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Node;
import aima.search.framework.NodeExpander;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchUtils;

/**
 * @author Ravi Mohan
 *  
 */
public class DepthLimitedSearch extends NodeExpander implements Search {

	private int limit;

	public DepthLimitedSearch(int limit) {
		this.limit = limit;

	}

	public List search(Problem p) throws Exception {
		clearInstrumentation();
		return recursiveDLS(new Node(p.getInitialState()), p, limit);
	}

	private List recursiveDLS(Node node, Problem problem, int limit) {
		boolean cutOffOccured = false;
		if (problem.isGoalState(node.getState())) {
			return SearchUtils.actionsFromNodes(node.getPathFromRoot());
		} else if (node.getDepth() == limit) {
			return createCutOffResult();
		} else {
			List children = expandNode(node, problem);
			for (int i = 0; i < children.size(); i++) {
				Node child = (Node) children.get(i);
				List result = recursiveDLS(child, problem, limit);
				if (cutoffResult(result)) {
					cutOffOccured = true;
				} else if (!(failure(result))) {
					return result;
				}
			}
			if (cutOffOccured) {
				return createCutOffResult();
			} else {
				return new ArrayList();
			}

		}

	}

	private boolean failure(List result) {

		return result.size() == 0;
	}

	private boolean cutoffResult(List result) {

		return result.size() == 1 && result.get(0).equals("cutoff");
	}

	private List createCutOffResult() {
		List result = new ArrayList();
		result.add("cutoff");
		return result;
	}

}