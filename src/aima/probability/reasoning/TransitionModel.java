package aima.probability.reasoning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.probability.RandomVariable;
import aima.util.Matrix;
import aima.util.Table;

public class TransitionModel {

    private Table<String, String, Double> table;
    private List<String> states;

    public TransitionModel(List<String> states, List<String> actions) {
	this.states = states;
	List<String> state_actions = new ArrayList<String>();
	for (String state : states) {
	    for (String action : actions) {
		state_actions.add(state.concat(action));
	    }
	}
	table = new Table<String, String, Double>(state_actions, states);
    }

    public TransitionModel(List<String> states) {
	// no actions possible thus the only "action" is to "wait" till the next
	// perception is observed
	this(states, Arrays.asList(new String[] { HmmConstants.DO_NOTHING }));
    }

    public void setTransitionProbability(String startState, String endState,
	    Double probability) {
	String start_state_plus_action = startState
		.concat(HmmConstants.DO_NOTHING);
	table.set(start_state_plus_action, endState, probability);
    }

    public void setTransitionProbability(String startState, String action,
	    String endState, Double probability) {
	String start_state_plus_action = startState.concat(action);
	table.set(start_state_plus_action, endState, probability);
    }

    public double get(String old_state_action, String newState) {

	return table.get(old_state_action, newState);
    }

    public Matrix asMatrix(String action) {
	Matrix transitionMatrix = new Matrix(states.size(), 
		states.size());
	for (int i = 0; i < states.size(); i++) {
	    String oldState = states.get(i);
	    String old_state_action = oldState.concat(action);
	    for (int j = 0; j < states.size(); j++) {
		String newState = states.get(j);
		double transitionProbability = get(old_state_action, newState);
		transitionMatrix.set(i, j, transitionProbability);
	    }
	}
	return transitionMatrix;
    }
    
    public Matrix asMatrix() {
	return asMatrix(HmmConstants.DO_NOTHING);
    }
    
    public Matrix unitMatrix() {
	Matrix m =  asMatrix();
	return Matrix.identity(m.getRowDimension(), m.getColumnDimension());
    }

}
