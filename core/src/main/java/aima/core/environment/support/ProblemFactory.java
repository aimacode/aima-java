package aima.core.environment.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2DFunctionFactory;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.vacuum.VELocalState;
import aima.core.environment.vacuum.VEWorldState;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.search.api.ActionsFunction;
import aima.core.search.api.GoalTestPredicate;
import aima.core.search.api.Problem;
import aima.core.search.api.ResultFunction;
import aima.core.search.basic.support.BasicProblem;
import aima.core.util.datastructure.Pair;

public class ProblemFactory {

	public static Problem<GoAction, InState> getSimplifiedRoadMapOfPartOfRomaniaProblem(String initialState,
																						final String... goalLocations) {
		final SimplifiedRoadMapOfPartOfRomania simplifidRoadMapOfPartOfRomania = new SimplifiedRoadMapOfPartOfRomania();
		final Set<String> locationSet = new HashSet<>(simplifidRoadMapOfPartOfRomania.getLocations());
		if (!locationSet.contains(initialState)) {
			throw new IllegalArgumentException(
					"Initial State " + initialState + " is not a member of the state space.");
		}
		for (String location : goalLocations) {
			if (!locationSet.contains(location)) {
				throw new IllegalArgumentException(
						"Goal location " + location + " is not a member of the state space.");
			}
		}

		return new BasicProblem<>(new InState(initialState),
				Map2DFunctionFactory.getActionsFunction(simplifidRoadMapOfPartOfRomania),
				Map2DFunctionFactory.getResultFunction(simplifidRoadMapOfPartOfRomania),
				Map2DFunctionFactory.getGoalTestPredicate(simplifidRoadMapOfPartOfRomania, goalLocations),
				Map2DFunctionFactory.getStepCostFunction(simplifidRoadMapOfPartOfRomania));
	}

	public static Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> getSimpleBidirectionalSearchProblem(String initialState, final String goalLocation) {
		final SimplifiedRoadMapOfPartOfRomania simplifidRoadMapOfPartOfRomania = new SimplifiedRoadMapOfPartOfRomania();
		final Set<String> locationSet = new HashSet<>(simplifidRoadMapOfPartOfRomania.getLocations());
		if (!locationSet.contains(initialState)) {
			throw new IllegalArgumentException(
					"Initial State " + initialState + " is not a member of the state space.");
		}
		return new Pair<>(new BasicProblem<>(new InState(initialState),
				Map2DFunctionFactory.getActionsFunction(simplifidRoadMapOfPartOfRomania),
				Map2DFunctionFactory.getResultFunction(simplifidRoadMapOfPartOfRomania),
				Map2DFunctionFactory.getGoalTestPredicate(simplifidRoadMapOfPartOfRomania, goalLocation),
				Map2DFunctionFactory.getStepCostFunction(simplifidRoadMapOfPartOfRomania)),
				new BasicProblem<>(new InState(goalLocation),
						Map2DFunctionFactory.getActionsFunction(simplifidRoadMapOfPartOfRomania),
						Map2DFunctionFactory.getResultFunction(simplifidRoadMapOfPartOfRomania),
						Map2DFunctionFactory.getGoalTestPredicate(simplifidRoadMapOfPartOfRomania, goalLocation),
						Map2DFunctionFactory.getStepCostFunction(simplifidRoadMapOfPartOfRomania)));
	}

	public static Problem<String, VEWorldState> getSimpleVacuumWorldProblem(String inInitialLocation,
			final VELocalState... leftToRightLocalStates) {
		// These actions are legal in all states
		final List<String> allActions = new ArrayList<>();
		allActions.add(VacuumEnvironment.ACTION_LEFT);
		allActions.add(VacuumEnvironment.ACTION_SUCK);
		allActions.add(VacuumEnvironment.ACTION_RIGHT);
		ActionsFunction<String, VEWorldState> actionsFn = inState -> {
			return allActions;
		};

		ResultFunction<String, VEWorldState> resultFn = (worldState, action) -> worldState.performDeterministic(action);

		GoalTestPredicate<VEWorldState> goalTestPredicate = worldState -> {
			return worldState.isAllClean();
		};

		return new BasicProblem<>(new VEWorldState(inInitialLocation, leftToRightLocalStates), actionsFn, resultFn,
				goalTestPredicate);
	}

	public static List<String> getSimpleBinaryTreeStates() {
		return Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z");
	}

	public static Problem<String, String> getSimpleBinaryTreeProblem(String initialState, String... goalStates) {
		return getSimpleBinaryTreeProblem(initialState, getSimpleBinaryTreeStates(), goalStates);
	}

	public static Problem<String, String> getSimpleBinaryTreeProblem(String initialState, List<String> states,
			final String... goalStates) {
		Set<String> stateSet = new LinkedHashSet<>(states);
		if (stateSet.size() != states.size()) {
			throw new IllegalArgumentException("States provided contain duplicates");
		}
		if (!stateSet.contains(initialState)) {
			throw new IllegalArgumentException(
					"Initial State " + initialState + " is not a member of the state space.");
		}
		for (String goalState : goalStates) {
			if (!stateSet.contains(goalState)) {
				throw new IllegalArgumentException("Goal State " + goalState + " is not a member of the state space.");
			}
		}
		final Map<String, List<String>> simpleBinaryTreeStateSpace = new HashMap<>();
		Queue<String> children = new LinkedList<>(states.subList(1, states.size()));
		for (int i = 0; i < states.size(); i++) {
			String node = states.get(i);
			List<String> targets = new ArrayList<>();
			for (int c = 0; c < 2; c++) {
				if (!children.isEmpty()) {
					targets.add(children.remove());
				}
			}
			simpleBinaryTreeStateSpace.put(node, targets);
		}

		ActionsFunction<String, String> actionsFn = state -> {
			if (simpleBinaryTreeStateSpace.containsKey(state)) {
				return new ArrayList<>(simpleBinaryTreeStateSpace.get(state));
			}
			return Collections.emptyList();
		};

		ResultFunction<String, String> resultFn = (state, action) -> action;

		GoalTestPredicate<String> goalTestPredicate = state -> {
			for (String goalState : goalStates) {
				if (goalState.equals(state)) {
					return true;
				}
			}
			return false;
		};

		return new BasicProblem<>(initialState, actionsFn, resultFn, goalTestPredicate);
	}

