package aima.core.learning.reinforcement;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import aima.core.probability.decision.MDPPolicy;
import aima.core.util.Util;
import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class QTable<STATE_TYPE, ACTION_TYPE> {

	Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double> table;

	private List<ACTION_TYPE> allPossibleActions;

	public QTable(List<ACTION_TYPE> allPossibleActions) {
		this.table = new Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double>();
		this.allPossibleActions = allPossibleActions;
	}

	public Double getQValue(STATE_TYPE state, ACTION_TYPE action) {
		Pair<STATE_TYPE, ACTION_TYPE> stateActionPair = new Pair<STATE_TYPE, ACTION_TYPE>(
				state, action);
		if (!(table.keySet().contains(stateActionPair))) {
			return 0.0;
		} else {
			return table.get(stateActionPair);
		}
	}

	public Pair<ACTION_TYPE, Double> maxDiff(STATE_TYPE startState,
			ACTION_TYPE action, STATE_TYPE endState) {
		Double maxDiff = 0.0;
		ACTION_TYPE maxAction = null;
		// randomly choose an action so that it doesn't return the same action
		// every time if all Q(a,s) are zero
		maxAction = Util.selectRandomlyFromList(allPossibleActions);
		maxDiff = getQValue(endState, maxAction)
				- getQValue(startState, action);

		for (ACTION_TYPE anAction : allPossibleActions) {
			Double diff = getQValue(endState, anAction)
					- getQValue(startState, action);
			if (diff > maxDiff) {
				maxAction = anAction;
				maxDiff = diff;
			}
		}

		return new Pair<ACTION_TYPE, Double>(maxAction, maxDiff);
	}

	public void setQValue(STATE_TYPE state, ACTION_TYPE action, Double d) {
		Pair<STATE_TYPE, ACTION_TYPE> stateActionPair = new Pair<STATE_TYPE, ACTION_TYPE>(
				state, action);
		table.put(stateActionPair, d);
	}

	public ACTION_TYPE upDateQ(STATE_TYPE startState, ACTION_TYPE action,
			STATE_TYPE endState, double alpha, double reward, double phi) {
		double oldQValue = getQValue(startState, action);
		Pair<ACTION_TYPE, Double> actionAndMaxDiffValue = maxDiff(startState,
				action, endState);
		double addedValue = alpha
				* (reward + (phi * actionAndMaxDiffValue.getSecond()));
		setQValue(startState, action, oldQValue + addedValue);
		return actionAndMaxDiffValue.getFirst();
	}

	public void normalize() {
		Double maxValue = findMaximumValue();
		if (maxValue != 0.0) {
			for (Pair<STATE_TYPE, ACTION_TYPE> key : table.keySet()) {
				Double presentValue = table.get(key);
				table.put(key, presentValue / maxValue);
			}
		}
	}

	public MDPPolicy<STATE_TYPE, ACTION_TYPE> getPolicy() {
		MDPPolicy<STATE_TYPE, ACTION_TYPE> policy = new MDPPolicy<STATE_TYPE, ACTION_TYPE>();
		List<STATE_TYPE> startingStatesRecorded = getAllStartingStates();

		for (STATE_TYPE state : startingStatesRecorded) {
			ACTION_TYPE action = getRecordedActionWithMaximumQValue(state);
			policy.setAction(state, action);
		}
		return policy;
	}

	@Override
	public String toString() {
		return table.toString();
	}

	//
	// PRIVATE METHODS
	//

	private Double findMaximumValue() {
		Set<Pair<STATE_TYPE, ACTION_TYPE>> keys = table.keySet();
		if (keys.size() > 0) {
			Double maxValue = table.get(keys.toArray()[0]);
			for (Pair<STATE_TYPE, ACTION_TYPE> key : keys) {
				Double v = table.get(key);
				if (v > maxValue) {
					maxValue = v;
				}
			}
			return maxValue;

		} else {
			return 0.0;
		}
	}

	private ACTION_TYPE getRecordedActionWithMaximumQValue(STATE_TYPE state) {
		Double maxValue = Double.NEGATIVE_INFINITY;
		ACTION_TYPE action = null;
		for (Pair<STATE_TYPE, ACTION_TYPE> stateActionPair : table.keySet()) {
			if (stateActionPair.getFirst().equals(state)) {
				ACTION_TYPE ac = stateActionPair.getSecond();
				Double value = table.get(stateActionPair);
				if (value > maxValue) {
					maxValue = value;
					action = ac;
				}
			}
		}
		return action;
	}

	private List<STATE_TYPE> getAllStartingStates() {
		List<STATE_TYPE> states = new ArrayList<STATE_TYPE>();
		for (Pair<STATE_TYPE, ACTION_TYPE> stateActionPair : table.keySet()) {
			STATE_TYPE state = stateActionPair.getFirst();
			if (!(states).contains(state)) {
				states.add(state);
			}
		}
		return states;
	}
}
