package aima.core.api.search.local;

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
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public interface HillClimbingSearch<A, S> extends SearchFunction<A, S> {

    // function HILL-CLIMBING(problem) returns a state that is a local maximum
    @Override
    default List<A> apply(Problem<A, S> problem) {
        // current <- MAKE-NODE(problem.INITIAL-STATE)
        SuccessorNode<A, S> current = new SuccessorNode<>(newNode(problem.initialState()), this::h);
        SuccessorNode<A, S> neighbor;
        // loop do
        do {
            // neighbor <- a highest-valued successor of current
            List<SuccessorNode<A, S>> successors = new ArrayList<>();
            for (A action : problem.actions(current.n.state())) {
                successors.add(new SuccessorNode<>(childNode(problem, current.n, action), this::h));
            }
            if (successors.isEmpty()) {
                return solution(current.n);
            } else {
                Collections.sort(successors, (s1, s2) -> Double.compare(s2.value, s1.value));
                neighbor = successors.get(0);
            }
            // if neighbor.VALUE <= current.VALUE then return current.STATE
            if (neighbor.value <= current.value) {
            	return solution(current.n);
            }
            current = neighbor;
        } while (true);
    }

    double h(Node<A, S> node);

    class SuccessorNode<A, S> {
        Node<A, S> n;
        double value;
        double h;

        SuccessorNode(Node<A, S> node, Function<Node<A, S>, Double> h) {
            this.n = node;
            this.h = h.apply(node);
            this.value = -1 * this.h;
        }
    }
}