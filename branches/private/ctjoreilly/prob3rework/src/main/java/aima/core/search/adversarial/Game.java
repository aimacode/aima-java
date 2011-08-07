package aima.core.search.adversarial;

import java.util.List;

import aima.core.util.Util;

/**
 * @param <MOVE>
 *            the type of moves that can be made within the game.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public abstract class Game<MOVE> {
	protected GameState initialState = new GameState();

	protected GameState presentState = new GameState();

	protected int level;

	public abstract List<GameState> getSuccessorStates(GameState state);

	public abstract GameState makeMove(GameState state, Object o);

	public abstract int getMiniMaxValue(GameState state);

	public abstract int getAlphaBetaValue(GameState state);

	public boolean hasEnded() {
		return (terminalTest(getState()));
	}

	public int getLevel(GameState g) {
		return (((Integer) g.get("level")).intValue());
	}

	@SuppressWarnings("unchecked")
	public List<MOVE> getMoves(GameState state) {
		return (List<MOVE>) state.get("moves");
	}

	/**
	 * Returns the player to move at the specified game state.
	 * 
	 * @param state
	 *            a game state
	 * 
	 * @return the player to move at the specified game state.
	 */
	public String getPlayerToMove(GameState state) {
		return (String) state.get("player");
	}

	/**
	 * Returns a numeric value for the outcome of the game. In chess, the
	 * outcome is a win, loss, or draw, which we can represent by the value +1,
	 * -1, or 0. Some games have a wider variety of possible outcomes; for
	 * example, the payoffs in backgammon range from + 192 to -192.
	 * 
	 * @param h
	 *            a game state to evaluate.
	 * 
	 * @return a numeric value for the outcome of the game.
	 */
	public int getUtility(GameState h) {
		return ((Integer) h.get("utility")).intValue();
	}

	/**
	 * Returns the present state of the game.
	 * 
	 * @return the present state of the game.
	 */
	public GameState getState() {
		return presentState;
	}

	public int maxValue(GameState state) {
		int v = Integer.MIN_VALUE;
		if (terminalTest(state)) {
			return computeUtility(state);
		} else {
			List<GameState> successorList = getSuccessorStates(state);
			for (int i = 0; i < successorList.size(); i++) {
				GameState successor = successorList.get(i);
				int minimumValueOfSuccessor = minValue(successor);
				if (minimumValueOfSuccessor > v) {
					v = minimumValueOfSuccessor;
					state.put("next", successor);
				}
			}
			return v;
		}

	}

	public int minValue(GameState state) {

		int v = Integer.MAX_VALUE;

		if (terminalTest(state)) {
			return computeUtility(state);

		} else {
			List<GameState> successorList = getSuccessorStates(state);
			for (int i = 0; i < successorList.size(); i++) {
				GameState successor = successorList.get(i);
				int maximumValueOfSuccessors = maxValue(successor);
				if (maximumValueOfSuccessors < v) {
					v = maximumValueOfSuccessors;
					state.put("next", successor);
				}
			}
			return v;
		}

	}

	public int minValue(GameState state, AlphaBeta ab) {
		int v = Integer.MAX_VALUE;

		if (terminalTest(state)) {
			return (computeUtility(state));

		} else {
			List<GameState> successorList = getSuccessorStates(state);
			for (int i = 0; i < successorList.size(); i++) {
				GameState successor = successorList.get(i);
				int maximumValueOfSuccessor = maxValue(successor, ab.copy());
				if (maximumValueOfSuccessor < v) {
					v = maximumValueOfSuccessor;
					state.put("next", successor);
				}
				if (v <= ab.alpha()) {
					// System.out.println("pruning from min");
					return v;
				}
				ab.setBeta(Util.min(ab.beta(), v));

			}
			return v;
		}

	}

	public void makeMiniMaxMove() {
		getMiniMaxValue(presentState);
		GameState nextState = (GameState) presentState.get("next");
		if (nextState == null) {
			throw new RuntimeException("Mini Max Move failed");

		}
		makeMove(presentState, nextState.get("moveMade"));

	}

	public void makeAlphaBetaMove() {
		getAlphaBetaValue(presentState);

		GameState nextState = (GameState) presentState.get("next");
		if (nextState == null) {
			throw new RuntimeException("Alpha Beta Move failed");
		}
		makeMove(presentState, nextState.get("moveMade"));

	}

	//
	// PROTECTED METHODS
	//
	protected abstract int computeUtility(GameState state);

	protected abstract boolean terminalTest(GameState state);

	protected int maxValue(GameState state, AlphaBeta ab) {
		int v = Integer.MIN_VALUE;
		if (terminalTest(state)) {
			return computeUtility(state);
		} else {
			List<GameState> successorList = getSuccessorStates(state);
			for (int i = 0; i < successorList.size(); i++) {
				GameState successor = (GameState) successorList.get(i);
				int minimumValueOfSuccessor = minValue(successor, ab.copy());
				if (minimumValueOfSuccessor > v) {
					v = minimumValueOfSuccessor;
					state.put("next", successor);
				}
				if (v >= ab.beta()) {
					// System.out.println("pruning from max");
					return v;
				}
				ab.setAlpha(Util.max(ab.alpha(), v));
			}
			return v;
		}
	}
}