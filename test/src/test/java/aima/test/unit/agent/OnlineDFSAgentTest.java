package aima.test.unit.agent;

import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.basic.OnlineDFSAgent;
import aima.core.environment.map2d.ExtendableMap2D;
import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2D;
import aima.core.search.api.ActionsFunction;
import aima.core.search.api.GoalTestPredicate;
import aima.core.search.api.OnlineSearchProblem;
import aima.core.search.api.StepCostFunction;
import aima.core.search.basic.support.BasicProblem;

/*
 * @author Anurag Rai
 * @author Ciaran O'Reilly
 */
public class OnlineDFSAgentTest {
	/**
	 * The state space that is represented by map1.
	 * 
	 * <pre>
	 *    5      4      4
	 * A ---- B ---- D ---- F
	 * |      |		 |
	 * |6     |7     |8
	 * C      E      G
	 * </pre>
	 */
	Map2D map1 = new ExtendableMap2D() {
		{
			addBidirectionalLink("A", "B", 5.0);
			addBidirectionalLink("A", "C", 6.0);
			addBidirectionalLink("B", "D", 4.0);
			addBidirectionalLink("B", "E", 7.0);
			addBidirectionalLink("D", "F", 4.0);
			addBidirectionalLink("D", "G", 8.0);
		}
	};

	ActionsFunction<GoAction, InState> actionsFn1 = inState -> {
		return map1.getLocationsLinkedTo(inState.getLocation()).stream().map(GoAction::new)
				.collect(Collectors.toList());
	};

	GoalTestPredicate<InState> goalAPredicate = inState -> {
		return "A".equals(inState.getLocation());
	};

	GoalTestPredicate<InState> goalGPredicate = inState -> {
		return "G".equals(inState.getLocation());
	};

	StepCostFunction<GoAction, InState> stepCostFn1 = (s, a, sPrime) -> {
		return map1.getDistance(s.getLocation(), sPrime.getLocation());
	};
	Function<Integer, InState> perceptToStateFn1 = percept -> {
		return new InState(new String(new char[] { (char) ('A' + percept) }));
	};
	Function<InState, Integer> stateToPerceptFn1 = inState -> {
		return inState.getLocation().charAt(0) - 'A';
	};

	@Test
	public void testAlreadyAtGoal() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(actionsFn1, goalAPredicate, stepCostFn1);

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, perceptToStateFn1);

		Assert.assertEquals(odfsa.perceive(0), (GoAction) null);
	}

	@Test
	public void testNormalSearch() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(actionsFn1, goalGPredicate, stepCostFn1);

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, perceptToStateFn1);

		// Start State is 0 == "A"
		int percept = 0;
		GoAction action;
		StringJoiner result = new StringJoiner(" ");
		while (true) {
			action = odfsa.perceive(percept);
			if (action == null) {
				result.add("NoOp");
				break;
			}
			percept = stateToPerceptFn1.apply(new InState(action.getGoTo()));
			result.add(action.toString());
		}

		Assert.assertEquals(
				"Go(B) Go(A) Go(C) Go(A) Go(C) Go(A) Go(B) Go(D) Go(B) Go(E) Go(B) Go(E) Go(B) Go(D) Go(F) Go(D) Go(G) NoOp",
				result.toString());
	}

	/**
	 * Graph: A ---- B
	 */
	Map2D map2 = new ExtendableMap2D() {
		{
			addBidirectionalLink("A", "B", 1.0);
		}
	};

	ActionsFunction<GoAction, InState> actionsFn2 = inState -> {
		return map2.getLocationsLinkedTo(inState.getLocation()).stream().map(GoAction::new)
				.collect(Collectors.toList());
	};

	@Test
	public void testNoPath() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(actionsFn2, state -> false,
				(s, a, sPrime) -> 1.0);

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, perceptToStateFn1);

		// Start State is 0 == "A"
		int percept = 0;
		GoAction action;
		StringJoiner result = new StringJoiner(" ");
		while (true) {
			action = odfsa.perceive(percept);
			if (action == null) {
				result.add("NoOp");
				break;
			}
			percept = stateToPerceptFn1.apply(new InState(action.getGoTo()));
			result.add(action.toString());
		}

		Assert.assertEquals("Go(B) Go(A) Go(B) Go(A) NoOp", result.toString());
	}

	Map2D mazeFigure4_19 = new ExtendableMap2D() {
		{			
			addBidirectionalLink("1,1", "1,2", 1.0);
			addBidirectionalLink("1,1", "2,1", 1.0);
			addBidirectionalLink("1,3", "2,3", 1.0);
			addBidirectionalLink("2,1", "1,1", 1.0);
			addBidirectionalLink("2,1", "2,2", 1.0);
			addBidirectionalLink("2,1", "3,1", 1.0);
			addBidirectionalLink("2,2", "2,3", 1.0);
			addBidirectionalLink("3,1", "3,2", 1.0);
			addBidirectionalLink("3,2", "3,3", 1.0);
		}
	};
	ActionsFunction<GoAction, InState> actionsFnFigure4_19 = inState -> {
		return mazeFigure4_19.getLocationsLinkedTo(inState.getLocation()).stream().map(GoAction::new)
				.collect(Collectors.toList());
	};
	Function<Integer, InState> perceptToStateFnFigure4_19 = percept -> {
		return new InState(new String(new char[] { (char) ('1' + percept / 3), ',', (char) ('1' + percept % 3) }));
	};
	Function<InState, Integer> stateToPerceptFnFigure4_19 = inState -> {
		return ((inState.getLocation().charAt(0) - '1') * 3) + (inState.getLocation().charAt(2) - '1');
	};

	@Test
	public void testAIMA3eFig4_19() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(actionsFnFigure4_19,
				state -> "3,3".equals(state.getLocation()), (s, a, sPrime) -> 1.0);

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, perceptToStateFnFigure4_19);

		// Start State is 0 == "1,1"
		int percept = 0;
		GoAction action;
		StringJoiner result = new StringJoiner(" ");
		while (true) {
			action = odfsa.perceive(percept);
			if (action == null) {
				result.add("NoOp");
				break;
			}
			percept = stateToPerceptFnFigure4_19.apply(new InState(action.getGoTo()));
			result.add(action.toString());
		}

		Assert.assertEquals(
				"Go(1,2) Go(1,1) Go(2,1) Go(1,1) Go(2,1) Go(2,2) Go(2,1) Go(3,1) Go(2,1) Go(3,1) Go(3,2) Go(3,1) Go(3,2) Go(3,3) NoOp",
				result.toString());
	}
}