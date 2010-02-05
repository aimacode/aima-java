package aima.core.environment.cellworld;

/**
 * @author Ravi Mohan
 * 
 */
public class CellWorldPosition {
	private int x, y;

	public CellWorldPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CellWorldPosition)) {
			return false;
		}
		CellWorldPosition cwp = (CellWorldPosition) o;
		return ((this.x == cwp.x) && (this.y == cwp.y));
	}

	@Override
	public int hashCode() {
		return x + 31 * y;
	}

	@Override
	public String toString() {
		return "< " + x + " , " + y + " > ";
	}
}
