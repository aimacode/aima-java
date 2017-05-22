package aima.core.search.informed;

import aima.core.search.framework.Node;
import aima.core.search.framework.qsearch.QueueSearch;

import java.util.function.Function;

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
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class AStarSearch extends BestFirstSearch {

    /**
     * Constructs an A* search from a specified search space exploration
     * strategy and a heuristic function.
     *
     * @param impl a search space exploration strategy (e.g. TreeSearch, GraphSearch).
     * @param hf   a heuristic function <em>h(n)</em>, which estimates the cost
     *             of the cheapest path from the state at node <em>n</em> to a
     *             goal state.
     */
    public AStarSearch(QueueSearch impl, Function<Object, Double> hf) {
        super(impl, new EvalFunction(hf));
    }


    public static class EvalFunction extends HeuristicEvaluationFunction {
        private Function<Node, Double> gf;

        public EvalFunction(Function<Object, Double> hf) {
            this.hf = hf;
            this.gf = Node::getPathCost;
        }

        /**
         * Returns <em>g(n)</em> the cost to reach the node, plus <em>h(n)</em> the
         * heuristic cost to get from the specified node to the goal.
         *
         * @param n a node
         * @return g(n) + h(n)
         */
        @Override
        public Double apply(Node n) {
            // f(n) = g(n) + h(n)
            return gf.apply(n) + hf.apply(n.getState());
        }
    }
}