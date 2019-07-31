package aima.test.core.unit.search.adversarial;

import aima.core.environment.twoply.TwoPlyGame;
import aima.core.environment.twoply.TwoPlyGameState;
import aima.core.search.adversarial.AlphaBetaSearch;
import aima.core.search.adversarial.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AlphaBetaSearchTest {
	Game game;
	AlphaBetaSearch alphaBetaSearch;
	
	@Before
	public void setup() {
		this.game = new TwoPlyGame();
		this.alphaBetaSearch = new AlphaBetaSearch(game);
	}
	
	@Test
	public void testUtilities() {
		TwoPlyGameState state = new TwoPlyGameState("E");
		Assert.assertEquals(3, game.getUtility(state, game.getPlayer(state)), 0);
		state = new TwoPlyGameState("I");
		Assert.assertEquals(4, game.getUtility(state, game.getPlayer(state)), 0);
		state = new TwoPlyGameState("K");
		Assert.assertEquals(14, game.getUtility(state, game.getPlayer(state)), 0);
		
	}
	
	@Test
	public void testMinimaxDecision() {
		Assert.assertEquals("A", game.getInitialState().toString());
		Assert.assertEquals("MAX", game.getPlayer(game.getInitialState()));
		Assert.assertEquals("Action[name=moveTo, location=B]", alphaBetaSearch.makeDecision(game.getInitialState()).toString());
	}
}
