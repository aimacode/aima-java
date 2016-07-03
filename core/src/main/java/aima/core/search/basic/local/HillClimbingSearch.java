package aima.core.search.basic.local;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
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
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Paul Anton
 * @author Mike Stampone
 */
public class HillClimbingSearch<A, S> implements SearchForStateFunction<A, S> {
	// function HILL-CLIMBING(problem) returns a state that is a local maximum
	@Override
	public S apply(Problem<A, S> problem) {
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node<S> current = makeNode(problem.initialState());
		// loop do
		while (true) {
			// neighbor <- a highest-valued successor of current
			Node<S> neighbor = highestValuedSuccessor(current, problem);
			// if neighbor.VALUE <= current.VALUE then return current.STATE
			if (neighbor.value <= current.value) {
				return current.state;
			}
			// current <- neighbor
			current = neighbor;
		}
	}

	//
	// Supporting Code

	/**
	 * The algorithm does not maintain a search tree, so the data structure for
	 * the current node need only record the state and value of the
	 * objective/cost function.
	 * 
	 * @author oreilly
	 *
	 * @param <S>
	 *            the type of the state space
	 */
	public static class Node<S> {
		S state;
		double value;

		Node(S state, double value) {
			this.state = state;
			this.value = value;
		}

		@Override
		public String toString() {
			return "N(" + state + ", " + value + ")";
		}
	}

	/*
	 * Represents an objective (higher better) or cost/heuristic (lower better)
	 * function. If a cost/heuristic function is passed in the
	 * 'isSteepestAscentVersion' should be set to false for the algorithm to
	 * search for minimums.
	 */
	protected ToDoubleFunction<S> stateValueFn;

	public HillClimbingSearch(ToDoubleFunction<S> stateValueFn) {
		this(stateValueFn, true);
	}

	public HillClimbingSearch(ToDoubleFunction<S> stateValueFn, boolean isSteepestAscentVersion) {
		this.stateValueFn = stateValueFn;
		if (!isSteepestAscentVersion) {
			// Convert from one to the other by switching the sign
			this.stateValueFn = (state) -> stateValueFn.applyAsDouble(state) * -1;
		}
	}

	public Node<S> makeNode(S state) {
		return new Node<>(state, stateValueFn.applyAsDouble(state));
	}

	public Node<S> highestValuedSuccessor(Node<S> current, Problem<A, S> problem) {
		Node<S> highestValueSuccessor = null;
		for (A action : problem.actions(current.state)) {
			Node<S> successor = makeNode(problem.result(current.state, action));
			if (highestValueSuccessor == null || successor.value > highestValueSuccessor.value) {
				highestValueSuccessor = successor;
			}
		}
		// If no successor then just be our own neighbor
		// as the calling code will just return 'current' as
		// the <= test will be true
		if (highestValueSuccessor == null) {
			highestValueSuccessor = current;
		}
		return highestValueSuccessor;
	}
}