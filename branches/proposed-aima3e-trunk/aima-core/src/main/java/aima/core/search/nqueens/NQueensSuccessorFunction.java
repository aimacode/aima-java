package aima.core.search.nqueens;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.framework.Successor;
import aima.core.search.framework.SuccessorFunction;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class NQueensSuccessorFunction implements SuccessorFunction {

	public List<Successor> getSuccessors(Object state) {
		List<Successor> successors = new ArrayList<Successor>();
		NQueensBoard board = (NQueensBoard) state;
		int numQueens = board.getNumberOfQueensOnBoard();
		int boardSize = board.getSize();
		for (int i = 0; i < boardSize; i++) {
			if (!(board.isSquareUnderAttack(new XYLocation(numQueens, i)))) {
				NQueensBoard child = placeQueenAt(numQueens, i, board);
				successors.add(new Successor(new PlaceQueenAction(numQueens, i), child));
			}
		}

		return successors;
	}

	private NQueensBoard placeQueenAt(int row, int column,
			NQueensBoard parentBoard) {

		NQueensBoard newBoard = new NQueensBoard(parentBoard.getSize());
		List<XYLocation> queenPositionsOnParentBoard = parentBoard
				.getQueenPositions();
		queenPositionsOnParentBoard.add(new XYLocation(row, column));
		newBoard.setBoard(queenPositionsOnParentBoard);
		return newBoard;
	}

}