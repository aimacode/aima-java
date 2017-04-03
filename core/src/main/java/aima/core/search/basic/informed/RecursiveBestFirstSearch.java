package aima.core.search.basic.informed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

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
 * function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or failure
 *   return RBFS(problem, MAKE-NODE(problem.INITIAL-STATE), infinity)
 *
 * function RBFS(problem, node, f_limit) returns a solution, or failure and a new f-cost limit
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   successors &larr; []
 *   for each action in problem.ACTION(node.STATE) do
 *       add CHILD-NODE(problem, node, action) into successors
 *   if successors is empty then return failure, infinity
 *   for each s in successors do // update f with value from previous search, if any
 *     s.f &larr; max(s.g + s.h, node.f)
 *   loop do
 *     best &larr; the lowest f-value node in successors
 *     if best.f &gt; f_limit then return failure, best.f
 *     alternative &larr; the second-lowest f-value among successors
 *     result, best.f &larr; RBFS(problem, best, min(f_limit, alternative))
 *     if result != failure then return result
 * </pre>
 *
 * Figure ?? The algorithm for recursive best-first search.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Mike Stampone
 * 
 */
public class RecursiveBestFirstSearch<A, S> implements SearchForActionsFunction<A, S> {
	// function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or
	// failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// return RBFS(problem, MAKE-NODE(problem.INITIAL-STATE), infinity)
		return rbfs(problem, newRootNode(problem.initialState(), 0),
				Double.POSITIVE_INFINITY).result();
	}

	// function RBFS(problem, node, f_limit) returns a solution, or failure and
	// a new f-cost limit
	public Result rbfs(Problem<A, S> problem, SuccessorNode node, double f_limit) {
		// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
		if (isGoalState(node, problem)) {
			return new Result(solution(node));
		}
		// successors <- []
		List<SuccessorNode> successors = new ArrayList<>();
		// for each action in problem.ACTION(node.STATE) do
		for (A action : problem.actions(node.state())) {
			// add CHILD-NODE(problem, node, action) into successors
			successors.add(newChildNode(problem, node, action));
		}
		// if successors is empty then return failure, infinity
		if (successors.isEmpty()) {
			return new Result(failure(), Double.POSITIVE_INFINITY);
		}
		// for each s in successors do // update f with value from previous
		// search, if any
		for (SuccessorNode s : successors) {
			// s.f <- max(s.g + s.h, node.f)
			s.f = Math.max(s.g + s.h, node.f);
		}
		// loop do
		do {
			// best <- the lowest f-value node in successors
			SuccessorNode best = getLowestFValueNode(successors);
			// if best.f > f_limit then return failure, best.f
			if (best.f > f_limit) {
				return new Result(failure(), best.f);
			}
			// alternative <- the second-lowest f-value among successors
			double alternative = getSecondLowestFValue(successors, best);
			// result, best.f <- RBFS(problem, best, min(f_limit, alternative))
			Result result = rbfs(problem, best, Math.min(f_limit, alternative));
			best.f = result.newFCostLimit;
			// if result != failure then return result
			if (!result.isFailure()) {
				return result;
			}
		} while (isExecuting());
		return new Result(failure(), Double.POSITIVE_INFINITY);
	}

	//
	// Supporting Code
	private ToDoubleFunction<Node<A, S>> h;
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;
	
	public RecursiveBestFirstSearch(ToDoubleFunction<Node<A, S>> h) {
		this(h, new BasicSearchController<>(), new BasicNodeFactory<>());
	}

	public RecursiveBestFirstSearch(ToDoubleFunction<Node<A, S>> h, SearchController<A, S> searchController,
			NodeFactory<A, S> nodeFactory) {
		this.h = h;
		this.searchController = searchController;
		this.nodeFactory = nodeFactory;
	}

	public double h(Node<A, S> node) {
		return h.applyAsDouble(node);
	}

	public List<A> failure() {
		return searchController.failure();
	}

	public List<A> solution(Node<A, S> node) {
		return searchController.solution(node);
	}
	
	public SuccessorNode newRootNode(S initialState, double pathCost) {
		return new SuccessorNode(nodeFactory.newRootNode(initialState), this::h);
	}

	public SuccessorNode newChildNode(Problem<A, S> problem, SuccessorNode node, A action) {
		return new SuccessorNode(nodeFactory.newChildNode(problem, node.n, action), this::h);
	}
	
	public SuccessorNode getLowestFValueNode(List<SuccessorNode> nodes) {
		return nodes.stream().min(Comparator.comparingDouble(s -> s.f)).get();
	}

	public double getSecondLowestFValue(List<SuccessorNode> nodes, SuccessorNode lowest) {		
		return nodes.stream().filter(x -> x != lowest).min(Comparator.comparing(s -> s.f)).orElse(lowest).f;
	}
	
	public boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
		return searchController.isGoalState(node, problem);
	}
	
	public boolean isExecuting() {
		return searchController.isExecuting();
	}
	
	class Result {
		List<A> result;
		boolean issolution;
		double newFCostLimit = 0;

		Result(List<A> solution) {
			this.result = solution;
			this.issolution = true;
		}

		Result(List<A> failure, double newFCostLimit) {
			this.result = failure;
			this.issolution = false;
			this.newFCostLimit = newFCostLimit;
		}

		boolean isSolution() {
			return issolution;
		}

		boolean isFailure() {
			return !isSolution();
		}

		List<A> result() {
			return result;
		}
	}

	class SuccessorNode implements Node<A, S>{
		Node<A, S> n;
		double g;
		double h;
		double f;

		SuccessorNode(Node<A, S> node, Function<Node<A, S>, Double> h) {
			this.n = node;
			this.g = node.pathCost();
			this.h = h.apply(node);
			this.f = g + this.h;
		}

		@Override
		public S state() {
			return n.state();
		}

		@Override
		public Node<A, S> parent() {
			return n.parent();
		}

		@Override
		public A action() {
			return n.action();
		}

		@Override
		public double pathCost() {
			return n.pathCost();
		}
	}
}
