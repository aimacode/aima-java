package aima.core.search.informed;

import aima.core.search.framework.Node;
import aima.core.search.framework.qsearch.QueueSearch;

import java.util.function.Function;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Greedy best-first search tries to expand the node that is closest to the
 * goal, on the grounds that this is likely to lead to a solution quickly. Thus,
 * it evaluates nodes by using just the heuristic function; that is, f(n) = h(n)
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class GreedyBestFirstSearch extends BestFirstSearch {

	/**
	 * Constructs a greedy best-first search from a specified search space
	 * exploration strategy and a heuristic function.
	 * 
	 * @param impl
	 *            a search space exploration strategy (e.g. TreeSearch,
	 *            GraphSearch).
	 * @param hf
	 *            a heuristic function <em>h(n)</em>, which estimates the
	 *            cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public GreedyBestFirstSearch(QueueSearch impl, Function<Object, Double> hf) {
		super(impl, new EvalFunction(hf));
	}


	public static class EvalFunction extends HeuristicEvaluationFunction {
		public EvalFunction(Function<Object, Double> hf) {
			this.hf = hf;
		}

		/**
		 * Returns the heuristic cost <em>h(n)</em> to get from the specified node to the goal.
		 *
		 * @param n a node
		 * @return h(n)
		 */
		@Override
		public Double apply(Node n) {
			// f(n) = h(n)
			return hf.apply(n.getState());
		}
	}
}