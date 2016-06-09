package aima.test.core.unit.search.online;

/*
 * @author Anurag Rai
 * 
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import aima.core.api.search.online.StepCostFunction;
import aima.core.search.online.BasicOnlineDFSAgent;
import aima.core.search.online.OnlineSearchProblem;

public class OnlineDFSAgentTest {

	//The Action that is used
	class GoAction {
		String goTo;

		GoAction(String goTo) {
			this.goTo = goTo;
		}

		public String name() {
			return "Go(" + goTo + ")";
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof GoAction) {
				return this.name().equals(((GoAction) obj).name());
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return name().hashCode();
		}
	}

	/**The graph structure that is implemented
	 *    5      4      4
	 * A ---- B ---- D ---- F
	 * |      |		 |
	 * |6     |7     |8
	 * C      E      G
	 */

	Map<String, List<String>> map = new HashMap<String, List<String>>() {{
		put("A", Arrays.asList("B","C"));
		put("B", Arrays.asList("A","D","E"));
		put("C", Arrays.asList("A"));
		put("D", Arrays.asList("B","F","G"));
		put("E", Arrays.asList("B"));
		put("F", Arrays.asList("D"));
		put("G", Arrays.asList("D"));
	}};

	//The Action Function
	Function<String, Set<GoAction>> actionsFn = state -> {
		if (map.containsKey(state)) {
			return new LinkedHashSet<>(map.get(state).stream().map(GoAction::new).collect(Collectors.toList()));
		}
		return Collections.emptySet();
	};

	//Arbitrary Goal-State Function
	Predicate<String> goalTestFn = state -> { 
		if (state.equals("A")) { 
			return true;
		}
		return false;
	};

	//Arbitrary Step Cost function
	StepCostFunction<GoAction, String> stepCostFn = (state1, action, state2) -> {
		if ( (state1.equals("A") && state2.equals("B")) || (state1.equals("B") && state2.equals("A")) ) {
			return 5.0;
		}
		else if ( (state1.equals("A") && state2.equals("C")) || (state1.equals("A") && state2.equals("C")) ) {
			return 6.0;
		}
		else if ( (state1.equals("B") && state2.equals("D")) || (state1.equals("D") && state2.equals("B")) ) {
			return 4.0;
		}
		else if ( (state1.equals("B") && state2.equals("E")) || (state1.equals("E") && state2.equals("B")) ) {
			return 7.0;
		}
		else if ( (state1.equals("D") && state2.equals("F")) || (state1.equals("F") && state2.equals("D")) ) {
			return 4.0;
		}
		else if ( (state1.equals("D") && state2.equals("G")) || (state1.equals("G") && state2.equals("D")) ) {
			return 8.0;
		}
		return 1.0;
	};

	//Arbitrary Percept to State Function
	Function<Integer,String> perceptToStateFn = percept -> {
		if ( percept.intValue() == 0 ) { return "A"; }
		else if ( percept.intValue() == 1 ) { return "B"; }
		else if ( percept.intValue() == 2 ) { return "C"; }
		else if ( percept.intValue() == 3 ) { return "D"; }
		else if ( percept.intValue() == 4 ) { return "E"; }
		else if ( percept.intValue() == 5 ) { return "F"; }
		else if ( percept.intValue() == 6 ) { return "G"; }
		return "";
	};


	@Test
	public void testClass() {
		OnlineSearchProblem<GoAction, String> onlineSearchProblem = new OnlineSearchProblem<GoAction, String>(actionsFn, goalTestFn, stepCostFn);

		BasicOnlineDFSAgent<GoAction, Integer, String> onlineDFSAgent = new BasicOnlineDFSAgent<GoAction, Integer, String>(onlineSearchProblem, perceptToStateFn);

		Assert.assertEquals(onlineDFSAgent.getProblem(),onlineSearchProblem);
		Assert.assertEquals(onlineDFSAgent.getPerceptToStateFunction(),perceptToStateFn);

		Assert.assertEquals(onlineDFSAgent.getPerceptToStateFunction().apply(1),"B");
		Assert.assertNotEquals(onlineDFSAgent.getPerceptToStateFunction().apply(0),"B");
	}

	@Test
	public void testAlreadyAtGoal() {
		OnlineSearchProblem<GoAction, String> onlineSearchProblem = new OnlineSearchProblem<GoAction, String>(actionsFn, goalTestFn, stepCostFn);
		
		BasicOnlineDFSAgent<GoAction, Integer, String> onlineDFSAgent1 = new BasicOnlineDFSAgent<GoAction, Integer, String>(onlineSearchProblem, perceptToStateFn);
		Assert.assertEquals(onlineDFSAgent1.getProblem().isGoalState("A"),true);
		Assert.assertEquals(onlineDFSAgent1.perceive(0), (GoAction) null);
		
		BasicOnlineDFSAgent<GoAction, Integer, String> onlineDFSAgent2 = new BasicOnlineDFSAgent<GoAction, Integer, String>(onlineSearchProblem, perceptToStateFn);
		Assert.assertNotEquals(onlineDFSAgent2.getProblem().isGoalState("B"),true);
		Assert.assertNotEquals(onlineDFSAgent2.perceive(1), (GoAction) null);

	}

	@Test
	public void testNormalSearch() {
		//Defining "G" as the goal state
		Predicate<String> newGoalTestFn = state -> { 
			if (state.equals("G")) { 
				return true;
			}
			return false;
		};

		OnlineSearchProblem<GoAction, String> onlineSearchProblem = new OnlineSearchProblem<GoAction, String>(actionsFn, newGoalTestFn, stepCostFn);

		BasicOnlineDFSAgent<GoAction, Integer, String> onlineDFSAgent = new BasicOnlineDFSAgent<GoAction, Integer, String>(onlineSearchProblem, perceptToStateFn);

		//Start State is 0 == "A"
		int state = 0;
		GoAction action;
		StringBuffer result = new StringBuffer(100);

		while ( true ) {
			action = onlineDFSAgent.perceive(state);
			if ( action == null ) {
				result.append("NoOp");
				break;
			}
			state = newState(action);
			result.append(action.name());
			result.append(" ");
		}

		Assert.assertEquals(
				"Go(B) Go(A) Go(C) Go(A) Go(C) Go(A) Go(B) Go(D) Go(B) Go(E) Go(B) Go(E) Go(B) Go(D) Go(F) Go(D) Go(G) NoOp",
				result.toString());
	}

	/** Graph:
	 *   A ---- B
	 */
	Map<String, List<String>> newMap = new HashMap<String, List<String>>() {{
		put("A", Arrays.asList("B"));
		put("B", Arrays.asList("A"));
	}};
	//The Action Function
	Function<String, Set<GoAction>> newActionsFn = state -> {
		if (newMap.containsKey(state)) {
			return new LinkedHashSet<>(newMap.get(state).stream().map(GoAction::new).collect(Collectors.toList()));
		}
		return Collections.emptySet();
	};
	/**
	 * Arbitrary Goal-State Function which defines "X" as Goal state
	 */
	Predicate<String> newGoalTestFn = state -> { 
		if (state.equals("X")) { 
			return true;
		}
		return false;
	};

	@Test
	public void testNoPath() {


		/**	
		 * Arbitrary Percept to State Function
		 * Assigns 0 to "A" and 1 to "B"
		 */
		Function<Integer,String> newPerceptToStateFn = percept -> {
			if ( percept.intValue() == 0 ) { return "A"; }
			else if ( percept.intValue() == 1 ) { return "B"; }
			return "";
		};
		/**
		 * Arbitrary Step Cost function. Path Cost is 1.
		 */
		StepCostFunction<GoAction, String> newStepCostFn = (state1, action, state2) -> {
			return 1.0;
		};

		OnlineSearchProblem<GoAction, String> onlineSearchProblem = new OnlineSearchProblem<GoAction, String>(newActionsFn, newGoalTestFn, newStepCostFn);

		BasicOnlineDFSAgent<GoAction, Integer, String> onlineDFSAgent = new BasicOnlineDFSAgent<GoAction, Integer, String>(onlineSearchProblem, newPerceptToStateFn);

		int state = 0;
		GoAction action;
		StringBuffer result = new StringBuffer(100);

		while ( true ) {
			action = onlineDFSAgent.perceive(state);
			if ( action == null ) {
				result.append("NoOp");
				break;
			}
			state = newState(action);
			result.append(action.name());
			result.append(" ");
		}

		Assert.assertEquals( "Go(B) Go(A) Go(B) Go(A) NoOp", result.toString());
	}


	Map<String, List<String>> newExampleMap = new HashMap<String, List<String>>() {{
		put("1,1", Arrays.asList("1,2","2,1"));
		put("1,2", Arrays.asList("1,1"));
		put("1,3", Arrays.asList("2,3"));
		put("2,1", Arrays.asList("1,1","2,2","3,1"));
		put("2,2", Arrays.asList("2,1","2,3"));
		put("2,3", Arrays.asList("2,2","1,3"));
		put("3,1", Arrays.asList("2,1","3,2"));
		put("3,2", Arrays.asList("3,1","3,3"));
		put("3,3", Arrays.asList("3,2"));
	}};
	//The Action Function
	Function<String, Set<GoAction>> newExampleActionsFn = state -> {
		if (newExampleMap.containsKey(state)) {
			return new LinkedHashSet<>(newExampleMap.get(state).stream().map(GoAction::new).collect(Collectors.toList()));
		}
		return Collections.emptySet();
	};
	/**
	 * Arbitrary Goal-State Function which defines "3,3" as Goal state
	 */
	Predicate<String> newExampleGoalTestFn = state -> { 
		if (state.equals("3,3")) { 
			return true;
		}
		return false;
	};

	//Arbitrary Percept to State Function
	Function<Integer,String> newExamplePerceptToStateFn = percept -> {
		if ( percept.intValue() == 0 ) { return "1,1"; }
		else if ( percept.intValue() == 1 ) { return "1,2"; }
		else if ( percept.intValue() == 2 ) { return "1,3"; }
		else if ( percept.intValue() == 3 ) { return "2,1"; }
		else if ( percept.intValue() == 4 ) { return "2,2"; }
		else if ( percept.intValue() == 5 ) { return "2,3"; }
		else if ( percept.intValue() == 6 ) { return "3,1"; }
		else if ( percept.intValue() == 7 ) { return "3,2"; }
		else if ( percept.intValue() == 8 ) { return "3,3"; }
		return "";
	};
	//Arbitrary Step Cost function. Path Cost is 1.
	StepCostFunction<GoAction, String> newExampleStepCostFn = (state1, action, state2) -> { return 1.0; };


	@Test
	public void testAIMA3eFig4_19() {

		OnlineSearchProblem<GoAction, String> onlineSearchProblem = new OnlineSearchProblem<GoAction, String>(newExampleActionsFn, newExampleGoalTestFn, newExampleStepCostFn);

		BasicOnlineDFSAgent<GoAction, Integer, String> onlineDFSAgent = new BasicOnlineDFSAgent<GoAction, Integer, String>(onlineSearchProblem, newExamplePerceptToStateFn);

		int state = 0;
		GoAction action;
		StringBuffer result = new StringBuffer(100);

		while ( true ) {
			action = onlineDFSAgent.perceive(state);
			if ( action == null ) {
				result.append("NoOp");
				break;
			}
			state = newStateExample(action);
			result.append(action.name());
			result.append(" ");
		}

		Assert.assertEquals(
				"Go(1,2) Go(1,1) Go(2,1) Go(1,1) Go(2,1) Go(2,2) Go(2,1) Go(3,1) Go(2,1) Go(3,1) Go(3,2) Go(3,1) Go(3,2) Go(3,3) NoOp",
				result.toString()
				);

	}

	public int newState(GoAction action) {
		if ( action.name().contains("A") ) { return 0; }
		else if ( action.name().contains("B") ) { return 1; }
		else if ( action.name().contains("C") ) { return 2; }
		else if ( action.name().contains("D") ) { return 3; }
		else if ( action.name().contains("E") ) { return 4; }
		else if ( action.name().contains("F") ) { return 5; }
		else if ( action.name().contains("G") ) { return 6; }
		return -1;
	}
	public int newStateExample(GoAction action) {
		if ( action.name().contains("1,1") ) { return 0; }
		else if ( action.name().contains("1,2") ) { return 1; }
		else if ( action.name().contains("1,3") ) { return 2; }
		else if ( action.name().contains("2,1") ) { return 3; }
		else if ( action.name().contains("2,2") ) { return 4; }
		else if ( action.name().contains("2,3") ) { return 5; }
		else if ( action.name().contains("3,1") ) { return 6; }
		else if ( action.name().contains("3,2") ) { return 7; }
		else if ( action.name().contains("3,3") ) { return 8; }
		return -1;
	}
}
