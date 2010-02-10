package aima.core.search.adversarial;

/**
 * @author Ravi Mohan
 * 
 */
public class AlphaBeta {
	private int alpha;

	private int beta;

	public AlphaBeta(int alpha, int beta) {
		this.alpha = alpha;
		this.beta = beta;
	}

	public int alpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int beta() {
		return beta;
	}

	public void setBeta(int beta) {
		this.beta = beta;
	}

	public AlphaBeta copy() {
		return new AlphaBeta(alpha, beta);
	}
}
