package aima.learning.reinforcement;

import java.util.Hashtable;
import java.util.List;

import aima.probability.decision.MDP;
import aima.probability.decision.MDPPerception;
import aima.util.Pair;
import aima.util.Util;

public class QLearningAgent<STATE_TYPE, ACTION_TYPE> extends
		MDPAgent<STATE_TYPE, ACTION_TYPE> {

	private Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double> Q;

	private Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Integer> stateActionCount;

	private Double previousReward;

	private ACTION_TYPE chosenAction;

	public QLearningAgent(MDP<STATE_TYPE, ACTION_TYPE> mdp) {
		super(mdp);
		Q = new Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double>();
		stateActionCount = new Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Integer>();
	}

	@Override
	public ACTION_TYPE decideAction(MDPPerception<STATE_TYPE> perception) {
		currentState = perception.getState();
		currentReward = perception.getReward();
		// if(previousState != null){
		// incrementStateActionCount(previousState, previousAction);
		// chosenAction = updateQ(0.8);
		// }
		// if (mdp.isTerminalState(currentState)){
		// previousAction =null;
		// previousState =null;
		// previousReward =null;
		// }else{
		// previousAction = chosenAction;
		// previousState = currentState;
		// previousReward = currentReward;
		// }

		if (startingTrial()) {
			chosenAction = selectRandomAction();			
			updateLearnerState();
			return previousAction;
		}

		if (mdp.isTerminalState(currentState)) {
			incrementStateActionCount(previousState, previousAction);
			chosenAction = updateQ(0.8);
			previousAction = null;
			previousState = null;
			previousReward = null;
			return previousAction;
		}

		else {
			incrementStateActionCount(previousState, previousAction);
			chosenAction = updateQ(0.8);
			updateLearnerState();
			return previousAction;
		}
		
		
	}

	private void updateLearnerState() {
		previousAction = chosenAction;
		previousState = currentState;
		previousReward = currentReward;
	}

	private ACTION_TYPE updateQ(double gamma) {
		Double oldQValue = getQ(previousState, previousAction);
		Pair<ACTION_TYPE, Double> actionAndQDiff = actionMaximizingDifferenceinQ();
		Double addedQValue = previousReward
				+ (gamma * actionAndQDiff.getSecond());
		setQ(previousState, previousAction, oldQValue + addedQValue);
		return actionAndQDiff.getFirst();

	}

	private Pair<ACTION_TYPE, Double> actionMaximizingDifferenceinQ() {
		ACTION_TYPE resultAction = null;
		Double differenceInQ = 0.0;

		List<ACTION_TYPE> allActions = mdp.getAllActions();
		for (ACTION_TYPE action : allActions) {
			Double q1 = getQ(currentState, action);
			Double q2 = getQ(previousState, previousAction);
			Double q3 = q1 - q2;
			if (q3 > differenceInQ) {
				resultAction = action;
				differenceInQ = q3;
			}
			if (resultAction == null) {
				resultAction = Util.selectRandomlyFromList(allActions);
			}
		}

		return new Pair<ACTION_TYPE, Double>(resultAction, differenceInQ);
	}

	private ACTION_TYPE selectRandomAction() {
		List<ACTION_TYPE> allActions = mdp.getAllActions();
		return Util.selectRandomlyFromList(allActions);
	}

	private boolean startingTrial() {
		return (previousAction == null) && (previousState == null)
				&& (previousReward == null)
				&& (currentState.equals(mdp.getInitialState()));
	}

	private Double getQ(STATE_TYPE state, ACTION_TYPE action) {
		Pair<STATE_TYPE, ACTION_TYPE> stateActionPair = new Pair<STATE_TYPE, ACTION_TYPE>(
				state, action);
		if (Q.contains(stateActionPair)) {
			return Q.get(stateActionPair);
		} else {
			return 0.0; // default utility Value
		}
	}

	private void setQ(STATE_TYPE state, ACTION_TYPE action, Double d) {
		Q.put(new Pair<STATE_TYPE, ACTION_TYPE>(state, action), d);
	}

	private int getStateActionCount(
			Pair<STATE_TYPE, ACTION_TYPE> stateActionPair) {
//		if (stateActionPair.getFirst() == null) {
//			System.out.println("current state " + currentState);
//			System.out.println("chosen Action  " + chosenAction);
//			System.out.println("prev state " + previousState);
//			System.out.println("prev Action " + previousAction);
//		}
		if (stateActionCount.keySet().contains(stateActionPair)) {
			return stateActionCount.get(stateActionPair);
		} else {
			return 0;
		}
	}

	private void incrementStateActionCount(STATE_TYPE state, ACTION_TYPE action) {
		Pair<STATE_TYPE, ACTION_TYPE> stateActionPair = new Pair<STATE_TYPE, ACTION_TYPE>(
				state, action);
		stateActionCount.put(stateActionPair,
				getStateActionCount(stateActionPair) + 1);
	}

	public Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double> getQ() {
		return Q;
	}

}
