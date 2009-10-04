package aima.basic;

/**
 * @author Ravi Mohan
 * 
 */
public class XYLocation {
	int xCoOrdinate, yCoOrdinate;

	public XYLocation(int i, int j) {
		xCoOrdinate = i;
		yCoOrdinate = j;
	}

	public int getXCoOrdinate() {
		return xCoOrdinate;
	}

	@Override
	public boolean equals(Object o) {
		XYLocation anotherLoc = (XYLocation) o;
		return ((anotherLoc.getXCoOrdinate() == xCoOrdinate) && (anotherLoc
				.getYCoOrdinate() == yCoOrdinate));
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

	public XYLocation locationAt(String direction) {
		if (direction.equals("North")) {
			return north();
		}
		if (direction.equals("South")) {
			return south();
		}
		if (direction.equals("East")) {
			return east();
		}
		if (direction.equals("West")) {
			return west();
		} else {
			throw new RuntimeException("Unknown direction " + direction);
		}
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