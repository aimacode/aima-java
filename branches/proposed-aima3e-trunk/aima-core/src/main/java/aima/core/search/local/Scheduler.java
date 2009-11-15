package aima.core.search.local;

/**
 * @author Ravi Mohan
 * 
 */
public class Scheduler {

	private final int k, limit;

	private final double lam;

	public Scheduler(int k, double lam, int limit) {
		this.k = k;
		this.lam = lam;
		this.limit = limit;
	}

	public Scheduler() {
		this.k = 20;
		this.lam = 0.045;
		this.limit = 100;
	}

	public double getTemp(int t) {
		if (t < limit) {
			double res = k * Math.exp((-1) * lam * t);
			return res;
		} else {
			return 0.0;
		}
	}
}
