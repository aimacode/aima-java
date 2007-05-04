package aima.probability.decision;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.util.Pair;
import aima.util.Triplet;

public class MDPTransitionModel<STATE_TYPE, ACTION_TYPE> {

	private Hashtable<MDPTransition<STATE_TYPE, ACTION_TYPE>, Double> transitionToProbability = new Hashtable<MDPTransition<STATE_TYPE, ACTION_TYPE>, Double>();

	private List<STATE_TYPE> terminalStates;

	public MDPTransitionModel(List<STATE_TYPE> terminalStates) {
		this.terminalStates = terminalStates;

	}

	public void setTransitionProbability(STATE_TYPE initialState,
			ACTION_TYPE action, STATE_TYPE finalState, double probability) {
		if (!(terminalStates.contains(initialState))) {
			MDPTransition<STATE_TYPE, ACTION_TYPE> t = new MDPTransition<STATE_TYPE, ACTION_TYPE>(
					initialState, action, finalState);
			transitionToProbability.put(t, probability);
		}
	}

	public double getTransitionProbability(STATE_TYPE initialState,
			ACTION_TYPE action, STATE_TYPE finalState) {
		MDPTransition<STATE_TYPE, ACTION_TYPE> key = new MDPTransition<STATE_TYPE, ACTION_TYPE>(
				initialState, action, finalState);
		if (transitionToProbability.keySet().contains(key)) {
			return transitionToProbability.get(key);
		} else {
			return 0.0;
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (MDPTransition<STATE_TYPE, ACTION_TYPE> transition : transitionToProbability
				.keySet()) {
			buf.append(transition.toString() + " -> "
					+ transitionToProbability.get(transition) + " \n");
		}
		return buf.toString();
	}

	public Pair<ACTION_TYPE, Double> maxTransition(STATE_TYPE s,
			MDPUtilityFunction<STATE_TYPE> uf) {
		
		if ((terminalStates.contains(s))) {
			return new Pair<ACTION_TYPE, Double> (null,0.0);
		}
		
		List<MDPTransition<STATE_TYPE, ACTION_TYPE>> tranistionsStartingWithS = getTransitionsStartingWith(s);
		Hashtable<ACTION_TYPE, Double> actionsToUtilities = new Hashtable<ACTION_TYPE, Double>();

		for (MDPTransition<STATE_TYPE, ACTION_TYPE> triplet : tranistionsStartingWithS) {
			ACTION_TYPE action = triplet.getAction();
			STATE_TYPE destinationState = triplet.getDestinationState();
			double probabilityOfTransition = getTransitionProbability(s,
					action, destinationState);
			double expectedUtility = (probabilityOfTransition * uf
					.getUtility(destinationState));
			Double presentValue = actionsToUtilities.get(action);

			if (presentValue == null) {
				actionsToUtilities.put(action, expectedUtility);
			} else {
				actionsToUtilities.put(action, presentValue + expectedUtility);
			}
		}
		// System.out.println(actionsToUtilities);
		Pair<ACTION_TYPE, Double> highest = new Pair<ACTION_TYPE, Double>(null,
				-100.0);
		for (ACTION_TYPE key : actionsToUtilities.keySet()) {
			Double value = actionsToUtilities.get(key);
			if (value > highest.getSecond()) {
				highest = new Pair<ACTION_TYPE, Double>(key, value);
			}
		}
		return highest;
	}

	private List<MDPTransition<STATE_TYPE, ACTION_TYPE>> getTransitionsStartingWith(
			STATE_TYPE s) {
		List<MDPTransition<STATE_TYPE, ACTION_TYPE>> result = new ArrayList<MDPTransition<STATE_TYPE, ACTION_TYPE>>();
		for (MDPTransition<STATE_TYPE, ACTION_TYPE> transition : transitionToProbability
				.keySet()) {
			if (transition.getInitialState().equals(s)) {
				result.add(transition);
			}
		}
		return result;
	}

}
