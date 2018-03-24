package aima.core.environment.nqueens;

import aima.core.util.datastructure.GridLocation2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NQueensBoard {

    private boolean[][] board;

    public int getSize() {
        return board.length;
    }

    public NQueensBoard(int boardSize) {
        board = new boolean[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                board[i][j] = false;
            }
        }
        Random r = new Random();
        for (int i = 0; i < boardSize; i++)
            addQueenAt(new GridLocation2D(i, r.nextInt(boardSize)));
    }

    public void addQueenAt(GridLocation2D location){
        if (!queenExistsAt(location))
            board[location.getX()][location.getY()] = true;
    }

    public void removeQueenFrom(GridLocation2D location) {
        if (queenExistsAt(location) ) {
            board[location.getX()][location.getY()] = false;
        }
    }

    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                board[i][j] = false;
            }
        }
    }

    private boolean queenExistsAt(int x, int y) {
        return (board[x][y]);
    }

    public void setQueensAt(List<GridLocation2D> locations){
        clear();
        locations.forEach(this::addQueenAt);
    }
    private boolean queenExistsAt(GridLocation2D loc) {
        return board[loc.getX()][loc.getY()];
    }

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

    public int getNumberOfAttackingPairs() {
        int result = 0;
        for (GridLocation2D location : getQueenPositions()) {
            result += getNumberOfAttacksOn(location);
        }
        return result / 2;
    }

    public int getNumberOfAttacksOn(GridLocation2D location) {
        int x = location.getX();
        int y = location.getY();
        return numberOfHorizontalAttacksOn(x, y) + numberOfVerticalAttacksOn(x, y) + numberOfDiagonalAttacksOn(x, y);
    }

    private int numberOfHorizontalAttacksOn(int x, int y) {
        int retVal = 0;
        for (int i = 0; i < getSize(); i++) {
            if ((queenExistsAt(i, y)))
                if (i != x)
                    retVal++;
        }
        return retVal;
    }

    private int numberOfVerticalAttacksOn(int x, int y) {
        int retVal = 0;
        for (int j = 0; j < getSize(); j++) {
            if ((queenExistsAt(x, j)))
                if (j != y)
                    retVal++;
        }
        return retVal;
    }

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

    public void print() {
        System.out.println(getBoardPic());
    }

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

