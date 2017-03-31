package aima.test.unit.search.adversarial;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import aima.core.environment.map2d.ExtendableMap2D;
import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2D;
import aima.core.environment.map2d.Map2DFunctionFactory;
import aima.core.search.api.Game;
import aima.core.search.api.PlayerFunction;
import aima.core.search.api.SearchForAdversarialActionFunction;
import aima.core.search.api.TerminalStateUtilityFunction;
import aima.core.search.api.TerminalTestPredicate;
import aima.core.search.basic.adversarial.AlphaBetaSearch;
import aima.core.search.basic.adversarial.MinimaxDecision;
import aima.core.search.basic.support.BasicGame;
import aima.extra.search.adversarial.IterativeDeepeningAlphaBetaSearch;

@RunWith(Parameterized.class)
public class SearchForAdversarialActionFunctionTest {
	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> implementations() {
		return Arrays.asList(new Object[][]{{"MinimaxDecision"}, {"AlphaBetaSearch"}, {"IterativeDeepeningAlphaBetaSearch"}});
	}

	@Parameter
	public String searchFunctionName;

	public <S, A, P> A searchForAdversarialAction(Game<S, A, P> game, Integer... params) {
		SearchForAdversarialActionFunction<S, A> searchFn;
		if ("MinimaxDecision".equals(searchFunctionName)) {
			searchFn = new MinimaxDecision<>(game);
		} else if ("AlphaBetaSearch".equals(searchFunctionName)) {
			searchFn = new AlphaBetaSearch<>(game);
		} else {
			searchFn = new IterativeDeepeningAlphaBetaSearch<>(game, params[0], params[1], params[2]);
		}
		return searchFn.apply(game.initialState());
	}

	Map2D aima3eFig5_2 = new ExtendableMap2D() {
		{
			addUnidirectionalLink("A", "B", 1.0);
			addUnidirectionalLink("A", "C", 1.0);
			addUnidirectionalLink("A", "D", 1.0);
			addUnidirectionalLink("B", "E", 1.0);
			addUnidirectionalLink("B", "F", 1.0);
			addUnidirectionalLink("B", "G", 1.0);
			addUnidirectionalLink("C", "H", 1.0);
			addUnidirectionalLink("C", "I", 1.0);
			addUnidirectionalLink("C", "J", 1.0);
			addUnidirectionalLink("D", "K", 1.0);
			addUnidirectionalLink("D", "L", 1.0);
			addUnidirectionalLink("D", "M", 1.0);
		}
	};

	PlayerFunction<InState, String> aima3eFig5_2PlayerFn = inState -> {
		switch (inState.getLocation()) {
		case "B":
		case "C":
		case "D":
			return "MIN";
		default:
			return "MAX";
		}
	};

	TerminalTestPredicate<InState> aima3eFig5_2TerminalTestPredicate = inState -> inState.getLocation().charAt(0) > 'D';

	TerminalStateUtilityFunction<InState, String> aima3eFig5_2UtilityFn = (inState, player) -> {
		switch (inState.getLocation()) {
		// B
		case "E":
			return 3;
		case "F":
			return 12;
		case "G":
			return 8;
		// C
		case "H":
			return 2;
		case "I":
			return 4;
		case "J":
			return 6;
		// D
		case "K":
			return 14;
		case "L":
			return 5;
		case "M":
			return 2;
		default:
			throw new IllegalArgumentException("State " + inState.getLocation() + " unexpected.");
		}
	};

	@Test
	public void testAIMA3eFig5_2() {
		Game<InState, GoAction, String> game = new BasicGame<>(() -> new InState("A"), aima3eFig5_2PlayerFn,
				Map2DFunctionFactory.getActionsFunction(aima3eFig5_2),
				Map2DFunctionFactory.getResultFunction(aima3eFig5_2), aima3eFig5_2TerminalTestPredicate,
				aima3eFig5_2UtilityFn);

		Assert.assertEquals(new GoAction("B"), searchForAdversarialAction(game, 100, 0, 200));
	}
}
