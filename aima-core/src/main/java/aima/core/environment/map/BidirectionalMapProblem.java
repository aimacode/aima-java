package aima.core.environment.map;

import aima.core.search.framework.problem.BidirectionalProblem;
import aima.core.search.framework.problem.DefaultGoalTest;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class BidirectionalMapProblem extends Problem implements BidirectionalProblem {

	Map map;

	Problem reverseProblem;

	public BidirectionalMapProblem(Map map, String initialState, String goalState) {
		this(map, initialState, goalState, new DefaultGoalTest(goalState));
	}

	public BidirectionalMapProblem(Map map, String initialState, String goalState, GoalTest goalTest) {
		super(initialState, MapFunctionFactory.getActionsFunction(map), MapFunctionFactory.getResultFunction(),
				goalTest, new MapStepCostFunction(map));

		this.map = map;

		reverseProblem = new Problem(goalState, MapFunctionFactory.getReverseActionsFunction(map),
				MapFunctionFactory.getResultFunction(), new DefaultGoalTest(initialState),
				new MapStepCostFunction(map));
	}

	//
	// START Interface BidrectionalProblem
	public Problem getOriginalProblem() {
		return this;
	}

	public Problem getReverseProblem() {
		return reverseProblem;
	}
	// END Interface BirectionalProblem
	//
}
