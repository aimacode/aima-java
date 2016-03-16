package aima.core.api.search.local;

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
 * <p>
 * <pre>
 * function HILL-CLIMBING(problem) returns a state that is a local maximum
 *
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   loop do
 *     neighbor &lt;- a highest-valued successor of current
 *     if neighbor.VALUE &lt;= current.VALUE then return current.STATE
 *     current &lt;- neighbor
 * </pre>
 * <p>
 * Figure ?? The hill-climbing search algorithm, which is the most basic local
 * search technique. At each step the current node is replaced by the best
 * neighbor; in this version, that means the neighbor with the highest VALUE,
 * but if a heuristic cost estimate h is used, we would find the neighbor with
 * the lowest h.
 *
 * @author Paul Anton
 */
public interface HillClimbingSearch<S> extends SearchFunction<S> {

    // function HILL-CLIMBING(problem) returns a state that is a local maximum
    @Override
    default List<Action> apply(Problem<S> problem) {
        // current <- MAKE-NODE(problem.INITIAL-STATE)
        SuccessorNode<S> current = new SuccessorNode<>(newNode(problem.initialState()), this::h);
        SuccessorNode<S> neighbor;
        // loop do
        do {
            // neighbor <- a highest-valued successor of current
            List<SuccessorNode<S>> successors = new ArrayList<>();
            for (Action action : problem.actions(current.n.state())) {
                successors.add(new SuccessorNode<>(childNode(problem, current.n, action), this::h));
            }
            if (successors.isEmpty()) {
                neighbor = null;
            } else {
                Collections.sort(successors, (s1, s2) -> Double.compare(s2.value, s1.value));
                neighbor = successors.get(0);
            }
            // if neighbor.VALUE <= current.VALUE then return current.STATE
            if (neighbor == null || neighbor.value <= current.value) {
                if (isGoalState(current.n, problem)) {
                    return solution(current.n);
                }
            }
            current = neighbor;
        } while (true);
    }

    double h(Node<S> node);

    class SuccessorNode<S> {
        Node<S> n;
        double value;
        double h;

        SuccessorNode(Node<S> node, Function<Node<S>, Double> h) {
            this.n = node;
            this.h = h.apply(node);
            this.value = -1 * this.h;
        }
    }
}
