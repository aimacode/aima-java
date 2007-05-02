package aima.probability.decision;

import java.util.Hashtable;

import aima.util.Triplet;

public class MDPTransitionModel<STATE_TYPE, ACTION_TYPE> {

	private Hashtable<Triplet<STATE_TYPE, ACTION_TYPE, STATE_TYPE>, Double> tripletToProbability = new Hashtable<Triplet<STATE_TYPE, ACTION_TYPE, STATE_TYPE>, Double>();;

	public void setTransitionProbability(STATE_TYPE initialState,
			ACTION_TYPE action, STATE_TYPE finalState, double probability) {
		
		Triplet<STATE_TYPE, ACTION_TYPE, STATE_TYPE> t = new Triplet<STATE_TYPE, ACTION_TYPE, STATE_TYPE>(
				initialState, action, finalState);
		tripletToProbability.put(t, probability);
	}

	public double getTransitionProbability(STATE_TYPE initialState,
			ACTION_TYPE action, STATE_TYPE finalState) {
		Triplet<STATE_TYPE, ACTION_TYPE, STATE_TYPE> key = new Triplet<STATE_TYPE, ACTION_TYPE, STATE_TYPE>(
				initialState, action, finalState);
		if (tripletToProbability.keySet().contains(key)) {
			return tripletToProbability.get(key);
		} else {
			return 0.0;
		}
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		for (Triplet<STATE_TYPE, ACTION_TYPE, STATE_TYPE> triplet : tripletToProbability.keySet()){
			buf.append(triplet.toString()+ " -> " + tripletToProbability.get(triplet)+ " \n");
		}
		return buf.toString();
	}
}
