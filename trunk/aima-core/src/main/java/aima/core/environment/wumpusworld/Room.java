package aima.core.environment.wumpusworld;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 236.<br>
 * <br>
 * The <b>wumpus world</b> is a cave consisting of rooms connected by
 * passageways. Rooms are labeled [x,y], for example [1,1] would indicate the
 * room in the bottom left, and is also the room the agent always starts in. See
 * Figure 7.2 for an example room layout representing a wumpus's cave.
 * 
 * @author Ciaran O'Reilly
 */
public class Room {
	private int x = 1;
	private int y = 1;

	/**
	 * Constructor.
	 * 
	 * @param x
	 *            the room's x location.
	 * @param y
	 *            the room's y location.
	 */
	public Room(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @return the room's x location.
	 */
	public int getX() {
		return x;
	}

	/**
	 *
	 * @return the room's y location.
	 */
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Room) {
			Room r = (Room) o;
			if (x == r.x && y == r.y) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + getX();
		result = 43 * result + getY();
		return result;
	}
}
