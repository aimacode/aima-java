package aima.core.search.basic.local;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;
import aima.core.util.ExecutionController;

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
		Node current = makeNode(problem.initialState());
		// loop do
		while (loopDo()) {
			// neighbor <- a highest-valued successor of current
			Node neighbor = highestValuedSuccessor(current, problem);
			// if neighbor.VALUE <= current.VALUE then return current.STATE
			if (neighbor.value <= current.value) {
				return current.state;
			}
			// current <- neighbor
			current = neighbor;
		}
		return current.state;
	}

	//
	// Supporting Code
	public class Node {
		S state;
		double value;

		Node(S state, double value) {
			this.state = state;
			this.value = value;
		}

		@Override
		public String toString() {
			return "N(" + state.toString() + ", " + value + ")";
		}
	}

	protected ToDoubleFunction<S> stateValueFn;
	protected ExecutionController executionController;

	public HillClimbingSearch(ToDoubleFunction<S> stateValueFn) {
		this(stateValueFn, new ExecutionController() {
		});
	}

	public HillClimbingSearch(ToDoubleFunction<S> stateValueFn, ExecutionController executionController) {
		this.stateValueFn = stateValueFn;
		this.executionController = executionController;
	}

	public Node makeNode(S state) {
		return new Node(state, stateValueFn.applyAsDouble(state));
	}

	public boolean loopDo() {
		return executionController.isExecuting();
	}

	public Node highestValuedSuccessor(Node current, Problem<A, S> problem) {
		Node highestValueSuccessor = null;
		for (A action : problem.actions(current.state)) {
			Node successor = makeNode(problem.result(current.state, action));
			if (highestValueSuccessor == null || successor.value > highestValueSuccessor.value) {
				highestValueSuccessor = successor;
			}
		}
		// If no successor then just be our own neighbor
		if (highestValueSuccessor == null) {
			highestValueSuccessor = current;
		}
		return highestValueSuccessor;
	}
}