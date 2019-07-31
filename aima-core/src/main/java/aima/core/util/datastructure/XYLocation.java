package aima.core.util.datastructure;

/**
 * Note: If looking at a rectangle - the coordinate (x=0, y=0) will be the top
 * left hand corner. This corresponds with Java's AWT coordinate system.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class XYLocation {
	public enum Direction {
		North, South, East, West
	}

    int x, y;

	/**
	 * Constructs and initializes a location at the specified (<em>x</em>,
	 * <em>y</em>) location in the coordinate space.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public XYLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the X coordinate of the location in integer precision.
	 * 
	 * @return the X coordinate of the location in double precision.
	 */
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * Returns the location one unit left of this location.
	 * 
	 * @return the location one unit left of this location.
	 */
	public XYLocation west() {
		return new XYLocation(x - 1, y);
	}

	/**
	 * Returns the location one unit right of this location.
	 * 
	 * @return the location one unit right of this location.
	 */
	public XYLocation east() {
		return new XYLocation(x + 1, y);
	}

	/**
	 * Returns the location one unit ahead of this location.
	 * 
	 * @return the location one unit ahead of this location.
	 */
	public XYLocation north() {
		return new XYLocation(x, y - 1);
	}

	/**
	 * Returns the location one unit behind, this location.
	 * 
	 * @return the location one unit behind this location.
	 */
	public XYLocation south() {
		return new XYLocation(x, y + 1);
	}

	/**
	 * Returns the location one unit left of this location.
	 * 
	 * @return the location one unit left of this location.
	 */
	public XYLocation left() {
		return west();
	}

	/**
	 * Returns the location one unit right of this location.
	 * 
	 * @return the location one unit right of this location.
	 */
	public XYLocation right() {
		return east();
	}

	/**
	 * Returns the location one unit above this location.
	 * 
	 * @return the location one unit above this location.
	 */
	public XYLocation up() {
		return north();
	}

	/**
	 * Returns the location one unit below this location.
	 * 
	 * @return the location one unit below this location.
	 */
	public XYLocation down() {
		return south();
	}

	/**
	 * Returns the location one unit from this location in the specified
	 * direction.
	 * 
	 * @return the location one unit from this location in the specified
	 *         direction.
	 */
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
		if (null != o && getClass() == o.getClass()) {
			XYLocation loc = (XYLocation) o;
			return x == loc.getX() && y == loc.getY();
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + x;
		result = 43 * result + y;
		return result;
	}
}