package aima.core.environment.eightpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class EightPuzzleBoard implements Cloneable {

	public static Action LEFT = new DynamicAction("Left");
	public static Action RIGHT = new DynamicAction("Right");
	public static Action UP = new DynamicAction("Up");
	public static Action DOWN = new DynamicAction("Down");
	private int[] state;

	//
	// PUBLIC METHODS
	//

	public EightPuzzleBoard() {
		state = new int[] { 5, 4, 0, 6, 1, 8, 7, 3, 2 };
	}

	public EightPuzzleBoard(int[] state) { state.clone();
		this.state = state.clone();
	}

	public int[] getState() {
		return state;
	}

	public int getValueAt(XYLocation loc) {
		return getValueAt(loc.getX(), loc.getY());
	}

	public XYLocation getLocationOf(int val) {
		int pos = getPositionOf(val);
		return new XYLocation(getXCoord(pos), getYCoord(pos));
	}

	public void moveGapRight() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int y = getYCoord(gapPos);
		if (x != 2) {
			setValue(x, y, getValueAt(x + 1, y));
			setValue(x + 1, y, 0);
		}
	}

	public void moveGapLeft() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int y = getYCoord(gapPos);
		if (x != 0) {
			setValue(x, y, getValueAt(x - 1, y));
			setValue(x - 1, y, 0);
		}
	}

	public void moveGapDown() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int y = getYCoord(gapPos);
		if (y != 2) {
			setValue(x, y, getValueAt(x, y + 1));
			setValue(x, y + 1, 0);
		}

	}

	public void moveGapUp() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int y = getYCoord(gapPos);
		if (y != 0) {
			setValue(x, y, getValueAt(x, y - 1));
			setValue(x, y - 1, 0);
		}
	}

	public List<XYLocation> getPositions() {
		ArrayList<XYLocation> result = new ArrayList<>(9);
		for (int i = 0; i < 9; i++) {
			int pos = getPositionOf(i);
			result.add(new XYLocation(getXCoord(pos), getYCoord(pos)));
		}
		return result;
	}

	public void setBoard(List<XYLocation> locs) {
		int count = 0;
		for (XYLocation loc : locs)
			setValue(loc.getX(), loc.getY(), count++);
	}

	public boolean canMoveGap(Action action) {
		boolean result = true;
		int pos = getPositionOf(0);
		if (action.equals(LEFT))
			result = (getXCoord(pos) != 0);
		else if (action.equals(RIGHT))
			result = (getXCoord(pos) != 2);
		else if (action.equals(UP))
			result = (getYCoord(pos) != 0);
		else if (action.equals(DOWN))
			result = (getYCoord(pos) != 2);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && getClass() == o.getClass()) {
			EightPuzzleBoard aBoard = (EightPuzzleBoard) o;
			return Arrays.equals(state, aBoard.state);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(state);
	}

	@Override
	public String toString() {
		return state[0] + " " + state[1] + " " + state[2] + "\n"
				+ state[3] + " " + state[4] + " " + state[5] + "\n"
				+ state[6] + " " + state[7] + " " + state[8];
	}

	@Override
	public EightPuzzleBoard clone() {
		EightPuzzleBoard result = null;
		try {
			result = (EightPuzzleBoard) super.clone();
			result.state = result.state.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace(); // should never happen...
		}
		return result;
	}

	//
	// PRIVATE METHODS
	//

	private int getXCoord(int pos) {
		return pos % 3;
	}

	private int getYCoord(int pos) {
		return pos / 3;
	}

	private int getPosition(int x, int y) {
		return x + 3 * y;
	}

	private int getValueAt(int x, int y) {
		return state[getPosition(x, y)];
	}

	private int getGapPosition() {
		return getPositionOf(0);
	}

	private int getPositionOf(int val) {
		for (int i = 0; i < 9; i++)
			if (state[i] == val)
				return i;
		return -1;
	}

	private void setValue(int x, int y, int val) {
		int pos = getPosition(x, y);
		state[pos] = val;
	}
}
