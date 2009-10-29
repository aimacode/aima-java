package aima.core.search.framework;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page ??.
 * 
 * A problem can be defined formally by five components: <br>
 * <ul>
 * <li>The <b>initial state</b> that the agent starts in.</li>
 * <li>A description of the possible <b>actions</b> available to the agent. 
 * Given a particular state s, ACTIONS(s) returns the set of actions that can 
 * be executed in s.</li>
 * <li>A description of what each action does; the formal name for this is the 
 * <b>transition model, specified by a function RESULT(s, a) that returns the 
 * state that results from doing action a in state s.</b></li>
 * <li>The <b>goal test</b>, which determines whether a given state is a goal 
 * state.</li>
 * <li>A <b>path cost</b> function that assigns a numeric cost to each path. 
 * The problem-solving agent chooses a cost function that reflects its own 
 * performance measure. The <b>step cost</b> of taking action a in state s 
 * to reach state s' is denoted by c(s,a,s')</li>
 * </ul>
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Problem {

	protected Object initialState;

	protected ActionsFunction actionsFunction;
	
	protected ResultFunction resultFunction;

	protected GoalTest goalTest;

	protected StepCostFunction stepCostFunction;

	protected HeuristicFunction heuristicFunction;

	public Problem(Object initialState, ActionsFunction actionsFunction,
			ResultFunction resultFunction,
			GoalTest goalTest) {

		this.initialState = initialState;
		this.actionsFunction = actionsFunction;
		this.resultFunction = resultFunction;
		this.goalTest = goalTest;
		this.stepCostFunction = new DefaultStepCostFunction();
		this.heuristicFunction = new DefaultHeuristicFunction();
	}

	public Problem(Object initialState, ActionsFunction actionsFunction,
			ResultFunction resultFunction,
			GoalTest goalTest, StepCostFunction stepCostFunction) {
		this(initialState, actionsFunction, resultFunction, goalTest);
		this.stepCostFunction = stepCostFunction;
	}

	public Problem(Object initialState, ActionsFunction actionsFunction,
			ResultFunction resultFunction,
			GoalTest goalTest, HeuristicFunction heuristicFunction) {
		this(initialState, actionsFunction, resultFunction, goalTest);
		this.heuristicFunction = heuristicFunction;
	}

	public Problem(Object initialState,ActionsFunction actionsFunction,
			ResultFunction resultFunction,
			GoalTest goalTest, StepCostFunction stepCostFunction,
			HeuristicFunction heuristicFunction) {
		this(initialState, actionsFunction, resultFunction, goalTest, stepCostFunction);
		this.heuristicFunction = heuristicFunction;
	}

	public Object getInitialState() {

		return initialState;
	}

	public boolean isGoalState(Object state) {

		return goalTest.isGoalState(state);
	}

	public ActionsFunction getActionsFunction() {
		return actionsFunction;
	}
	
	public ResultFunction getResultFunction() {
		return resultFunction;
	}

	public HeuristicFunction getHeuristicFunction() {
		return heuristicFunction;
	}
	
	public StepCostFunction getStepCostFunction() {
		return stepCostFunction;
	}
	
	//
	// PROTECTED METHODS
	//
	protected Problem() {
	}
}