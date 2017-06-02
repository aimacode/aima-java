package aima.core.environment.eightpuzzle;

import aima.core.agent.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class EightPuzzleFunctions {

	public static List<Action> getActions(EightPuzzleBoard state) {
		List<Action> actions = new ArrayList<>(4);

		if (state.canMoveGap(EightPuzzleBoard.UP))
			actions.add(EightPuzzleBoard.UP);
		if (state.canMoveGap(EightPuzzleBoard.DOWN))
			actions.add(EightPuzzleBoard.DOWN);
		if (state.canMoveGap(EightPuzzleBoard.LEFT))
			actions.add(EightPuzzleBoard.LEFT);
		if (state.canMoveGap(EightPuzzleBoard.RIGHT))
			actions.add(EightPuzzleBoard.RIGHT);

		return actions;
	}

	public static EightPuzzleBoard getResult(EightPuzzleBoard state, Action action) {
		EightPuzzleBoard result = new EightPuzzleBoard(state);

		if (EightPuzzleBoard.UP.equals(action) && state.canMoveGap(EightPuzzleBoard.UP))
			result.moveGapUp();
		else if (EightPuzzleBoard.DOWN.equals(action) && state.canMoveGap(EightPuzzleBoard.DOWN))
			result.moveGapDown();
		else if (EightPuzzleBoard.LEFT.equals(action) && state.canMoveGap(EightPuzzleBoard.LEFT))
			result.moveGapLeft();
		else if (EightPuzzleBoard.RIGHT.equals(action) && state.canMoveGap(EightPuzzleBoard.RIGHT))
			result.moveGapRight();
		return result;
	}

	public static boolean testGoal(EightPuzzleBoard state) {
		return state.equals(goal);
	}

	private static EightPuzzleBoard goal = new EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });
}