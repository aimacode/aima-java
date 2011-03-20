package aima.core.probability.decision;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class MDPTransitionModel<STATE_TYPE, ACTION_TYPE> {

	private Hashtable<MDPTransition<STATE_TYPE, ACTION_TYPE>, Double> transitionToProbability = new Hashtable<MDPTransition<STATE_TYPE, ACTION_TYPE>, Double>();

	private List<STATE_TYPE> terminalStates;

	public MDPTransitionModel(List<STATE_TYPE> terminalStates) {
		this.terminalStates = terminalStates;

	}

	public void setTransitionProbability(STATE_TYPE initialState,
			ACTION_TYPE action, STATE_TYPE finalState, double probability) {
		if (!(isTerminal(initialState))) {
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

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (MDPTransition<STATE_TYPE, ACTION_TYPE> transition : transitionToProbability
				.keySet()) {
			buf.append(transition.toString() + " -> "
					+ transitionToProbability.get(transition) + " \n");
		}
		return buf.toString();
	}

	public Pair<ACTION_TYPE, Double> getTransitionWithMaximumExpectedUtility(
			STATE_TYPE s, MDPUtilityFunction<STATE_TYPE> uf) {

		if ((isTerminal(s))) {
			return new Pair<ACTION_TYPE, Double>(null, 0.0);
		}

		List<MDPTransition<STATE_TYPE, ACTION_TYPE>> transitionsStartingWithS = getTransitionsStartingWith(s);
		Hashtable<ACTION_TYPE, Double> actionsToUtilities = getExpectedUtilityForSelectedTransitions(
				transitionsStartingWithS, uf);

		return getActionWithMaximumUtility(actionsToUtilities);

	}

	public Pair<ACTION_TYPE, Double> getTransitionWithMaximumExpectedUtilityUsingPolicy(
			MDPPolicy<STATE_TYPE, ACTION_TYPE> policy, STATE_TYPE s,
			MDPUtilityFunction<STATE_TYPE> uf) {
		if ((isTerminal(s))) {
			return new Pair<ACTION_TYPE, Double>(null, 0.0);
		}
		List<MDPTransition<STATE_TYPE, ACTION_TYPE>> transitionsWithStartingStateSAndActionFromPolicy = getTransitionsWithStartingStateAndAction(
				s, policy.getAction(s));
		Hashtable<ACTION_TYPE, Double> actionsToUtilities = getExpectedUtilityForSelectedTransitions(
				transitionsWithStartingStateSAndActionFromPolicy, uf);

		return getActionWithMaximumUtility(actionsToUtilities);

	}

	public List<MDPTransition<STATE_TYPE, ACTION_TYPE>> getTransitionsWithStartingStateAndAction(
			STATE_TYPE s, ACTION_TYPE a) {
		List<MDPTransition<STATE_TYPE, ACTION_TYPE>> result = new ArrayList<MDPTransition<STATE_TYPE, ACTION_TYPE>>();
		for (MDPTransition<STATE_TYPE, ACTION_TYPE> transition : transitionToProbability
				.keySet()) {
			if ((transition.getInitialState().equals(s))
					&& (transition.getAction().equals(a))) {
				result.add(transition);
			}
		}
		return result;
	}

	public ACTION_TYPE randomActionFor(STATE_TYPE s) {
		List<MDPTransition<STATE_TYPE, ACTION_TYPE>> transitions = getTransitionsStartingWith(s);
		// MDPTransition<STATE_TYPE, ACTION_TYPE> randomTransition = Util
		// .selectRandomlyFromList(transitions);
		return transitions.get(0).getAction();
		// return randomTransition.getAction();
	}

	//
	// PRIVATE METHODS
	//

	private boolean isTerminal(STATE_TYPE s) {
		return terminalStates.contains(s);
	}

	private Pair<ACTION_TYPE, Double> getActionWithMaximumUtility(
			Hashtable<ACTION_TYPE, Double> actionsToUtilities) {
		Pair<ACTION_TYPE, Double> highest = new Pair<ACTION_TYPE, Double>(null,
				Double.MIN_VALUE);
		for (ACTION_TYPE key : actionsToUtilities.keySet()) {
			Double value = actionsToUtilities.get(key);
			if (value > highest.getSecond()) {
				highest = new Pair<ACTION_TYPE, Double>(key, value);
			}
		}
		return highest;
	}

	private Hashtable<ACTION_TYPE, Double> getExpectedUtilityForSelectedTransitions(

	List<MDPTransition<STATE_TYPE, ACTION_TYPE>> transitions,
			MDPUtilityFunction<STATE_TYPE> uf) {
		Hashtable<ACTION_TYPE, Double> actionsToUtilities = new Hashtable<ACTION_TYPE, Double>();
		for (MDPTransition<STATE_TYPE, ACTION_TYPE> triplet : transitions) {
			STATE_TYPE s = triplet.getInitialState();
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
		return actionsToUtilities;
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
