package aima.core.environment.nqueens;

import aima.core.util.datastructure.GridLocation2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is an implementation of the n-queens problem. The goal of the n-queens
 * problem is to place n-queens on a hypothetical chess board of dimensions nXn
 * such that no queen attacks any other. A queen attacks any piece in the same
 * row, column or diagonal.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 * @author samagra
 */
public class NQueensBoard {

    /**
     * X---> increases left to right with zero based index Y increases top to
     * bottom with zero based index | | V
     */
    private boolean[][] board;

    /** Parameters for initialization. */
    public enum Configuration{
        RANDOM_QUEENS_IN_EVERY_COL, EMPTY
    }

    /**
     * Default constructor. Creates a square board of side given by boardSize.
     * The board is initialised by a random arrangement of queens in each row.
     * @param boardSize
     *          the length of the side of the square board.
     */

    public NQueensBoard(int boardSize) {
        board = new boolean[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = false;
            }
        }
    }

    /**
     * A constructor that specifies the initial configuration of the board.
     * @param boardSize the length of the side of the square board.
     * @param config the initial configuration of the board
     */
    public NQueensBoard(int boardSize,Configuration config) {
        board = new boolean[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = false;
            }
        }
        if (config == Configuration.EMPTY) {
            return;
        }
        else if (config == Configuration.RANDOM_QUEENS_IN_EVERY_COL){
        Random r = new Random();
        for (int i = 0; i < boardSize; i++)
            addQueenAt(new GridLocation2D(i, r.nextInt(boardSize)));
        }

    }

    /**
     * This constructor can be used to specify the initial location of the board.
     * @param boardSize the length of the side of the square board.
     * @param locations A list of location where the queens are initially located.
     */
    public NQueensBoard(int boardSize,List<GridLocation2D> locations) {
        board = new boolean[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = false;
            }
        }
        setQueensAt(locations);
    }

    /**
     * @return
     *      The size of the board.
     */
    public int getSize() {
        return board.length;
    }

    /**
     * Adds queen at a desired location
     * @param location
     *      The location specifying the cell in which the queen is to be placed.
     */
    public void addQueenAt(GridLocation2D location){
        if (!queenExistsAt(location))
            board[location.getX()][location.getY()] = true;
    }

    /**
     * This method moves a queen from one location to another.
     * @param initialLocation The initial position of the queen.
     * @param finalLocation The final position of the queen.
     */
    public void moveQueen(GridLocation2D initialLocation, GridLocation2D finalLocation){
        removeQueenFrom(initialLocation);
        addQueenAt(finalLocation);
    }

    /**
     * Removes queen from a desired location
     * @param location
     *      The location specifying the cell from which the queen is to be removed.
     */
    public void removeQueenFrom(GridLocation2D location) {
        if (queenExistsAt(location) ) {
            board[location.getX()][location.getY()] = false;
        }
    }

    /**
     * Removes all the queens from the board.
     */
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                board[i][j] = false;
            }
        }
    }

    /**
     * This method checks if a queen is present at a certain cell.
     * @param x specifies the row of the cell
     * @param y specifies the column of the cell
     * @return true if queen is present at the location (x,y) else false
     */
    private boolean queenExistsAt(int x, int y) {
        return board[x][y];
    }

    /**
     * Sets the chessboard to a specific configuration specified by a list of
     * n locations.
     * @param locations A list of locations specifying the configuration of the
     *                  board.
     */
    private void setQueensAt(List<GridLocation2D> locations){
        clear();
        locations.forEach(this::addQueenAt);
    }

    /**
     * This method checks if a queen is present at a certain cell.
     * @param location specifies the location of the cell to be checked
     * @return true if queen is present at the location else false
     */
    public boolean queenExistsAt(GridLocation2D location) {
        return board[location.getX()][location.getY()];
    }

    /**
     * @return The number of queens present on the board.
     */
    public int getNumberOfQueensOnBoard() {
        int count = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (board[i][j])
                    count++;
            }
        }
        return count;
    }

    /**
     * This method gives the current configuration of the board.
     * @return A list of locations specifying the positions of the queens
     *          on the board.
     */
    public List<GridLocation2D> getQueenPositions() {
        ArrayList<GridLocation2D> result = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (queenExistsAt(i, j))
                    result.add(new GridLocation2D(i, j));
            }
        }
        return result;

    }

    /**
     * @return The number of attacking pairs in the present board configuration
     */
    public int getNumberOfAttackingPairs() {
        int result = 0;
        for (GridLocation2D location : getQueenPositions()) {
            result += getNumberOfAttacksOn(location);
        }
        return result / 2;
    }

    /**
     * This method gives the number of queens that can attack a particular position.
     * @param location The location for which the number of attacks have to be calculated
     * @return The number of attacks on a given location.
     */
    public int getNumberOfAttacksOn(GridLocation2D location) {
        int x = location.getX();
        int y = location.getY();
        return numberOfHorizontalAttacksOn(x, y) + numberOfVerticalAttacksOn(x, y) + numberOfDiagonalAttacksOn(x, y);
    }

    /**
     * This method gives the number of queens that can attack a particular
     * position horizontally.
     * @param x specifies the row of the cell
     * @param y specifies the column of the cell
     * @return The number of horizontal attacks on a given location.
     */
    private int numberOfHorizontalAttacksOn(int x, int y) {
        int retVal = 0;
        for (int i = 0; i < getSize(); i++) {
            if ((queenExistsAt(i, y)))
                if (i != x)
                    retVal++;
        }
        return retVal;
    }

    /**
     * This method gives the number of queens that can attack a particular
     * position vertically.
     * @param x specifies the row of the cell
     * @param y specifies the column of the cell
     * @return The number of vertical attacks on a given location.
     */
    private int numberOfVerticalAttacksOn(int x, int y) {
        int retVal = 0;
        for (int j = 0; j < getSize(); j++) {
            if ((queenExistsAt(x, j)))
                if (j != y)
                    retVal++;
        }
        return retVal;
    }

    /**
     * This method gives the number of queens that can attack a particular
     * position diagonally.
     * @param x specifies the row of the cell
     * @param y specifies the column of the cell
     * @return The number of diagonal attacks on a given location.
     */
    private int numberOfDiagonalAttacksOn(int x, int y) {
        int retVal = 0;
        int i;
        int j;
        // forward up diagonal
        for (i = (x + 1), j = (y - 1); (i < getSize() && (j > -1)); i++, j--) {
            if (queenExistsAt(i, j))
                retVal++;
        }
        // forward down diagonal
        for (i = (x + 1), j = (y + 1); ((i < getSize()) && (j < getSize())); i++, j++) {
            if (queenExistsAt(i, j))
                retVal++;
        }
        // backward up diagonal
        for (i = (x - 1), j = (y - 1); ((i > -1) && (j > -1)); i--, j--) {
            if (queenExistsAt(i, j))
                retVal++;
        }

        // backward down diagonal
        for (i = (x - 1), j = (y + 1); ((i > -1) && (j < getSize())); i--, j++) {
            if (queenExistsAt(i, j))
                retVal++;
        }

        return retVal;
    }

    /**
     * To print the configuration of the board at a particular moment.
     */
    public void print() {
        System.out.println(getBoardPic());
    }

    /**
     * @return A string representing the configuration of the board .
     */
    public String getBoardPic() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; (row < getSize()); row++) { // row
            for (int col = 0; (col < getSize()); col++) { // col
                if (queenExistsAt(col, row))
                    builder.append(" Q ");
                else
                    builder.append(" - ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < getSize(); row++) { // rows
            for (int col = 0; col < getSize(); col++) { // columns
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

