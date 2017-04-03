package aima.core.search.basic.uninformed;

import java.util.List;

import aima.core.search.api.DepthLimitedSearch;
import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function DEPTH-LIMITED-SEARCH(problem, limit) returns a solution, or failure/cutoff
 *   return RECURSIVE-DLS(MAKE-NODE(problem.INITIAL-STATE), problem, limit)
 *
 * function RECURSIVE-DLS(node, problem, limit) returns a solution, or failure/cutoff
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   else if limit = 0 then return cutoff
 *   else
 *       cutoff_occurred? &larr; false
 *       for each action in problem.ACTIONS(node.STATE) do
 *           child &larr; CHILD-NODE(problem, node, action)
 *           result &larr; RECURSIVE-DLS(child, problem, limit - 1)
 *           if result = cutoff then cutoff_occurred? &larr; true
 *           else if result != failure then return result
 *       if cutoff_occurred? then return cutoff else return failure
 * </pre>
 *
 * Figure ?? A recursive implementation of depth-limited tree search.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class DepthLimitedTreeSearch<A, S> implements SearchForActionsFunction<A, S>, DepthLimitedSearch<A, S> {
	// function DEPTH-LIMITED-SEARCH(problem, limit) returns a solution, or
	// failure/cutoff
	@Override
	public List<A> depthLimitedSearch(Problem<A, S> problem, int limit) {
		// return RECURSIVE-DLS(MAKE-NODE(problem.INITIAL-STATE), problem,
		// limit)
		return recursiveDLS(newRootNode(problem.initialState()), problem, limit);
	}

	// function RECURSIVE-DLS(node, problem, limit) returns a solution, or
	// failure/cutoff
	public List<A> recursiveDLS(Node<A, S> node, Problem<A, S> problem, int limit) {
		// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
		if (isGoalState(node, problem)) {
			return solution(node);
		} // else if limit = 0 then return cutoff
		else if (limit == 0) {
			return getCutoff();
		} // else
		else {
			// cutoff_occurred? <- false
			boolean cutoff_occurred = false;
			// for each action in problem.ACTIONS(node.STATE) do
			for (A action : problem.actions(node.state())) {
				// child <- CHILD-NODE(problem, node, action)
				Node<A, S> child = newChildNode(problem, node, action);
				// result <- RECURSIVE-DLS(child, problem, limit - 1)
				List<A> result = recursiveDLS(child, problem, limit - 1);
				// if result = cutoff then cutoff_occurred? <- true
				if (result == getCutoff()) {
					cutoff_occurred = true;
				} // else if result != failure then return result
				else if (result != failure()) {
					return result;
				}
			}
			// if cutoff_occurred? then return cutoff else return failure
			if (cutoff_occurred) {
				return getCutoff();
			} else {
				return failure();
			}
		}
	}
	
	//
	// Supporting Code
	private int limit;
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;

	public List<A> failure() {
		return searchController.failure();
	}

	public List<A> solution(Node<A, S> node) {
		return searchController.solution(node);
	}
	
	public boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
		return searchController.isGoalState(node, problem);
	}
	
	public Node<A, S> newRootNode(S initialState) {
		return nodeFactory.newRootNode(initialState);
	}

	public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
		return nodeFactory.newChildNode(problem, node, action);
	}
	
	public DepthLimitedTreeSearch(int limit) {
		this(limit, new BasicSearchController<>(), new BasicNodeFactory<>());
	}

	public DepthLimitedTreeSearch(int limit, SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory) {
		this.limit = limit;
		this.searchController = searchController;
		this.nodeFactory = nodeFactory;
	}

	@Override
	public List<A> apply(Problem<A, S> problem) {
		return depthLimitedSearch(problem, getLimit());
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public List<A> getCutoff() {
		return null;
	}
}
