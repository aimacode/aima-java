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

	public Pair<Integer, Integer> moveProbabilisticallyFrom(int i, int j,
			String direction, Randomizer r) {

		return moveFrom(i, j, determineDirectionOfActualMovement(direction, r));

	}

	private Pair<Integer, Integer> moveFrom(int i, int j, String direction) {
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
	
	private Pair<Integer, Integer> moveFrom(Pair<Integer,Integer> startingPosition, String direction){
		return moveFrom(startingPosition.getFirst(),startingPosition.getSecond() , direction);
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
		if (isBlocked(i + 1, j)) {
			return new Pair<Integer, Integer>(i, j);
		}
		return new Pair<Integer, Integer>(i + 1, j);
	}

	private Pair<Integer, Integer> moveDownFrom(int i, int j) {
		if (isBlocked(i - 1, j)) {
			return new Pair<Integer, Integer>(i, j);
		}
		return new Pair<Integer, Integer>(i - 1, j);
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
			Pair<Integer, Integer> startingPosition, String actionDesired,
			Pair<Integer, Integer> endingPosition) {
		

		String firstRightAngledAction =  determineDirectionOfActualMovement(actionDesired, 0.85);
		String secondRightAngledAction =  determineDirectionOfActualMovement(actionDesired, 0.95);
		
		
		Hashtable<String,Pair<Integer,Integer>> actionsToPositions= new Hashtable<String,Pair<Integer,Integer>>();
		actionsToPositions.put(actionDesired, moveFrom(startingPosition, actionDesired));
		actionsToPositions.put(firstRightAngledAction, moveFrom(startingPosition, firstRightAngledAction));
		actionsToPositions.put(secondRightAngledAction, moveFrom(startingPosition, secondRightAngledAction));
		
		Hashtable<Pair<Integer,Integer>, Double> positionsToProbability = new Hashtable<Pair<Integer,Integer>, Double> ();
		for (Pair<Integer,Integer> p : actionsToPositions.values()){
			positionsToProbability.put(p, 0.0);
		}
		
		for (String action: actionsToPositions.keySet()){
			Pair<Integer,Integer> position= actionsToPositions.get(action);
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
		UtilityFunction<Pair<Integer, Integer>> uf = new UtilityFunction<Pair<Integer, Integer>>();
		for (Cell c : unblockedCells()) {
			uf.setUtility(c.position(), c.getUtility());
		}
		return uf;
	}

	public MDPTransitionModel<Pair<Integer, Integer>,String> getTransitionModel() {
		MDPTransitionModel<Pair<Integer, Integer>, String> mtm = new MDPTransitionModel<Pair<Integer, Integer>, String>();
	
		List<String> actions =  Arrays.asList(new String[]{UP,DOWN,LEFT,RIGHT});
		for (Cell c : unblockedCells()) {
			Pair<Integer,Integer> startingPosition = c.position();
			for (String actionDesired: actions){
				for (Cell target:unblockedCells()){ //too much work?  should just cycle through neighbouring cells
					Pair<Integer,Integer> endingPosition = target.position();
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
			RewardFunction<Pair<Integer, Integer>> {
		private Hashtable<Pair<Integer, Integer>, Double> hash;

		CellWorldRewardFunction() {
			for (Cell c : unblockedCells()) {
				Pair<Integer, Integer> pair = c.position();
				double reward = c.getReward();
				hash.put(pair, reward);
			}
		}

		public double getRewardFor(Pair<Integer, Integer> state) {

			return hash.get(state);
		}

	}

}
