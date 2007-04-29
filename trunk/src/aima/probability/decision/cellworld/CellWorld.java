package aima.probability.decision.cellworld;

import java.util.ArrayList;
import java.util.List;

import aima.probability.Randomizer;
import aima.util.Pair;

public class CellWorld {
	public static final String LEFT = "left";

	public static final String RIGHT = "right";

	public static final String UP = "up";

	public static final String DOWN = "down";

	List<Cell> blockedCells, allCells;

	private int numberOfRows;

	private int numberOfColumns;

	public CellWorld(int numberOfRows, int numberOfColumns,
			double initialUtility, double initialReward) {
		allCells = new ArrayList<Cell>();
		blockedCells = new ArrayList<Cell>();

		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;

		for (int row = 1; row <= numberOfRows; row++) {
			for (int col = 1; col <= numberOfColumns; col++) {
				allCells.add(new Cell(row, col, initialUtility, initialReward));
			}
		}
	}

	public void markBlocked(int i, int j) {
		blockedCells.add(getCellAt(i, j));

	}

	private boolean isBlocked(int i, int j) {
		if ((i < 1) || (i > numberOfRows) || (j < 1) || (j > numberOfColumns)) {
			return true;
		}
		for (Cell c : blockedCells) {
			if ((c.getX() == i) && (c.getY() == j)) {
				return true;
			}
		}
		return false;
	}

	private Cell getCellAt(int i, int j) {
		for (Cell c : allCells) {
			if ((c.getX() == i) && (c.getY() == j)) {
				return c;
			}
		}
		throw new RuntimeException("No Cell found at " + i + " , " + j);
	}

	public Pair<Integer, Integer> moveProbabilisticallyFrom(int i, int j, String direction,
			Randomizer r) {

		return moveFrom(i,j,determineDirectionOfMovement(direction, r));

	}
	
	private Pair<Integer, Integer> moveFrom(int i, int j, String direction){
		if (direction.equals(LEFT)) {
			return moveLeftFrom(i, j);
		}
		if (direction.equals(RIGHT)) {
			return moveRightFrom(i, j);
		}
		if (direction.equals(UP)) {
			return moveUpFrom(i, j);
		}
		if (direction.equals(DOWN)) {
			return moveDownFrom(i, j);
		}
		throw new RuntimeException("Unable to move " + direction + " from " + i
				+ " , " + j);
	}
	
	private String determineDirectionOfMovement(String commandedDirection, Randomizer r){
		double prob = r.nextDouble();
		if (prob < 0.8){
			return commandedDirection;
		}else if ((prob > 0.8) && (prob < 0.9)){
			if ((commandedDirection.equals(LEFT)) || (commandedDirection.equals(RIGHT))){
				return UP;
			}
			if ((commandedDirection.equals(UP)) || (commandedDirection.equals(DOWN))){
				return LEFT;
			}
		}else{ //0.9 < prob  < 1.0
			if ((commandedDirection.equals(LEFT)) || (commandedDirection.equals(RIGHT))){
				return DOWN;
			}
			if ((commandedDirection.equals(UP)) || (commandedDirection.equals(DOWN))){
				return RIGHT;
			}
		}
		throw new RuntimeException("Unable to determine direction when command =  " + commandedDirection + " and probability = " +prob);
	}

	private Pair<Integer, Integer> moveLeftFrom(int i, int j) {
		if (isBlocked(i, j - 1)) {
			return new Pair<Integer, Integer>(i, j);
		}
		return new Pair<Integer, Integer>(i, j - 1);
	}

	private Pair<Integer, Integer> moveRightFrom(int i, int j) {
		if (isBlocked(i, j + 1)) {
			return new Pair<Integer, Integer>(i, j);
		}
		return new Pair<Integer, Integer>(i, j + 1);
	}
	
	private Pair<Integer, Integer> moveUpFrom(int i, int j) {
		if (isBlocked(i+1, j)) {
			return new Pair<Integer, Integer>(i, j);
		}
		return new Pair<Integer, Integer>(i+1, j);
	}
	
	private Pair<Integer, Integer> moveDownFrom(int i, int j) {
		if (isBlocked(i-1, j)) {
			return new Pair<Integer, Integer>(i, j);
		}
		return new Pair<Integer, Integer>(i-1, j);
	}

	public void setReward(int i, int j, double reward) {
		Cell c = getCellAt(i, j);
		c.setReward(reward);
		
	}
}
