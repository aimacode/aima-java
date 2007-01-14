package aima.probability.reasoning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.probability.RandomVariable;
import aima.util.Table;

public class HiddenMarkovModel {

	Table<String, String, Double>  transitionModel, sensorModel; 
	RandomVariable  priorDistribution, belief;

	public HiddenMarkovModel(List<String> states, List<String>perceptions,List<String> actions ) {
		priorDistribution = new RandomVariable("HiddenState", states);	
		belief = new RandomVariable("HiddenState", states);	
		createEmptyTransitionTable(states,actions);
		sensorModel = new Table<String, String, Double>( states,perceptions);
	}



	
	private void createEmptyTransitionTable(List<String> states, List<String> actions) {
		List<String> state_actions = new ArrayList<String>();
		for (String state: states){
			for (String action: actions){
				state_actions.add(state.concat(action));
			}
		}
		transitionModel = new Table<String, String, Double>(state_actions, states);
		
	}
	

	public RandomVariable prior() {
		return priorDistribution;
	}
	
	public RandomVariable belief(){
		return belief;
	}


	public void setTransitionModelValue(String startState, String action, String endState, Double probability) {
		String start_state_plus_action = startState.concat(action);
		transitionModel.set(start_state_plus_action,endState,probability);		
	}


	public void setSensorModelValue( String state, String perception,double probability) {
		sensorModel.set(state,perception,probability);		
		
	}

}
