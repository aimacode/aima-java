package aima.core.search.adversarial;

/**
 * @author Anurag Rai
 */

import aima.core.api.search.adversarial.AlphaBetaSearch;
import aima.core.api.search.adversarial.Game;

public class BasicAdversarialSearch<A,S> implements AlphaBetaSearch<A, S> {

	Game<A,S> game;
		
	public BasicAdversarialSearch(Game<A,S> game) {
		setGame(game);
	}
	
	public void setGame(Game<A,S> game) {
		this.game = game;
	}
	
	public Game<A,S> getGame() {
		return game;
	}
}
