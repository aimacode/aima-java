package aima.test.core.unit.search.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import aima.core.search.api.Problem;
import aima.core.search.basic.support.BasicProblem;

public class ProblemFactory {

	public static List<String> getSimpleBinaryTreeStates() {
		return Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "0", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z");
	}
	
	public static Problem<String, String> getSimpleBinaryTreeProblem(String initialState, String goalState) {
		return  getSimpleBinaryTreeProblem(initialState, goalState, getSimpleBinaryTreeStates());
	}

	public static Problem<String, String> getSimpleBinaryTreeProblem(String initialState, String goalState,
			List<String> states) {
		Set<String> stateSet = new LinkedHashSet<>(states);
		if (stateSet.size() != states.size()) {
			throw new IllegalArgumentException("States provided contain duplicates");
		}
		if (!stateSet.contains(initialState)) {
			throw new IllegalArgumentException("Initial State "+initialState+" is not a member of the state space.");
		}
		if (!stateSet.contains(goalState)) {
			throw new IllegalArgumentException("Goal State "+goalState+" is not a member of the state space.");
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

		Function<String, Set<String>> simpleBinaryTreeActionsFn = state -> {
			if (simpleBinaryTreeStateSpace.containsKey(state)) {
				return new LinkedHashSet<>(simpleBinaryTreeStateSpace.get(state));
			}
			return Collections.emptySet();
		};
		
	    BiFunction<String, String, String> goActionResultFn = (state, action) -> action;

	    return new BasicProblem<>(initialState,
                simpleBinaryTreeActionsFn,
                goActionResultFn,
                goalState::equals);
	}
}