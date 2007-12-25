package aima.search.nqueens;

import aima.search.framework.HeuristicFunction;

/**
 * @author Ravi Mohan
 * 
 */

public class QueensToBePlacedHeuristic implements HeuristicFunction {
	public double getHeuristicValue(Object state) {
		NQueensBoard board = (NQueensBoard) state;
		return board.size - board.getNumberOfQueensOnBoard();
	}

}