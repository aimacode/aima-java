/*
 * Created on Feb 15, 2005
 *
 */
package aima.games;

import java.util.ArrayList;

import aima.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class Game {
	protected GameState initialState = new GameState();

	protected GameState presentState = new GameState();

	public int getLevel(GameState g) {
		return (((Integer) g.get("level")).intValue());
	}

	protected int level;

	public ArrayList getMoves(GameState state) {
		return (ArrayList) state.get("moves");
	}

	public String getPlayerToMove(GameState state) {
		return (String) state.get("player");
	}

	public int getUtility(GameState h) {
		return ((Integer) h.get("utility")).intValue();
	}

	public GameState getState() {
		return presentState;
	}

	protected abstract int computeUtility(GameState state);

	protected abstract boolean terminalTest(GameState state);

	public int maxValue(GameState state) {
		int v = Integer.MIN_VALUE;
		if (terminalTest(state)) {
			return computeUtility(state);
		} else {
			ArrayList successorList = getSuccessorStates(state);
			for (int i = 0; i < successorList.size(); i++) {
				GameState successor = (GameState) successorList.get(i);
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
			ArrayList successorList = getSuccessorStates(state);
			for (int i = 0; i < successorList.size(); i++) {
				GameState successor = (GameState) successorList.get(i);
				int maximumValueOfSuccessors = maxValue(successor);
				if (maximumValueOfSuccessors < v) {
					v = maximumValueOfSuccessors;
					state.put("next", successor);
				}
			}
			return v;
		}

	}

	protected int maxValue(GameState state, AlphaBeta ab) {
		int v = Integer.MIN_VALUE;
		if (terminalTest(state)) {
			return computeUtility(state);
		} else {
			ArrayList successorList = getSuccessorStates(state);
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

	public int minValue(GameState state, AlphaBeta ab) {
		int v = Integer.MAX_VALUE;

		if (terminalTest(state)) {
			return (computeUtility(state));

		} else {
			ArrayList successorList = getSuccessorStates(state);
			for (int i = 0; i < successorList.size(); i++) {
				GameState successor = (GameState) successorList.get(i);
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

	public abstract ArrayList getSuccessorStates(GameState state);

	public abstract GameState makeMove(GameState state, Object o);

	public boolean hasEnded() {
		return (terminalTest(getState()));
	}

	public Game() {
	}

	public abstract int getMiniMaxValue(GameState state);

	public abstract int getAlphaBetaValue(GameState state);
}