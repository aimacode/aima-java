package aima.core.api.search.informed.tree;

import aima.core.api.search.SearchFunction;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or failure
 *   return RBFS(problem, MAKE-NODE(problem.INITIAL-STATE), infinity)
 *
 * function RBFS(problem, node, f_limit) returns a solution, or failure and a new f-cost limit
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   successors &lt;- []
 *   for each action in problem.ACTION(node.STATE) do
 *       add CHILD-NODE(problem, node, action) into successors
 *   if successors is empty then return failure, infinity
 *   for each s in successors do // update f with value from previous search, if any
 *     s.f &lt;- max(s.g + s.h, node.f)
 *   loop do
 *     best &lt;- the lowest f-value node in successors
 *     if best.f &gt; f_limit then return failure, best.f
 *     alternative &lt;- the second-lowest f-value among successors
 *     result, best.f &lt;- RBFS(problem, best, min(f_limit, alternative))
 *     if result != failure then return result
 * </pre>
 *
 * Figure ?? The algorithm for recursive best-first search.
 *
 * @author Ciaran O'Reilly
 */
public interface RecursiveBestFirstTreeSearch<A, S> extends SearchFunction<A, S> {
    // function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or failure
    @Override
    default List<A> apply(Problem<A, S> problem) {
        // return RBFS(problem, MAKE-NODE(problem.INITIAL-STATE), infinity)
        return rbfs(problem, new SuccessorNode<>(newNode(problem.initialState()), this::h), Double.POSITIVE_INFINITY).result();
    }

    // function RBFS(problem, node, f_limit) returns a solution, or failure and a new f-cost limit
    default Result<A> rbfs(Problem<A, S> problem, SuccessorNode<A, S> node, double f_limit) {
        // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
        if (isGoalState(node.n, problem)) { return new Result<>(solution(node.n)); }
        // successors <- []
        List<SuccessorNode<A, S>> successors = new ArrayList<>();
        // for each action in problem.ACTION(node.STATE) do
        for (A action: problem.actions(node.n.state())) {
            // add CHILD-NODE(problem, node, action) into successors
            successors.add(new SuccessorNode<>(childNode(problem, node.n, action), this::h));
        }
        // if successors is empty then return failure, infinity
        if (successors.isEmpty()) { return new Result<>(failure(), Double.POSITIVE_INFINITY); }
        // for each s in successors do // update f with value from previous search, if any
        for (SuccessorNode<A, S> s : successors) {
            // s.f <- max(s.g + s.h, node.f)
            s.f = Math.max(s.g + s.h, node.f);
        }
        // loop do
        do {
            // best <- the lowest f-value node in successors
            Collections.sort(successors, (s1, s2) -> Double.compare(s1.f, s2.f));
            SuccessorNode<A, S> best = successors.get(0);
            // if best.f > f_limit then return failure, best.f
            if (best.f > f_limit) { return new Result<>(failure(), best.f); }
            // alternative <- the second-lowest f-value among successors
            double alternative = successors.size() > 1 ? successors.get(1).f : best.f;
            // result, best.f <- RBFS(problem, best, min(f_limit, alternative))
            Result<A> result = rbfs(problem, best, Math.min(f_limit, alternative));
            best.f           = result.newFCostLimit;
            // if result != failure then return result
            if (!result.isFailure()) { return result; }
        } while (true);
    }

    double h(Node<A, S> node);

    class Result<A> {
        List<A> result;
        boolean issolution;
        double newFCostLimit = 0;

        Result(List<A> solution) {
            this.result     = solution;
            this.issolution = true;
        }

        Result(List<A> failure, double newFCostLimit) {
            this.result        = failure;
            this.issolution    = false;
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

    class SuccessorNode<A, S> {
        Node<A, S> n;
        double     g;
        double     h;
        double     f;
        SuccessorNode(Node<A, S> node, Function<Node<A, S>, Double> h) {
            this.n = node;
            this.g = node.pathCost();
            this.h = h.apply(node);
            this.f = g + this.h;
        }
    }
}
