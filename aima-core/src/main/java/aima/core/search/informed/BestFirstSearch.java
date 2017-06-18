package aima.core.search.informed;

import aima.core.search.framework.Node;
import aima.core.search.framework.QueueBasedSearch;
import aima.core.search.framework.QueueFactory;
import aima.core.search.framework.qsearch.QueueSearch;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Best-first search is an instance of the general TREE-SEARCH or GRAPH-SEARCH
 * algorithm in which a node is selected for expansion based on an evaluation
 * function, f(n). The evaluation function is construed as a cost estimate, so
 * the node with the lowest evaluation is expanded first. The implementation of
 * best-first graph search is identical to that for uniform-cost search (Figure
 * 3.14), except for the use of f instead of g to order the priority queue.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class BestFirstSearch<S, A> extends QueueBasedSearch<S, A> implements Informed<S, A> {

	private final ToDoubleFunction<Node<S, A>> evalFn;
	
	/**
	 * Constructs a best first search from a specified search problem and
	 * evaluation function.
	 * 
	 * @param impl
	 *            a search space exploration strategy.
	 * @param evalFn
	 *            an evaluation function, which returns a number purporting to
	 *            describe the desirability (or lack thereof) of expanding a
	 *            node.
	 */
	public BestFirstSearch(QueueSearch<S, A> impl, final ToDoubleFunction<Node<S, A>> evalFn) {
		super(impl, QueueFactory.createPriorityQueue(Comparator.comparing(evalFn::applyAsDouble)));
		this.evalFn = evalFn;
	}

	/** Modifies the evaluation function if it is a {@link HeuristicEvaluationFunction}. */
	@Override
	public void setHeuristicFunction(ToDoubleFunction<Node<S, A>> h) {
		if (evalFn instanceof HeuristicEvaluationFunction)
			((HeuristicEvaluationFunction<S, A>) evalFn).setHeuristicFunction(h);
	}
}
