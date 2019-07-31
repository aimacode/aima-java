package aima.core.search.informed;

import aima.core.search.framework.Node;
import aima.core.search.framework.qsearch.QueueSearch;

import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Greedy best-first search tries to expand the node that is closest to the
 * goal, on the grounds that this is likely to lead to a solution quickly. Thus,
 * it evaluates nodes by using just the heuristic function; that is, f(n) = h(n)
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class GreedyBestFirstSearch<S, A> extends BestFirstSearch<S, A> {

	/**
	 * Constructs a greedy best-first search from a specified search execution strategy
	 * and a heuristic function.
	 * 
	 * @param impl
	 *            A search execution strategy (e.g. TreeSearch, GraphSearch).
	 * @param h
	 *            A heuristic function <em>h(n)</em>, which estimates the
	 *            cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public GreedyBestFirstSearch(QueueSearch<S, A> impl, ToDoubleFunction<Node<S, A>> h) {
		super(impl, createEvalFn(h));
	}

	// f(n) = h(n)
	public static <S, A> EvaluationFunction<S, A> createEvalFn(ToDoubleFunction<Node<S, A>> h) {
		return new EvaluationFunction<S, A>(h) {
			@Override
			public double applyAsDouble(Node<S, A> node) {
				return this.h.applyAsDouble(node);
			}
		};
	}
}