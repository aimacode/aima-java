/*
 * Created on Feb 15, 2005
 *
 */
package aima.games;

import aima.basic.Agent;

/**
 * @author Ravi Mohan
 * 
 */
public class GameAgent extends Agent {
	private Game game;

	public GameAgent(Game g) {
		this.game = g;
	}

	public void makeMiniMaxMove() {
		game.makeMiniMaxMove();
	}

	public void makeAlphaBetaMove() {
		game.makeAlphaBetaMove();
	}

}