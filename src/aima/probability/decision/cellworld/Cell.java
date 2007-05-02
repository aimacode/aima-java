package aima.probability.decision.cellworld;


public class Cell {
	private int x_co_ord,y_co_ord;
	private double utility,reward;
	
	public Cell(int i, int j, double reward, double utility) {
		this.x_co_ord =i;
		this.y_co_ord =j;
		this.utility= utility;
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

	public double getUtility() {
		return utility;
	}

	public void setUtility(double utility) {
		this.utility = utility;
	}
	
	public CellWorldPosition  position(){
		return new  CellWorldPosition(getX(),getY());
	}

	

}
