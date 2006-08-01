/*
 * Created by IntelliJ IDEA. User: rrmohan Date: Jan 19, 2003 Time: 5:12:20 PM
 * To change template for new class use Code Style | Class Templates options
 * (Tools | IDE Options).
 */
package aima.util;

import aima.basic.XYLocation;

public class Calculator {

	public static int calculateSquareOfDistanceBetweenLocations(
			XYLocation loc1, XYLocation loc2) {
		int xdifference = loc1.getXCoOrdinate() - loc2.getXCoOrdinate();
		int ydifference = loc1.getYCoOrdinate() - loc2.getYCoOrdinate();
		return (xdifference * xdifference) + (ydifference * ydifference);

	}
}