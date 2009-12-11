/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.eightpuzzle;

import aima.basic.XYLocation;
import aima.search.framework.HeuristicFunction;

/**
 * @author Ravi Mohan
 * 
 */

public class ManhattanHeuristicFunction implements HeuristicFunction {

	public double getHeuristicValue(Object state) {
		EightPuzzleBoard board = (EightPuzzleBoard) state;
		int retVal = 0;
		for (int i = 1; i < 9; i++) {
			XYLocation loc = board.getLocationOf(i);
			retVal += evaluateManhattanDistanceOf(i, loc);
		}
		return retVal;

	}

	public int evaluateManhattanDistanceOf(int i, XYLocation loc) {
		int retVal = -1;
		int xpos = loc.getXCoOrdinate();
		int ypos = loc.getYCoOrdinate();
		switch (i) {

		case 1:
			retVal = Math.abs(xpos - 0) + Math.abs(ypos - 1);
			break;
		case 2:
			retVal = Math.abs(xpos - 0) + Math.abs(ypos - 2);
			break;
		case 3:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 0);
			break;
		case 4:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 1);
			break;
		case 5:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 2);
			break;
		case 6:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 0);
			break;
		case 7:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 1);
			break;
		case 8:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 2);
			break;

		}
		return retVal;
	}

}