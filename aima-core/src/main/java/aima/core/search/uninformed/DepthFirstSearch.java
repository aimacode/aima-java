package aima.core.search.uninformed;

import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.Search;
import aima.core.util.datastructure.LIFOQueue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 85.<br>
 * <br>
 * Depth-first search always expands the deepest node in the current frontier of
 * the search tree. <br>
 * <br>
 * <b>Note:</b> Supports both Tree and Graph based versions by assigning an
 * instance of TreeSearch or GraphSearch to its constructor.
 * 
 * @author Ravi Mohan
 * 
 */
public class DepthFirstSearch implements Search {

	QueueSearch search;

	public DepthFirstSearch(QueueSearch search) {
		this.search = search;
	}

	public List<Action> search(Problem p) {
		return search.search(p, new LIFOQueue<Node>());
	}

	public Metrics getMetrics() {
		return search.getMetrics();
	}
}