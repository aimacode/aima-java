package aima.probability.reasoning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.util.Table;

public class HiddenMarkovModel {

	Table<String, String, Double> priorDistribution, transitionModel, sensorModel, postDistribution;

	public HiddenMarkovModel(List<String> states, List<String>perceptions,List<String> actions ) {
		createPriorDistribution(states);
		createEmptyTransitionTable(states,actions);
		createSensorModel(states,perceptions);
	}


	private void createPriorDistribution(List<String> states) {
		String columnHeader = "probability";
		priorDistribution = new Table<String, String, Double>(states,Arrays.asList(new String[]{columnHeader}));
		int numberOfStates =  states.size();
		double initialProbability = 1.0/numberOfStates;
		for (String s:states){
			priorDistribution.set(s,columnHeader,initialProbability);
		}
		
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
	
	private void createSensorModel(List<String> states, List<String> perceptions) {
	
		sensorModel = new Table<String, String, Double>( states,perceptions);//TODO check this ordering;
		
	}


	public Table<String, String, Double> prior() {
		return priorDistribution;
	}


	public void setTransitionModelValue(String startState, String action, String endState, Double probability) {
		String start_state_plus_action = startState.concat(action);
		transitionModel.set(start_state_plus_action,endState,probability);		
	}


	public void setSensorModelValue( String state, String perception,double probability) {
		sensorModel.set(state,perception,probability);		
		
	}

}
