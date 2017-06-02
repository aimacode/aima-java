package aima.core.environment.eightpuzzle;

import aima.core.agent.Action;
import aima.core.search.framework.problem.BidirectionalProblem;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;

/**
 * @author Ruediger Lunde
 * 
 */
public class BidirectionalEightPuzzleProblem extends GeneralProblem<EightPuzzleBoard, Action>
		implements BidirectionalProblem<EightPuzzleBoard, Action> {

	private Problem<EightPuzzleBoard, Action> reverseProblem;

	public BidirectionalEightPuzzleProblem(EightPuzzleBoard initialState) {
		super(initialState, EightPuzzleFunctions::getActions, EightPuzzleFunctions::getResult,
				GoalTest.isEqual(new EightPuzzleBoard(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8})));

		reverseProblem = new GeneralProblem<>(new EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }),
				EightPuzzleFunctions::getActions, EightPuzzleFunctions::getResult,
				GoalTest.isEqual(initialState));
	}

	public Problem<EightPuzzleBoard, Action> getOriginalProblem() {
		return this;
	}

	public Problem<EightPuzzleBoard, Action> getReverseProblem() {
		return reverseProblem;
	}
}
