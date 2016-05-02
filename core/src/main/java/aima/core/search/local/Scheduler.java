package aima.core.search.local;
/*
* The Scheduler for Simulated Annealing
*
* @author Ravi Mohan
* @author Anurag Rai
*/
public class Scheduler {

  	private final int k, limit;
	private final double lam;
	
	//default constructor
	public Scheduler() {
		//base value
		this.k = 20;
		this.lam = 0.045;
		//time limit
		this.limit = 100;
	}
	
	public Scheduler(int k, double lam, int limit) {
		this.k = k;
		this.lam = lam;
		this.limit = limit;
	}
	
	/*
	* The probability also decreases as the temperature T goes down: 
	* bad moves are more likely to be allowed at the start
	* when T is high, and they become more unlikely as T decreases.
	*
	* @param t
	*		the time that has gone by from the start of the algo
	*
	* @return the value of schedule calculated as a function of given time
	*/
	public double getTemp(int t) {
	  if (t < limit) {
			double res = k * Math.exp((-1) * lam * t);
			return res;
		} else {
			return 0.0;
		}
	}
}
