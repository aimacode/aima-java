package aima.extra.search.local;

import java.util.Random;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;
import aima.core.util.ExecutionController;

/**
 * An alternative implementation of hill-climbing search that allows for
 * sideways moves and whose search can be interrupted by an ExecutionController
 * (e.g. for use in a GUI).
 *
 * @author Ciaran O'Reilly
 */
public class HillClimbingSearchWithSidewaysMoves<A, S> implements SearchForStateFunction<A, S> {
	// function HILL-CLIMBING(problem) returns a state that is a local maximum
	@Override
	public S apply(Problem<A, S> problem) {
		Node<S> current = makeNode(problem.initialState());
		int consecutiveSidewayMovesTaken = 0;
		while (loopDo()) {
			Node<S> neighbor = highestValuedSuccessor(current, problem);
			// There are no successor nodes in this state space 
			// (i.e. a dead end state with irreversible actions).
			if (neighbor == null) {
				return current.state;
			}
			if (neighbor.value < current.value) {
				return current.state;
			} else if (neighbor.value == current.value) {
				if (consecutiveSidewayMovesTaken < allowedConsecutiveSidewayMoves) {
					consecutiveSidewayMovesTaken++;
					current = neighbor;
				} else {
					return current.state;
				}
			} else {
				current = neighbor;
				consecutiveSidewayMovesTaken = 0;
			}
		}
		return current.state;
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
	protected int allowedConsecutiveSidewayMoves;
	protected ExecutionController executionController;
	protected Random random = new Random();

	public HillClimbingSearchWithSidewaysMoves(ToDoubleFunction<S> stateValueFn) {
		this(stateValueFn, true);
	}

	public HillClimbingSearchWithSidewaysMoves(ToDoubleFunction<S> stateValueFn, boolean isSteepestAscentVersion) {
		this(stateValueFn, isSteepestAscentVersion, 100);
	}

	public HillClimbingSearchWithSidewaysMoves(ToDoubleFunction<S> stateValueFn, boolean isSteepestAscentVersion,
			int allowedConsecutiveSidewayMoves) {
		this(stateValueFn, isSteepestAscentVersion, allowedConsecutiveSidewayMoves, null);
	}

	public HillClimbingSearchWithSidewaysMoves(ToDoubleFunction<S> stateValueFn, boolean isSteepestAscentVersion,
			int allowedConsecutiveSidewayMoves, ExecutionController executionController) {
		this.stateValueFn = stateValueFn;
		if (!isSteepestAscentVersion) {
			// Convert from one to the other by switching the sign
			this.stateValueFn = (state) -> stateValueFn.applyAsDouble(state) * -1;
		}
		this.allowedConsecutiveSidewayMoves = allowedConsecutiveSidewayMoves;
		this.executionController = executionController;
	}

	public Node<S> makeNode(S state) {
		return new Node<>(state, stateValueFn.applyAsDouble(state));
	}

	public boolean loopDo() {
		return executionController == null || executionController.isExecuting();
	}

	public Node<S> highestValuedSuccessor(Node<S> current, Problem<A, S> problem) {
		Node<S> highestValueSuccessor = null;
		for (A action : problem.actions(current.state)) {
			Node<S> successor = makeNode(problem.result(current.state, action));
			if (highestValueSuccessor == null || successor.value > highestValueSuccessor.value) {
				highestValueSuccessor = successor;
			} else if (successor.value == highestValueSuccessor.value) {
				// With ties we need to randomly select a successor, otherwise
				// depending on order we could get stuck in an infinite loop
				if (random.nextBoolean()) {
					highestValueSuccessor = successor;
				}
			}
		}

		return highestValueSuccessor;
	}
}