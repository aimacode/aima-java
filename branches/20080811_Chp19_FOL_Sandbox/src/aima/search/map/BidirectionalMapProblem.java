package aima.search.map;

import aima.search.framework.BidirectionalProblem;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class BidirectionalMapProblem extends Problem implements
		BidirectionalProblem {

	Map map;

	Problem reverseProblem;

	public BidirectionalMapProblem(Map aMap, String initialState,
			String goalState) {
		super(initialState, new MapSuccessorFunction(aMap), new MapGoalTest(
				goalState), new MapStepCostFunction(aMap));

		map = aMap;

		reverseProblem = new Problem(goalState, new MapSuccessorFunction(aMap),
				new MapGoalTest(initialState), new MapStepCostFunction(aMap));
	}

	public BidirectionalMapProblem(Map aMap, String initialState,
			String goalState, HeuristicFunction hf) {
		super(initialState, new MapSuccessorFunction(aMap), new MapGoalTest(
				goalState), new MapStepCostFunction(aMap), hf);

		map = aMap;

		reverseProblem = new Problem(goalState, new MapSuccessorFunction(aMap),
				new MapGoalTest(initialState), new MapStepCostFunction(aMap),
				hf);
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
