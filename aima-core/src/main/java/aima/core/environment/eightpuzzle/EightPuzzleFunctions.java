package aima.core.environment.eightpuzzle;

import aima.core.agent.Action;
import aima.core.search.framework.Node;
import aima.core.util.datastructure.XYLocation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Useful functions for solving EightPuzzle problems.
 * @author Ruediger Lunde
 */
public class EightPuzzleFunctions {

	public static final EightPuzzleBoard GOAL_STATE = new EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });

	public static List<Action> getActions(EightPuzzleBoard state) {
		return Stream.of(EightPuzzleBoard.UP, EightPuzzleBoard.DOWN, EightPuzzleBoard.LEFT, EightPuzzleBoard.RIGHT).
				filter(state::canMoveGap).collect(Collectors.toList());
	}

	public static EightPuzzleBoard getResult(EightPuzzleBoard state, Action action) {
		EightPuzzleBoard result = state.clone();

		if (result.canMoveGap(action)) {
			if (action == EightPuzzleBoard.UP)
				result.moveGapUp();
			else if (action == EightPuzzleBoard.DOWN)
				result.moveGapDown();
			else if (action == EightPuzzleBoard.LEFT)
				result.moveGapLeft();
			else if (action == EightPuzzleBoard.RIGHT)
				result.moveGapRight();
		}
		return result;
	}

	public static double getManhattanDistance(Node<EightPuzzleBoard, Action> node) {
		EightPuzzleBoard currState = node.getState();
		int result = 0;
		for (int val = 1; val <= 8; val++) {
			XYLocation locCurr = currState.getLocationOf(val);
			XYLocation locGoal = GOAL_STATE.getLocationOf(val);
			result += Math.abs(locGoal.getX() - locCurr.getX());
			result += Math.abs(locGoal.getY() - locCurr.getY());
		}
		return result;
	}

	public static int getNumberOfMisplacedTiles(Node<EightPuzzleBoard, Action> node) {
		EightPuzzleBoard currState = node.getState();
		int result = 0;
		for (int val = 1; val <= 8; val++)
			if (!(currState.getLocationOf(val).equals(GOAL_STATE.getLocationOf(val))))
				result++;
		return result;
	}
}