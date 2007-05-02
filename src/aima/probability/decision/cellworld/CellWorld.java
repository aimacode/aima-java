package aima.probability.decision.cellworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import aima.probability.Randomizer;
import aima.probability.decision.MDPTransitionModel;
import aima.probability.decision.RewardFunction;
import aima.probability.decision.UtilityFunction;
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

	public CellWorldPosition moveProbabilisticallyFrom(int i, int j,
			String direction, Randomizer r) {

		return moveFrom(i, j, determineDirectionOfActualMovement(direction, r));

	}

	private CellWorldPosition moveFrom(int i, int j, String direction) {
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
	
	private CellWorldPosition moveFrom(CellWorldPosition startingPosition, String direction){
		return moveFrom(startingPosition.getX(),startingPosition.getY() , direction);
	}

	private String determineDirectionOfActualMovement(
			String commandedDirection, double prob) {
		if (prob < 0.8) {
			return commandedDirection;
		} else if ((prob > 0.8) && (prob < 0.9)) {
			if ((commandedDirection.equals(LEFT))
					|| (commandedDirection.equals(RIGHT))) {
				return UP;
			}
			if ((commandedDirection.equals(UP))
					|| (commandedDirection.equals(DOWN))) {
				return LEFT;
			}
		} else { // 0.9 < prob < 1.0
			if ((commandedDirection.equals(LEFT))
					|| (commandedDirection.equals(RIGHT))) {
				return DOWN;
			}
			if ((commandedDirection.equals(UP))
					|| (commandedDirection.equals(DOWN))) {
				return RIGHT;
			}
		}
		throw new RuntimeException(
				"Unable to determine direction when command =  "
						+ commandedDirection + " and probability = " + prob);

	}

	private String determineDirectionOfActualMovement(
			String commandedDirection, Randomizer r) {
		return determineDirectionOfActualMovement(commandedDirection, r
				.nextDouble());

	}

	private CellWorldPosition moveLeftFrom(int i, int j) {
		if (isBlocked(i, j - 1)) {
			return new CellWorldPosition(i, j);
		}
		return new CellWorldPosition(i, j - 1);
	}

	private CellWorldPosition moveRightFrom(int i, int j) {
		if (isBlocked(i, j + 1)) {
			return new CellWorldPosition(i, j);
		}
		return new CellWorldPosition(i, j + 1);
	}

	private CellWorldPosition moveUpFrom(int i, int j) {
		if (isBlocked(i + 1, j)) {
			return new CellWorldPosition(i, j);
		}
		return new CellWorldPosition(i + 1, j);
	}

	private CellWorldPosition moveDownFrom(int i, int j) {
		if (isBlocked(i - 1, j)) {
			return new CellWorldPosition(i, j);
		}
		return new CellWorldPosition(i - 1, j);
	}

	public void setReward(int i, int j, double reward) {
		Cell c = getCellAt(i, j);
		c.setReward(reward);

	}

	public List<Cell> unblockedCells() {
		List<Cell> res = new ArrayList<Cell>();
		for (Cell c : allCells) {
			if (!(blockedCells.contains(c))) {
				res.add(c);
			}
		}
		return res;
	}

	public boolean isBlocked(Pair<Integer, Integer> p) {
		return isBlocked(p.getFirst(), p.getSecond());
	}

	// what is the probability of starting from position p1 taking action a and
	// reaaching position p2
	// method is public ONLY for testing do not use in client code.
	public double getTransitionProbability(
			CellWorldPosition startingPosition, String actionDesired,
			CellWorldPosition endingPosition) {
		

		String firstRightAngledAction =  determineDirectionOfActualMovement(actionDesired, 0.85);
		String secondRightAngledAction =  determineDirectionOfActualMovement(actionDesired, 0.95);
		
		
		Hashtable<String,CellWorldPosition> actionsToPositions= new Hashtable<String,CellWorldPosition>();
		actionsToPositions.put(actionDesired, moveFrom(startingPosition, actionDesired));
		actionsToPositions.put(firstRightAngledAction, moveFrom(startingPosition, firstRightAngledAction));
		actionsToPositions.put(secondRightAngledAction, moveFrom(startingPosition, secondRightAngledAction));
		
		Hashtable<CellWorldPosition, Double> positionsToProbability = new Hashtable<CellWorldPosition, Double> ();
		for (CellWorldPosition p : actionsToPositions.values()){
			positionsToProbability.put(p, 0.0);
		}
		
		for (String action: actionsToPositions.keySet()){
			CellWorldPosition position= actionsToPositions.get(action);
			double value = positionsToProbability.get(position);
			if (action.equals(actionDesired)){
				positionsToProbability.put(position, value+0.8);
			}else{ //right angled steps
				positionsToProbability.put(position, value+0.1);
			}
			
		}
		
		if (positionsToProbability.keySet().contains(endingPosition)){
			return positionsToProbability.get(endingPosition);
		}
		else{
			return 0.0;
		}
		

	}

	private UtilityFunction getUtilityFunction() {
		UtilityFunction<CellWorldPosition> uf = new UtilityFunction<CellWorldPosition>();
		for (Cell c : unblockedCells()) {
			uf.setUtility(c.position(), c.getUtility());
		}
		return uf;
	}

	public MDPTransitionModel<CellWorldPosition,String> getTransitionModel() {
		MDPTransitionModel<CellWorldPosition, String> mtm = new MDPTransitionModel<CellWorldPosition, String>();
	
		List<String> actions =  Arrays.asList(new String[]{UP,DOWN,LEFT,RIGHT});
		for (Cell c : unblockedCells()) {
			CellWorldPosition  startingPosition = c.position();
			for (String actionDesired: actions){
				for (Cell target:unblockedCells()){ //too much work?  should just cycle through neighbouring cells
					CellWorldPosition endingPosition = target.position();
					double transitionProbability = getTransitionProbability(startingPosition, actionDesired, endingPosition);
					if (!(transitionProbability == 0.0)){
						
						mtm.setTransitionProbability(startingPosition, actionDesired, endingPosition, transitionProbability);
					}
				}
			}
		}
		return mtm;
	}

	public class CellWorldRewardFunction implements
			RewardFunction<CellWorldPosition> {
		private Hashtable<CellWorldPosition, Double> positionToReward;

		CellWorldRewardFunction() {
			for (Cell c : unblockedCells()) {
				CellWorldPosition pos = c.position();
				double reward = c.getReward();
				positionToReward.put(pos, reward);
			}
		}

		public double getRewardFor(CellWorldPosition state) {

			return positionToReward.get(state);
		}

	}

}
