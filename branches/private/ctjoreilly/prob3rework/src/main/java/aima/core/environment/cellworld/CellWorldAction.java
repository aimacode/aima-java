package aima.core.environment.cellworld;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 645.<br>
 * <br>
 * 
 * The actions in every state are Up, Down, Left, and Right.<br>
 * <br>
 * <b>Note:<b> Moving 'North' causes y to increase by 1, 'Down' y to decrease by
 * 1, 'Left' x to decrease by 1, and 'Right' x to increase by 1 within a Cell
 * World.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public enum CellWorldAction implements Action {
	Up, Down, Left, Right, None;

	private static final Set<CellWorldAction> _actions = new LinkedHashSet<CellWorldAction>();
	static {
		_actions.add(Up);
		_actions.add(Down);
		_actions.add(Left);
		_actions.add(Right);
		_actions.add(None);
	}

	/**
	 * 
	 * @return a set of the actual actions.
	 */
	public static final Set<CellWorldAction> actions() {
		return _actions;
	}
	
	//
	// START-Action
	@Override
	public boolean isNoOp() {
		if (None == this) {
			return true;
		}
		return false;
	}
	// END-Action
	//

	/**
	 * 
	 * @param curX
	 *            the current x position.
	 * @return the result on the x position of applying this action.
	 */
	public int getXResult(int curX) {
		int newX = curX;

		switch (this) {
		case Left:
			newX--;
			break;
		case Right:
			newX++;
			break;
		}

		return newX;
	}

	/**
	 * 
	 * @param curY
	 *            the current y position.
	 * @return the result on the y position of applying this action.
	 */
	public int getYResult(int curY) {
		int newY = curY;

		switch (this) {
		case Up:
			newY++;
			break;
		case Down:
			newY--;
			break;
		}

		return newY;
	}

	/**
	 * 
	 * @return the first right angled action related to this action.
	 */
	public CellWorldAction getFirstRightAngledAction() {
		CellWorldAction a = null;

		switch (this) {
		case Up:
		case Down:
			a = Left;
			break;
		case Left:
		case Right:
			a = Down;
			break;
		case None:
			a = None;
			break;
		}

		return a;
	}

	/**
	 * 
	 * @return the second right angled action related to this action.
	 */
	public CellWorldAction getSecondRightAngledAction() {
		CellWorldAction a = null;

		switch (this) {
		case Up:
		case Down:
			a = Right;
			break;
		case Left:
		case Right:
			a = Up;
			break;
		case None:
			a = None;
			break;
		}

		return a;
	}
}
