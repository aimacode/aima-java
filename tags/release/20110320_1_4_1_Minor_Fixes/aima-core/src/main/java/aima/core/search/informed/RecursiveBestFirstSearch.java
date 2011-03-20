package aima.core.search.informed;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchUtils;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.26, page 99.
 * 
 * <code>
 * function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or failure
 *   return RBFS(problem, MAKE-NODE(problem.INITIAL-STATE), infinity)
 *   
 * function RBFS(problem, node, f_limit) returns a solution, or failure and a new f-cost limit
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   successors <- []
 *   for each action in problem.ACTION(node.STATE) do
 *       add CHILD-NODE(problem, node, action) into successors
 *   if successors is empty then return failure, infinity
 *   for each s in successors do // update f with value from previous search, if any
 *     s.f <- max(s.g + s.h, node.f)
 *   repeat
 *     best <- the lowest f-value node in successors
 *     if best.f > f_limit then return failure, best.f
 *     alternative <- the second-lowest f-value among successors
 *     result, best.f <- RBFS(problem, best, min(f_limit, alternative))
 *     if result != failure then return result
 * </code>
 * Figure 3.26 The algorithm for recursive best-first search.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class RecursiveBestFirstSearch extends NodeExpander implements Search {

	private final EvaluationFunction evaluationFunction;

	private static final String MAX_RECURSIVE_DEPTH = "maxRecursiveDepth";

	private static final String PATH_COST = "pathCost";

	private static final Double INFINITY = Double.MAX_VALUE;

	public RecursiveBestFirstSearch(EvaluationFunction ef) {
		evaluationFunction = ef;
	}

	// function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or
	// failure
	public List<Action> search(Problem p) throws Exception {
		List<Action> actions = new ArrayList<Action>();

		clearInstrumentation();

		// RBFS(problem, MAKE-NODE(INITIAL-STATE[problem]), infinity)
		Node n = new Node(p.getInitialState());
		SearchResult sr = rbfs(p, n, evaluationFunction.f(n), INFINITY, 0);
		if (sr.getOutcome() == SearchResult.SearchOutcome.SOLUTION_FOUND) {
			Node s = sr.getSolution();
			actions = SearchUtils.actionsFromNodes(s.getPathFromRoot());
			setPathCost(s.getPathCost());
		}

		// Empty List can indicate already at Goal
		// or unable to find valid set of actions
		return actions;
	}

	@Override
	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(MAX_RECURSIVE_DEPTH, 0);
		metrics.set(PATH_COST, 0.0);
	}

	public void setMaxRecursiveDepth(int recursiveDepth) {
		int maxRdepth = metrics.getInt(MAX_RECURSIVE_DEPTH);
		if (recursiveDepth > maxRdepth) {
			metrics.set(MAX_RECURSIVE_DEPTH, recursiveDepth);
		}
	}

	public int getMaxRecursiveDepth() {
		return metrics.getInt(MAX_RECURSIVE_DEPTH);
	}

	public double getPathCost() {
		return metrics.getDouble(PATH_COST);
	}

	public void setPathCost(Double pathCost) {
		metrics.set(PATH_COST, pathCost);
	}

	//
	// PRIVATE METHODS
	// 
	// function RBFS(problem, node, f_limit) returns a solution, or failure and
	// a new f-cost limit
	private SearchResult rbfs(Problem p, Node n, double node_f, double fLimit,
			int recursiveDepth) {

		setMaxRecursiveDepth(recursiveDepth);

		// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
		if (SearchUtils.isGoalState(p, n)) {
			return new SearchResult(n, fLimit);
		}

		// successors <- []
		// for each action in problem.ACTION(node.STATE) do
		// add CHILD-NODE(problem, node, action) into successors
		List<Node> successors = expandNode(n, p);
		// if successors is empty then return failure, infinity
		if (0 == successors.size()) {
			return new SearchResult(null, INFINITY);
		}
		double[] f = new double[successors.size()];
		// for each s in successors do
		// update f with value from previous search, if any
		int size = successors.size();
		for (int s = 0; s < size; s++) {
			// s.f <- max(s.g + s.h, node.f)
			f[s] = Math.max(evaluationFunction.f(successors.get(s)), node_f);
		}

		// repeat
		while (true) {
			// best <- the lowest f-value node in successors
			int bestIndex = getBestFValueIndex(f);
			// if best.f > f_limit then return failure, best.f
			if (f[bestIndex] > fLimit) {
				return new SearchResult(null, f[bestIndex]);
			}
			// if best.f > f_limit then return failure, best.f
			int altIndex = getNextBestFValueIndex(f, bestIndex);
			// result, best.f <- RBFS(problem, best, min(f_limit, alternative))
			SearchResult sr = rbfs(p, successors.get(bestIndex), f[bestIndex],
					Math.min(fLimit, f[altIndex]), recursiveDepth + 1);
			f[bestIndex] = sr.getFCostLimit();
			// if result != failure then return result
			if (sr.getOutcome() == SearchResult.SearchOutcome.SOLUTION_FOUND) {
				return sr;
			}
		}
	}

	// the lowest f-value node
	private int getBestFValueIndex(double[] f) {
		int lidx = 0;
		Double lowestSoFar = INFINITY;

		for (int i = 0; i < f.length; i++) {
			if (f[i] < lowestSoFar) {
				lowestSoFar = f[i];
				lidx = i;
			}
		}

		return lidx;
	}

	// the second-lowest f-value
	private int getNextBestFValueIndex(double[] f, int bestIndex) {
		// Array may only contain 1 item (i.e. no alternative),
		// therefore default to bestIndex initially
		int lidx = bestIndex;
		Double lowestSoFar = INFINITY;

		for (int i = 0; i < f.length; i++) {
			if (i != bestIndex && f[i] < lowestSoFar) {
				lowestSoFar = f[i];
				lidx = i;
			}
		}

		return lidx;
	}
}

class SearchResult {
	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	};

	private Node solution;

	private SearchOutcome outcome;

	private final Double fCostLimit;

	public SearchResult(Node solution, Double fCostLimit) {
		if (null == solution) {
			this.outcome = SearchOutcome.FAILURE;
		} else {
			this.outcome = SearchOutcome.SOLUTION_FOUND;
			this.solution = solution;
		}
		this.fCostLimit = fCostLimit;
	}

	public SearchOutcome getOutcome() {
		return outcome;
	}

	public Node getSolution() {
		return solution;
	}

	public Double getFCostLimit() {
		return fCostLimit;
	}
}