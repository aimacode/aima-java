package aima.core.environment.nqueens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.core.util.datastructure.XYLocation;

/**
 * Represents a quadratic board with a matrix of squares on which queens can be
 * placed (only one per square) and moved.
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class NQueensBoard {

	/** Parameters for initialization. */
	public enum Config {
		EMPTY, QUEENS_IN_FIRST_ROW, QUEEN_IN_EVERY_COL
	}

	/**
	 * X (first index, col) increases left to right with zero based index,
	 * Y (second index, row) increases top to bottom with zero based index.
	 * A queen at position (x, y) is indicated by value true.
	 */
	private boolean[][] squares;

	/**
	 * Creates a board with <code>size</code> rows and size columns. Column and
	 * row indices start with 0.
	 */
	public NQueensBoard(int size) {
		squares = new boolean[size][size];
		for (int col = 0; col < size; col++) {
			for (int row = 0; row < size; row++) {
				squares[col][row] = false;
			}
		}
	}

	/**
	 * Creates a board with <code>size</code> rows and size columns. Column and
	 * row indices start with 0.
	 * 
	 * @param config
	 *            Controls whether the board is initially empty or contains some
	 *            queens.
	 */
	public NQueensBoard(int size, Config config) {
		this(size);
		if (config == Config.QUEENS_IN_FIRST_ROW) {
			for (int col = 0; col < size; col++)
				addQueenAt(new XYLocation(col, 0));
		} else if (config == Config.QUEEN_IN_EVERY_COL) {
			Random r = new Random();
			for (int col = 0; col < size; col++)
				addQueenAt(new XYLocation(col, r.nextInt(size)));
		}
	}

	public int getSize() {
		return squares.length;
	}

	public void clear() {
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				squares[col][row] = false;
			}
		}
	}

	public void setQueensAt(List<XYLocation> locations) {
		clear();
		locations.forEach(this::addQueenAt);
	}

	/** Column and row indices start with 0! */
	public void addQueenAt(XYLocation l) {
		squares[l.getX()][l.getY()] = true;
	}

	public void removeQueenFrom(XYLocation l) {
		squares[l.getX()][l.getY()] = false;
	}

	/**
	 * Moves the queen in the specified column (x-value of <code>l</code>) to
	 * the specified row (y-value of <code>l</code>). The action assumes a
	 * complete-state formulation of the n-queens problem.
	 */
	public void moveQueenTo(XYLocation l) {
		for (int row = 0; row < getSize(); row++)
			squares[l.getX()][row] = false;
		squares[l.getX()][l.getY()] = true;
	}

	public void moveQueen(XYLocation from, XYLocation to) {
		if ((queenExistsAt(from)) && (!(queenExistsAt(to)))) {
			removeQueenFrom(from);
			addQueenAt(to);
		}
	}

	public boolean queenExistsAt(XYLocation l) {
		return (queenExistsAt(l.getX(), l.getY()));
	}

	private boolean queenExistsAt(int x, int y) {
		return squares[x][y];
	}

	public int getNumberOfQueensOnBoard() {
		int count = 0;
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				if (squares[col][row])
					count++;
			}
		}
		return count;
	}

	public List<XYLocation> getQueenPositions() {
		ArrayList<XYLocation> result = new ArrayList<>();
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				if (queenExistsAt(col, row))
					result.add(new XYLocation(col, row));
			}
		}
		return result;

	}

	public int getNumberOfAttackingPairs() {
		return getQueenPositions().stream().mapToInt(this::getNumberOfAttacksOn).sum() / 2;
	}

	public int getNumberOfAttacksOn(XYLocation l) {
		int x = l.getX();
		int y = l.getY();
		return numberOfHorizontalAttacksOn(x, y) + numberOfVerticalAttacksOn(x, y) + numberOfDiagonalAttacksOn(x, y);
	}

	public boolean isSquareUnderAttack(XYLocation l) {
		int x = l.getX();
		int y = l.getY();
		return (isSquareHorizontallyAttacked(x, y) || isSquareVerticallyAttacked(x, y)
				|| isSquareDiagonallyAttacked(x, y));
	}

	private boolean isSquareHorizontallyAttacked(int x, int y) {
		return numberOfHorizontalAttacksOn(x, y) > 0;
	}

	private boolean isSquareVerticallyAttacked(int x, int y) {
		return numberOfVerticalAttacksOn(x, y) > 0;
	}

	private boolean isSquareDiagonallyAttacked(int x, int y) {
		return numberOfDiagonalAttacksOn(x, y) > 0;
	}

	private int numberOfHorizontalAttacksOn(int x, int y) {
		int result = 0;
		for (int col = 0; col < getSize(); col++) {
			if ((queenExistsAt(col, y)))
				if (col != x)
					result++;
		}
		return result;
	}

	private int numberOfVerticalAttacksOn(int x, int y) {
		int result = 0;
		for (int row = 0; row < getSize(); row++) {
			if ((queenExistsAt(x, row)))
				if (row != y)
					result++;
		}
		return result;
	}

	private int numberOfDiagonalAttacksOn(int x, int y) {
		int result = 0;
		int col;
		int row;
		// forward up diagonal
		for (col = (x + 1), row = (y - 1); (col < getSize() && (row > -1)); col++, row--)
			if (queenExistsAt(col, row))
				result++;

		// forward down diagonal
		for (col = (x + 1), row = (y + 1); ((col < getSize()) && (row < getSize())); col++, row++)
			if (queenExistsAt(col, row))
				result++;

		// backward up diagonal
		for (col = (x - 1), row = (y - 1); ((col > -1) && (row > -1)); col--, row--)
			if (queenExistsAt(col, row))
				result++;

		// backward down diagonal
		for (col = (x - 1), row = (y + 1); ((col > -1) && (row < getSize())); col--, row++)
			if (queenExistsAt(col, row))
				result++;

		return result;
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				if (queenExistsAt(col, row))
					result = 17 * result + 7 * col + row;
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && getClass() == o.getClass()) {
			NQueensBoard aBoard = (NQueensBoard) o;
			for (int col = 0; col < getSize(); col++) {
				for (int row = 0; row < getSize(); row++) {
					if (queenExistsAt(col, row) != aBoard.queenExistsAt(col, row))
						return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < getSize(); row++) {
			for (int col = 0; col < getSize(); col++) {
				if (queenExistsAt(col, row))
					builder.append('Q');
				else
					builder.append('-');
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}