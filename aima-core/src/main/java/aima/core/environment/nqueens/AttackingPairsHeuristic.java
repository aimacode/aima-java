package aima.core.environment.nqueens;

import java.util.function.Function;

/**
 * Estimates the distance to goal by the number of attacking pairs of queens on
 * the board.
 * 
 * @author Ruediger Lunde
 */
public class AttackingPairsHeuristic implements Function<Object, Double> {

	public Double apply(Object state) {
		NQueensBoard board = (NQueensBoard) state;
		return (double) board.getNumberOfAttackingPairs();
	}
}
