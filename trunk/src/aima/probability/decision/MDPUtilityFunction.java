package aima.probability.decision;

import java.util.Hashtable;


public class MDPUtilityFunction<STATE_TYPE> {
	private Hashtable<STATE_TYPE,Double> hash ;
	
	public MDPUtilityFunction(){
		hash =  new Hashtable<STATE_TYPE,Double>();
	}
	public double  getUtility(STATE_TYPE state){
		Double d =  hash.get(state);
		if (d == null){
			System.out.println("no value for " + state );
		}
		return d;
	}
	public void setUtility(STATE_TYPE state, double utility){
		hash.put(state,utility);
	}
	public MDPUtilityFunction<STATE_TYPE> copy() {
		MDPUtilityFunction<STATE_TYPE> other = new MDPUtilityFunction<STATE_TYPE>();
		for (STATE_TYPE state: hash.keySet()){
			other.setUtility(state, hash.get(state));
		}
		return other;
	}
	
	public String toString(){
		return hash.toString();
	}
}
