package aima.core.api.search.informed.tree;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;
import aima.core.api.search.SearchFunction;

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
public interface RecursiveBestFirstTreeSearch<S> extends SearchFunction<S> {
    // function RECURSIVE-BEST-FIRST-SEARCH(problem) returns a solution, or failure
    @Override
    default List<Action> apply(Problem<S> problem) {
        // return RBFS(problem, MAKE-NODE(problem.INITIAL-STATE), infinity)
        return rbfs(problem, new SuccessorNode<>(newNode(problem.initialState()), this::h), Double.POSITIVE_INFINITY).result();
    }

    // function RBFS(problem, node, f_limit) returns a solution, or failure and a new f-cost limit
    default Result rbfs(Problem<S> problem, SuccessorNode<S> node, double f_limit) {
        // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
        if (isGoalState(node.n, problem)) { return new Result(solution(node.n)); }
        // successors <- []
        List<SuccessorNode<S>>  successors = new ArrayList<>();
        // for each action in problem.ACTION(node.STATE) do
        for (Action action: problem.actions(node.n.state())) {
            // add CHILD-NODE(problem, node, action) into successors
            successors.add(new SuccessorNode<>(childNode(problem, node.n, action), this::h));
        }
        // if successors is empty then return failure, infinity
        if (successors.isEmpty()) { return new Result(failure(), Double.POSITIVE_INFINITY); }
        // for each s in successors do // update f with value from previous search, if any
        for (SuccessorNode s : successors) {
            // s.f <- max(s.g + s.h, node.f)
            s.f = Math.max(s.g + s.h, node.f);
        }
        // loop do
        do {
            // best <- the lowest f-value node in successors
            Collections.sort(successors, (s1, s2) -> Double.compare(s1.f, s2.f));
            SuccessorNode<S> best = successors.get(0);
            // if best.f > f_limit then return failure, best.f
            if (best.f > f_limit) { return new Result(failure(), best.f); }
            // alternative <- the second-lowest f-value among successors
            double alternative = successors.size() > 1 ? successors.get(1).f : best.f;
            // result, best.f <- RBFS(problem, best, min(f_limit, alternative))
            Result result = rbfs(problem, best, Math.min(f_limit, alternative));
            best.f        = result.newFCostLimit;
            // if result != failure then return result
            if (!result.isFailure()) { return result; }
        } while (true);
    }

    double h(Node<S> node);

    class Result {
        List<Action> result;
        boolean issolution;
        double newFCostLimit = 0;

        Result(List<Action> solution) {
            this.result     = solution;
            this.issolution = true;
        }

        Result(List<Action> failure, double newFCostLimit) {
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

        List<Action> result() {
            return result;
        }
    }

    class SuccessorNode<S> {
        Node<S> n;
        double g;
        double h;
        double f;
        SuccessorNode(Node<S> node, Function<Node<S>, Double> h) {
            this.n = node;
            this.g = node.pathCost();
            this.h = h.apply(node);
            this.f = g + this.h;
        }
    }
}
