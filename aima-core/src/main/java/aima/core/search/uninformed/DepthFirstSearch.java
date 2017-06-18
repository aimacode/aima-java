package aima.core.search.uninformed;

import java.util.List;
import java.util.function.Consumer;

import aima.core.agent.Action;
import aima.core.search.framework.*;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 85.<br>
 * <br>
 * Depth-first search always expands the deepest node in the current frontier of
 * the search tree. <br>
 * <br>
 * <b>Note:</b> Supports TreeSearch, GraphSearch, and BidirectionalSearch. Just
 * provide an instance of the desired QueueSearch implementation to the
 * constructor!
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * 
 */
public class DepthFirstSearch<S, A> extends QueueBasedSearch<S, A> {

	public DepthFirstSearch(QueueSearch<S, A> impl) {
		super(impl, QueueFactory.createLifoQueue());
	}
}