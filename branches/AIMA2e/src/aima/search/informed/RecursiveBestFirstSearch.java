package aima.search.informed;

import java.util.ArrayList;
import java.util.List;

import aima.search.framework.EvaluationFunction;
import aima.search.framework.Node;
import aima.search.framework.NodeExpander;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchUtils;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 4.5, page 102.
 * 
 * <code>
 * function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or failure
 *   RBFS(problem, MAKE-NODE(INITIAL-STATE[problem]), infinity)
 *   
 * function RBFS(problem, node, f_limit) returns a solution, or failure and a new f-cost limit
 *   if GOAL-TEST[problem](STATE[node]) then return node
 *   successors <- EXPAND(node, problem)
 *   if successors is empty then return failure, infinity
 *   for each s in successors do
 *     f[s] <- max(g(s) + h(s), f[node])
 *   repeat
 *     best <- the lowest f-value node in successors
 *     if f[best] > f_limit then return failure, f[best]
 *     alternative <- the second-lowest f-value among successors
 *     result, f[best] <- RBFS(problem, best, min(f_limit, alternative))
 *     if result <> failure then return result
 * </code>
 * Figure 4.5 The algorithm for recursive best-first search.
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
	public List<String> search(Problem p) throws Exception {
		List<String> actions = new ArrayList<String>();

		clearInstrumentation();

		// RBFS(problem, MAKE-NODE(INITIAL-STATE[problem]), infinity)
		Node n = new Node(p.getInitialState());
		SearchResult sr = rbfs(p, n, evaluationFunction.getValue(p, n),
				INFINITY, 0);
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
	private SearchResult rbfs(Problem p, Node n, Double fNode, Double fLimit,
			int recursiveDepth) {

		setMaxRecursiveDepth(recursiveDepth);

		// if GOAL-TEST[problem](STATE[node]) then return node
		if (p.isGoalState(n.getState())) {
			return new SearchResult(n, fLimit);
		}

		// successors <- EXPAND(node, problem)
		List<Node> successors = expandNode(n, p);
		// if successors is empty then return failure, infinity
		if (0 == successors.size()) {
			return new SearchResult(null, INFINITY);
		}
		double[] f = new double[successors.size()];
		// for each s in successors do
		int size = successors.size();
		for (int s = 0; s < size; s++) {
			// f[s] <- max(g(s) + h(s), f[node])
			f[s] = Math.max(evaluationFunction.getValue(p, successors.get(s)),
					fNode);
		}

		// repeat
		while (true) {
			// best <- the lowest f-value node in successors
			int bestIndex = getBestFValueIndex(f);
			// if f[best] > f_limit then return failure, f[best]
			if (f[bestIndex] > fLimit) {
				return new SearchResult(null, f[bestIndex]);
			}
			// alternative <- the second-lowest f-value among successors
			int altIndex = getNextBestFValueIndex(f, bestIndex);
			// result, f[best] <- RBFS(problem, best, min(f_limit, alternative))
			SearchResult sr = rbfs(p, successors.get(bestIndex), f[bestIndex],
					Math.min(fLimit, f[altIndex]), recursiveDepth + 1);
			f[bestIndex] = sr.getFCostLimit();
			// if result <> failure then return result
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