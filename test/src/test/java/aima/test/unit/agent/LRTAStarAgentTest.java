package aima.test.unit.agent;

import java.util.StringJoiner;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.basic.LRTAStarAgent;
import aima.core.environment.map2d.ExtendableMap2D;
import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2D;
import aima.core.environment.map2d.Map2DFunctionFactory;
import aima.core.search.api.OnlineSearchProblem;
import aima.core.search.basic.support.BasicProblem;

public class LRTAStarAgentTest {
	Map2D mapAtoF= new ExtendableMap2D() {
		{
			addBidirectionalLink("A", "B", 1.0);
			addBidirectionalLink("B", "C", 1.0);
			addBidirectionalLink("C", "D", 1.0);
			addBidirectionalLink("D", "E", 1.0);
			addBidirectionalLink("E", "F", 1.0);
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
				Map2DFunctionFactory.getActionsFunction(mapAtoF), inState -> "A".equals(inState.getLocation()),
				Map2DFunctionFactory.getStepCostFunction(mapAtoF));

		LRTAStarAgent<GoAction, Integer, InState> lrtasa = new LRTAStarAgent<>(osp, inState -> 1.0, alphabetPerceptToStateFn);

		Assert.assertEquals(lrtasa.perceive(0), (GoAction) null);
	}
	
	@Test
	public void testNormalSearch() {
		OnlineSearchProblem<GoAction, InState> osp = new BasicProblem<>(
				Map2DFunctionFactory.getActionsFunction(mapAtoF), inState -> "F".equals(inState.getLocation()),
				Map2DFunctionFactory.getStepCostFunction(mapAtoF));

		LRTAStarAgent<GoAction, Integer, InState> lrtasa = new LRTAStarAgent<>(osp, inState -> 'F' - inState.getLocation().charAt(0), alphabetPerceptToStateFn);

		testExpectedActionSequence(lrtasa, 0 /* i.e. 'A' */, alphabetStateToPerceptFn,
				"Go(B) Go(A) Go(B) Go(C) Go(B) Go(C) Go(D) Go(C) Go(D) Go(E) Go(D) Go(E) Go(F) NoOp");
		
		osp = new BasicProblem<>(
				Map2DFunctionFactory.getActionsFunction(mapAtoF), inState -> "A".equals(inState.getLocation()),
				Map2DFunctionFactory.getStepCostFunction(mapAtoF));
		
		lrtasa = new LRTAStarAgent<>(osp, inState -> inState.getLocation().charAt(0) - 'A', alphabetPerceptToStateFn);
		
		testExpectedActionSequence(lrtasa, 5 /* i.e. 'F' */, alphabetStateToPerceptFn,
				"Go(E) Go(D) Go(C) Go(B) Go(A) NoOp");
	}
	
	public void testExpectedActionSequence(LRTAStarAgent<GoAction, Integer, InState> lrtasa, int initialPercept,
			Function<InState, Integer> stateToPerceptFn, String expected) {
		int percept = initialPercept;
		GoAction action;
		StringJoiner result = new StringJoiner(" ");
		while (true) {
			action = lrtasa.perceive(percept);
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
