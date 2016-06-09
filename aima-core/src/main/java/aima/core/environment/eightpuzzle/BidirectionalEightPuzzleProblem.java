package aima.core.environment.eightpuzzle;

import aima.core.search.framework.problem.BidirectionalProblem;
import aima.core.search.framework.problem.DefaultGoalTest;
import aima.core.search.framework.problem.Problem;

/**
 * @author Ruediger Lunde
 * 
 */
public class BidirectionalEightPuzzleProblem extends Problem implements BidirectionalProblem {

	Problem reverseProblem;

	public BidirectionalEightPuzzleProblem(EightPuzzleBoard initialState) {
		super(initialState, EightPuzzleFunctionFactory.getActionsFunction(),
				EightPuzzleFunctionFactory.getResultFunction(),
				new DefaultGoalTest(new EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 })));

		reverseProblem = new Problem(new EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }),
				EightPuzzleFunctionFactory.getActionsFunction(), EightPuzzleFunctionFactory.getResultFunction(),
				new DefaultGoalTest(initialState));
	}

	public Problem getOriginalProblem() {
		return this;
	}

	public Problem getReverseProblem() {
		return reverseProblem;
	}
}
