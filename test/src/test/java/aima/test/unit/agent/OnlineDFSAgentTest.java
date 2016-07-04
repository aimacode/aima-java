package aima.test.unit.agent;

import java.util.StringJoiner;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.basic.OnlineDFSAgent;
import aima.core.environment.map2d.ExtendableMap2D;
import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2D;
import aima.core.environment.map2d.Map2DFunctionFactory;
import aima.core.search.api.OnlineSearchProblem;
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
	Map2D mapAtoG = new ExtendableMap2D() {
		{
			addBidirectionalLink("A", "B", 5.0);
			addBidirectionalLink("A", "C", 6.0);
			addBidirectionalLink("B", "D", 4.0);
			addBidirectionalLink("B", "E", 7.0);
			addBidirectionalLink("D", "F", 4.0);
			addBidirectionalLink("D", "G", 8.0);
		}
	};

	Function<Integer, InState> alphabetPerceptToStateFn = percept -> {
		return new InState(new String(new char[] { (char) ('A' + percept) }));
	};
	Function<InState, Integer> alphabetStateToPerceptFn = inState -> {
		return inState.getLocation().charAt(0) - 'A';
	};

	@Test
	public void testAlreadyAtGoal() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(
				Map2DFunctionFactory.getActionsFunction(mapAtoG), inState -> "A".equals(inState.getLocation()),
				Map2DFunctionFactory.getStepCostFunction(mapAtoG));

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, alphabetPerceptToStateFn);

		Assert.assertEquals(odfsa.perceive(0), (GoAction) null);
	}

	@Test
	public void testNormalSearch() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(
				Map2DFunctionFactory.getActionsFunction(mapAtoG), inState -> "G".equals(inState.getLocation()),
				Map2DFunctionFactory.getStepCostFunction(mapAtoG));

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, alphabetPerceptToStateFn);

		testExpectedActionSequence(odfsa, 0 /* i.e. 'A' */, alphabetStateToPerceptFn,
				"Go(B) Go(A) Go(C) Go(A) Go(C) Go(A) Go(B) Go(D) Go(B) Go(E) Go(B) Go(E) Go(B) Go(D) Go(F) Go(D) Go(G) NoOp");
	}

	/**
	 * Graph: A ---- B
	 */
	Map2D mapAtoB = new ExtendableMap2D() {
		{
			addBidirectionalLink("A", "B", 1.0);
		}
	};

	@Test
	public void testNoPath() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(
				Map2DFunctionFactory.getActionsFunction(mapAtoB), state -> false,
				Map2DFunctionFactory.getStepCostFunction(mapAtoB));

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, alphabetPerceptToStateFn);

		testExpectedActionSequence(odfsa, 0 /* i.e. 'A' */, alphabetStateToPerceptFn, "Go(B) Go(A) Go(B) Go(A) NoOp");
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
	Function<Integer, InState> xyPerceptToStateFn = percept -> {
		return new InState(new String(new char[] { (char) ('1' + percept / 3), ',', (char) ('1' + percept % 3) }));
	};
	Function<InState, Integer> xyStateToPerceptFn = inState -> {
		return ((inState.getLocation().charAt(0) - '1') * 3) + (inState.getLocation().charAt(2) - '1');
	};

	@Test
	public void testAIMA3eFig4_19() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(
				Map2DFunctionFactory.getActionsFunction(mazeFigure4_19), inState -> "3,3".equals(inState.getLocation()),
				Map2DFunctionFactory.getStepCostFunction(mazeFigure4_19));

		OnlineDFSAgent<GoAction, Integer, InState> odfsa = new OnlineDFSAgent<>(osp, xyPerceptToStateFn);

		testExpectedActionSequence(odfsa, 0 /* i.e. [1,1] */, xyStateToPerceptFn,
				"Go(1,2) Go(1,1) Go(2,1) Go(1,1) Go(2,1) Go(2,2) Go(2,1) Go(3,1) Go(2,1) Go(3,1) Go(3,2) Go(3,1) Go(3,2) Go(3,3) NoOp");
	}

	public void testExpectedActionSequence(OnlineDFSAgent<GoAction, Integer, InState> odfsa, int initialPercept,
			Function<InState, Integer> stateToPerceptFn, String expected) {
		int percept = initialPercept;
		GoAction action;
		StringJoiner result = new StringJoiner(" ");
		while (true) {
			action = odfsa.perceive(percept);
			if (action == null) {
				result.add("NoOp");
				break;
			}
			percept = stateToPerceptFn.apply(new InState(action.getGoTo()));
			result.add(action.toString());
		}

		Assert.assertEquals(expected, result.toString());
	}
}