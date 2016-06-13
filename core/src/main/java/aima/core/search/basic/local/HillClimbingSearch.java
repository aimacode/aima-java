package aima.core.search.basic.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 * <p>
 * 
 * <pre>
 * function HILL-CLIMBING(problem) returns a state that is a local maximum
 *
 *   current &larr; MAKE-NODE(problem.INITIAL-STATE)
 *   loop do
 *     neighbor &larr; a highest-valued successor of current
 *     if neighbor.VALUE &le; current.VALUE then return current.STATE
 *     current &larr; neighbor
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
public class HillClimbingSearch<A, S> implements SearchForActionsFunction<A, S> {
	// function HILL-CLIMBING(problem) returns a state that is a local maximum
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		SuccessorNode current = new SuccessorNode(nodeFactory.newRootNode(problem.initialState()), this.h);
		SuccessorNode neighbor;
		// loop do
		do {
			// neighbor <- a highest-valued successor of current
			List<SuccessorNode> successors = new ArrayList<>();
			for (A action : problem.actions(current.n.state())) {
				successors.add(new SuccessorNode(nodeFactory.newChildNode(problem, current.n, action), this.h));
			}
			if (successors.isEmpty()) {
				return searchController.solution(current.n);
			} else {
				Collections.sort(successors, (s1, s2) -> Double.compare(s2.value, s1.value));
				neighbor = successors.get(0);
			}
			// if neighbor.VALUE <= current.VALUE then return current.STATE
			if (neighbor.value <= current.value) {
				return searchController.solution(current.n);
			}
			current = neighbor;
		} while (true);
	}

	//
	// Supporting Code
	private ToDoubleFunction<Node<A, S>> h;
	private SearchController<A, S> searchController;
	private NodeFactory<A, S> nodeFactory;

	public HillClimbingSearch(ToDoubleFunction<Node<A, S>> h) {
		this(h, new BasicSearchController<>(), new BasicNodeFactory<>());
	}

	public HillClimbingSearch(ToDoubleFunction<Node<A, S>> h, SearchController<A, S> searchController,
			NodeFactory<A, S> nodeFactory) {
		this.h = h;
		this.searchController = searchController;
		this.nodeFactory = nodeFactory;
	}

	public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
		return h;
	}

	class SuccessorNode {
		Node<A, S> n;
		double value;
		double h;

		SuccessorNode(Node<A, S> node, ToDoubleFunction<Node<A, S>> h) {
			this.n = node;
			this.h = h.applyAsDouble(node);
			this.value = -1 * this.h;
		}
	}
}
