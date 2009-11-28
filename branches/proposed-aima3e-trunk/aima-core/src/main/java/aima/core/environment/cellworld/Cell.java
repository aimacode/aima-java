package aima.core.environment.cellworld;

/**
 * @author Ravi Mohan
 * 
 */
public class Cell {
	private int x_co_ord, y_co_ord;

	private double reward;

	public Cell(int i, int j, double reward) {
		this.x_co_ord = i;
		this.y_co_ord = j;
		this.reward = reward;
	}

	public int getY() {
		return y_co_ord;
	}

	public int getX() {
		return x_co_ord;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	public CellWorldPosition position() {
		return new CellWorldPosition(getX(), getY());
	}
}
