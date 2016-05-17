package aima.core.search.basic.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * Greedy best-first graph search tries to expand the node that is closest to the goal, on the grounds
 * that this is likely to lead to a solution quickly. Thus, it evaluates nodes by using just the heuristic
 * function; that is <em>f(n) = h(n)</em>.
 *
 * @author Ciaran O'Reilly
 */
public class GreedyBestFirstGraphSearch<A, S> extends BestFirstGraphSearch<A, S> {
	private ToDoubleFunction<Node<A, S>> h;

	public GreedyBestFirstGraphSearch(ToDoubleFunction<Node<A, S>> h) {
	    super(h);
	    this.h = h;
	}
	
	public ToDoubleFunction<Node<A, S>> getHeuristifcFunctionH() {
	  return h;
	}
}
