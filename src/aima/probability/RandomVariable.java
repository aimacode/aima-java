package aima.probability;

import java.util.Hashtable;
import java.util.List;

public class RandomVariable {
	private String name;
	private Hashtable<String, Double> distribution;

	public RandomVariable(String name, List<String> states){
		this.name = name;
		this.distribution = new Hashtable<String, Double>();
		int numberOfStates =  states.size();
		double initialProbability = 1.0/numberOfStates;
		for (String s:states){
			distribution.put(s,initialProbability);
		}
	}
	
	public void setProbabilityOf(String state, Double probability){
		//check that state is valid, then
		distribution.put(state, probability);
	}
	
	public double getProbabilityOf(String state){
		//check state is valid, then
		return distribution.get(state);
	}

}
