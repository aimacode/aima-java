package aima.search.framework;

/**
 * @author Ravi Mohan
 * 
 */

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 62.
 * 
 * A problem can be defined formally by four components: 1) Initial State. 2)
 * Successor Function. 3) Goal Test. 4) Path Cost.
 */

public class Problem {

	Object initialState;

	SuccessorFunction successorFunction;

	GoalTest goalTest;

	StepCostFunction stepCostFunction;

	public Problem(Object initialState, SuccessorFunction successorFunction,
			GoalTest goalTest) {

		this.initialState = initialState;
		this.successorFunction = successorFunction;
		this.goalTest = goalTest;
		this.stepCostFunction = new DefaultStepCostFunction();
	}

	public Problem(Object initialState, SuccessorFunction successorFunction,
			GoalTest goalTest, StepCostFunction stepCostFunction) {
		this(initialState, successorFunction, goalTest);
		this.stepCostFunction = stepCostFunction;
	}

	public Object getInitialState() {

		return initialState;
	}

	public boolean isGoalState(Object state) {

		return goalTest.isGoalState(state);
	}

	public StepCostFunction getStepCostFunction() {
		return stepCostFunction;
	}

	public SuccessorFunction getSuccessorFunction() {
		return successorFunction;
	}
}