package aima.core.search.adversarial;

import aima.core.agent.impl.AbstractAgent;

/**
 * @param <MOVE>
 *            the type of moves that can be made within the game the agent is
 *            playing.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class GameAgent<MOVE> extends AbstractAgent {
	private Game<MOVE> game;

	/**
	 * Constructs a game agent with the specified game to play.
	 * 
	 * @param g
	 *            a game for the game agent to play.
	 */
	public GameAgent(Game<MOVE> g) {
		this.game = g;
	}

	/**
	 * Causes the game agent to make a move from the minimax decision, which
	 * maximizes the utility under the assumption that the opponent will play
	 * perfectly to minimize it.
	 */
	public void makeMiniMaxMove() {
		game.makeMiniMaxMove();
	}

	/**
	 * Causes the game agent to make a move from an alpha beta search, which
	 * does the same computation as a normal minimax, but also prunes the search
	 * tree.
	 */
	public void makeAlphaBetaMove() {
		game.makeAlphaBetaMove();
	}

}