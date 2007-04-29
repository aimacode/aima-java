package aima.probability.decision;

import java.util.Hashtable;


public class UtilityFunction<STATE_TYPE> {
	private Hashtable<STATE_TYPE,Double> hash ;
	
	public UtilityFunction(){
		hash =  new Hashtable<STATE_TYPE,Double>();
	}
	public double  getUtility(STATE_TYPE state){
		return hash.get(state);
	}
	public void setUtility(STATE_TYPE state, double utility){
		hash.put(state,utility);
	}
	public UtilityFunction<STATE_TYPE> copy() {
		// TODO Auto-generated method stub
		return null;
	}
}