	public static final int[] DEFAULT_DISCRETE_FUNCTION_DEPENDENT_VALUES = new int[] { // ->
			2, // y = f(x=0)
			3, // y = f(x=1)
			4, // y = f(x=2)
			5, // y = f(x=3) - start of shoulder
			5, // y = f(x=4) - shoulder
			5, // y = f(x=5) - end of shoulder
			6, // y = f(x=6)
			7, // y = f(x=7)
			8, // y = f(x=8)
			9, // y = f(x=9) - global maximum
			7, // y = f(x=10)
			4, // y = f(x=11)
			2, // y = f(x=12)
			1, // y = f(x=13) - global minimum
			2, // y = f(x=14)
			3, // y = f(x=15)
			4, // y = f(x=16) - local maximum
			3, // y = f(x=17)
			2, // y = f(x=18) - local minimum
			3, // y = f(x=19) - start 'flat' local maximum
			3, // y = f(x=20) - 'flat' local maximum
			3, // y = f(x=21) - end 'flat' local maximum
			2, // y = f(x=22)
			1, // y = f(x=23) - global minimum
			1, // y = f(x=24) - global minimum
	};
	public static final int DEFAULT_DISCRETE_FUNCTION_SHOULDER_VALUE = 5;
	public static final int DEFAULT_DISCRETE_FUNCTION_GLOBAL_MAXIMIM = 9;
	public static final int DEFAULT_DISCRETE_FUNCTION_GLOBAL_MINIMUM = 1;
	public static final int DEFAULT_DISCRETE_FUNCTION_LOCAL_MAXIMUM_VALUE = 4;
	public static final int DEFAULT_DISCRETE_FUNCTION_FLAT_LOCAL_MAXIMUM_VALUE = 3;

	/**
	 * A simple discrete function problem based on Figure 4.1 from AIMA3e
	 * 
	 * @param xInitialStateValue
	 *            x's initial value.
	 * @param goalIsGlobalMaximum
	 *            true if goal is global maximum, false if it is global minimum.
	 * @return a simple discrete function problem based on Figure 4.1 from
	 *         AIMA3e.
	 */
	public static Problem<String, Pair<Integer, Integer>> getDefaultSimpleDiscreteFunctionProblem(
			int xInitialStateValue, boolean goalIsGlobalMaximum) {
		return getSimpleDiscreteFunctionProblem(xInitialStateValue, DEFAULT_DISCRETE_FUNCTION_DEPENDENT_VALUES,
				goalIsGlobalMaximum);
	}

	public static Problem<String, Pair<Integer, Integer>> getSimpleDiscreteFunctionProblem(int xInitialStateValue,
			final int[] dependentValues, boolean goalIsGlobalMaximum) {
		if (xInitialStateValue < 0 || xInitialStateValue >= dependentValues.length) {
			throw new IllegalArgumentException(
					"x initial value is outside legal range of [0, " + (dependentValues.length - 1) + "]");
		}
		final List<Pair<Integer, Integer>> x_y_functionTable = new ArrayList<>();
		for (int x = 0; x < dependentValues.length; x++) {
			x_y_functionTable.add(new Pair<>(x, dependentValues[x]));
		}
		final Pair<Integer, Integer> initialState = x_y_functionTable.get(xInitialStateValue);

		final Integer goalValue;
		if (goalIsGlobalMaximum) {
			goalValue = IntStream.of(dependentValues).max().getAsInt();
		}
		// else foal is global minimum
		else {
			goalValue = IntStream.of(dependentValues).min().getAsInt();
		}
		// Note: we intentionally allow actions that won't have an effect
		// at the range boundaries of the function to ensure calling algorithms
		// can properly handle the case where the current state would be
		// considered a neighbor
		// (i.e. the result of an action leaves us in the same state)
		final List<String> actions = new ArrayList<>();
		actions.add("Left");
		actions.add("Right");
		ActionsFunction<String, Pair<Integer, Integer>> actionsFn = inState -> {
			return actions;
		};

		ResultFunction<String, Pair<Integer, Integer>> resultFn = (x_y, action) -> {
			if ("Left".equals(action)) {
				if (x_y.getFirst().equals(0)) {
					return x_y;
				} else {
					return x_y_functionTable.get(x_y.getFirst() - 1);
				}
			}
			// else its 'Right'
			else {
				if (x_y.getFirst().equals(x_y_functionTable.size() - 1)) {
					return x_y;
				} else {
					return x_y_functionTable.get(x_y.getFirst() + 1);
				}
			}
		};

		GoalTestPredicate<Pair<Integer, Integer>> goalTestPredicate = x_y -> {
			return x_y.getSecond().equals(goalValue);
		};

		return new BasicProblem<>(initialState, actionsFn, resultFn, goalTestPredicate);
	}
}