package aima.core.search.informed;

import aima.core.search.framework.Node;
import aima.core.search.framework.qsearch.QueueSearch;

import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 93.<br>
 * <br>
 * The most widely known form of best-first search is called A* Search
 * (pronounced "A-star search"). It evaluates nodes by combining g(n), the cost
 * to reach the node, and h(n), the cost to get from the node to the goal:<br>
 * f(n) = g(n) + h(n).<br>
 * <br>
 * Since g(n) gives the path cost from the start node to node n, and h(n) is the
 * estimated cost of the cheapest path from n to the goal, we have<br>
 * f(n) = estimated cost of the cheapest solution through n.
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class AStarSearch<S, A> extends BestFirstSearch<S, A> {

    /**
     * Constructs an A* search from a specified search execution
     * strategy and a heuristic function.
     *
     * @param impl A search execution strategy (e.g. TreeSearch, GraphSearch).
     * @param h    A heuristic function <em>h(n)</em>, which estimates the cost
     *             of the cheapest path from the state of node <em>n</em> to a
     *             goal state.
     */
    public AStarSearch(QueueSearch<S, A> impl, ToDoubleFunction<Node<S, A>> h) {
        super(impl, createEvalFn(h));
    }

    // f(n) = g(n) + h(n)
    public static <S, A> EvaluationFunction<S, A> createEvalFn(ToDoubleFunction<Node<S, A>> h) {
        return new EvaluationFunction<S, A>(h) {
            @Override
            public double applyAsDouble(Node<S, A> node) {
                return node.getPathCost() + this.h.applyAsDouble(node);
            }
        };
    }
}