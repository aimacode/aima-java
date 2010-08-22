package aima.core.learning.reinforcement;

import java.util.Hashtable;
import java.util.List;

import aima.core.probability.decision.MDP;
import aima.core.probability.decision.MDPPerception;
import aima.core.util.FrequencyCounter;
import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class QLearningAgent<STATE_TYPE, ACTION_TYPE> extends
		MDPAgent<STATE_TYPE, ACTION_TYPE> {

	private Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double> Q;

	private FrequencyCounter<Pair<STATE_TYPE, ACTION_TYPE>> stateActionCount;

	private Double previousReward;

	private QTable<STATE_TYPE, ACTION_TYPE> qTable;

	private int actionCounter;

	public QLearningAgent(MDP<STATE_TYPE, ACTION_TYPE> mdp) {
		super(mdp);
		Q = new Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double>();
		qTable = new QTable<STATE_TYPE, ACTION_TYPE>(mdp.getAllActions());
		stateActionCount = new FrequencyCounter<Pair<STATE_TYPE, ACTION_TYPE>>();
		actionCounter = 0;
	}

	@Override
	public ACTION_TYPE decideAction(MDPPerception<STATE_TYPE> perception) {
		currentState = perception.getState();
		currentReward = perception.getReward();

		if (startingTrial()) {
			ACTION_TYPE chosenAction = selectRandomAction();
			updateLearnerState(chosenAction);
			return previousAction;
		}

		if (mdp.isTerminalState(currentState)) {
			incrementStateActionCount(previousState, previousAction);
			updateQ(0.8);
			previousAction = null;
			previousState = null;
			previousReward = null;
			return previousAction;
		}

		else {
			incrementStateActionCount(previousState, previousAction);
			ACTION_TYPE chosenAction = updateQ(0.8);
			updateLearnerState(chosenAction);
			return previousAction;
		}

	}

	public Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double> getQ() {
		return Q;
	}

	public QTable<STATE_TYPE, ACTION_TYPE> getQTable() {
		return qTable;
	}

	//
	// PRIVATE METHODS
	//

	private void updateLearnerState(ACTION_TYPE chosenAction) {
		// previousAction = actionMaximizingLearningFunction();
		previousAction = chosenAction;
		previousAction = chosenAction;
		previousState = currentState;
		previousReward = currentReward;
	}

	private ACTION_TYPE updateQ(double gamma) {

		actionCounter++;
		// qtable update

		double alpha = calculateProbabilityOf(previousState, previousAction);
		ACTION_TYPE ac = qTable.upDateQ(previousState, previousAction,
				currentState, alpha, currentReward, 0.8);

		return ac;
	}

	private double calculateProbabilityOf(STATE_TYPE state, ACTION_TYPE action) {
		Double den = 0.0;
		Double num = 0.0;
		for (Pair<STATE_TYPE, ACTION_TYPE> stateActionPair : stateActionCount
				.getStates()) {

			if (stateActionPair.getFirst().equals(state)) {
				den += 1;
				if (stateActionPair.getSecond().equals(action)) {
					num += 1;
				}
			}
		}
		return num / den;
	}

	private ACTION_TYPE actionMaximizingLearningFunction() {
		ACTION_TYPE maxAct = null;
		Double maxValue = Double.NEGATIVE_INFINITY;
		for (ACTION_TYPE action : mdp.getAllActions()) {
			Double qValue = qTable.getQValue(currentState, action);
			Double lfv = learningFunction(qValue);
			if (lfv > maxValue) {
				maxValue = lfv;
				maxAct = action;
			}
		}
		return maxAct;
	}

	private Double learningFunction(Double utility) {
		if (actionCounter > 3) {
			actionCounter = 0;
			return 1.0;
		} else {
			return utility;
		}
	}

	private ACTION_TYPE selectRandomAction() {
		List<ACTION_TYPE> allActions = mdp.getAllActions();
		return allActions.get(0);
		// return Util.selectRandomlyFromList(allActions);
	}

	private boolean startingTrial() {
		return (previousAction == null) && (previousState == null)
				&& (previousReward == null)
				&& (currentState.equals(mdp.getInitialState()));
	}

	private void incrementStateActionCount(STATE_TYPE state, ACTION_TYPE action) {
		Pair<STATE_TYPE, ACTION_TYPE> stateActionPair = new Pair<STATE_TYPE, ACTION_TYPE>(
				state, action);
		stateActionCount.incrementFor(stateActionPair);
	}
}
