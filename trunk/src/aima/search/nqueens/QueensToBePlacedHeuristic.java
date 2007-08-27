package aima.search.nqueens;

import aima.search.framework.HeuristicFunction;
import aima.search.framework.Node;

/**
 * @author Ravi Mohan
 * 
 */

public class QueensToBePlacedHeuristic implements HeuristicFunction {
	public int getHeuristicValue(Node n) {
		NQueensBoard board = (NQueensBoard) n.getState();
		return board.size - board.getNumberOfQueensOnBoard();
	}

}