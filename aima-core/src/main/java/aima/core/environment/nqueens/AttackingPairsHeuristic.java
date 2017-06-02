package aima.core.environment.nqueens;

import aima.core.search.framework.Node;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Estimates the distance to goal by the number of attacking pairs of queens on
 * the board.
 * 
 * @author Ruediger Lunde
 */
public class AttackingPairsHeuristic implements ToDoubleFunction<Node<NQueensBoard, QueenAction>> {

	public double applyAsDouble(Node<NQueensBoard, QueenAction> node) {
		NQueensBoard board = node.getState();
		return (double) board.getNumberOfAttackingPairs();
	}
}
