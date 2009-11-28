package aima.core.environment.nqueens;

import aima.core.search.framework.HeuristicFunction;

/**
 * @author Ravi Mohan
 * 
 */
public class QueensToBePlacedHeuristic implements HeuristicFunction {

	public double h(Object state) {
		NQueensBoard board = (NQueensBoard) state;
		return board.size - board.getNumberOfQueensOnBoard();
	}
}