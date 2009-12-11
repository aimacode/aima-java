package aima.core.util.datastructure;

/**
 * Note: If looking at a rectangle - the coordinate (x=0, y=0) will be the top left hand corner.
 * This corresponds with Java's AWT coordinate system.
 */

/**
 * @author Ravi Mohan
 * 
 */
public class XYLocation {
	public enum Direction {
		North, South, East, West
	};

	int xCoOrdinate, yCoOrdinate;

	public XYLocation(int x, int y) {
		xCoOrdinate = x;
		yCoOrdinate = y;
	}

	public int getXCoOrdinate() {
		return xCoOrdinate;
	}

	public int getYCoOrdinate() {
		return yCoOrdinate;
	}

	public XYLocation west() {
		return new XYLocation(xCoOrdinate - 1, yCoOrdinate);
	}

	public XYLocation east() {
		return new XYLocation(xCoOrdinate + 1, yCoOrdinate);
	}

	public XYLocation north() {
		return new XYLocation(xCoOrdinate, yCoOrdinate - 1);
	}

	public XYLocation south() {
		return new XYLocation(xCoOrdinate, yCoOrdinate + 1);
	}

	public XYLocation right() {
		return east();
	}

	public XYLocation left() {
		return west();
	}

	public XYLocation up() {
		return north();
	}

	public XYLocation down() {
		return south();
	}

	public XYLocation locationAt(Direction direction) {
		if (direction.equals(Direction.North)) {
			return north();
		}
		if (direction.equals(Direction.South)) {
			return south();
		}
		if (direction.equals(Direction.East)) {
			return east();
		}
		if (direction.equals(Direction.West)) {
			return west();
		} else {
			throw new RuntimeException("Unknown direction " + direction);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (null == o || !(o instanceof XYLocation)) {
			return super.equals(o);
		}
		XYLocation anotherLoc = (XYLocation) o;
		return ((anotherLoc.getXCoOrdinate() == xCoOrdinate) && (anotherLoc
				.getYCoOrdinate() == yCoOrdinate));
	}

	@Override
	public String toString() {
		return " ( " + xCoOrdinate + " , " + yCoOrdinate + " ) ";
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + xCoOrdinate;
		result = result + yCoOrdinate;
		return result;
	}
}