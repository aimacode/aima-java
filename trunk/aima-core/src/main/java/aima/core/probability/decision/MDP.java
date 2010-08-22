package aima.core.probability.decision;

import java.util.List;

import aima.core.probability.Randomizer;
import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class MDP<STATE_TYPE, ACTION_TYPE> {
	private STATE_TYPE initialState;

	private MDPTransitionModel<STATE_TYPE, ACTION_TYPE> transitionModel;

	private MDPRewardFunction<STATE_TYPE> rewardFunction;

	private List<STATE_TYPE> nonFinalstates, terminalStates;

	private MDPSource<STATE_TYPE, ACTION_TYPE> source;

	public MDP(MDPSource<STATE_TYPE, ACTION_TYPE> source) {
		this.initialState = source.getInitialState();
		this.transitionModel = source.getTransitionModel();
		this.rewardFunction = source.getRewardFunction();
		this.nonFinalstates = source.getNonFinalStates();
		this.terminalStates = source.getFinalStates();
		this.source = source;
	}

	public MDP<STATE_TYPE, ACTION_TYPE> emptyMdp() {
		MDP<STATE_TYPE, ACTION_TYPE> mdp = new MDP<STATE_TYPE, ACTION_TYPE>(
				source);
		mdp.rewardFunction = new MDPRewardFunction<STATE_TYPE>();
		mdp.rewardFunction.setReward(initialState, rewardFunction
				.getRewardFor(initialState));
		mdp.transitionModel = new MDPTransitionModel<STATE_TYPE, ACTION_TYPE>(
				terminalStates);
		return mdp;
	}

	public MDPUtilityFunction<STATE_TYPE> valueIteration(double gamma,
			double error, double delta) {
		MDPUtilityFunction<STATE_TYPE> U = initialUtilityFunction();
		MDPUtilityFunction<STATE_TYPE> U_dash = initialUtilityFunction();
		double delta_max = (error * gamma) / (1 - gamma);
		do {
			U = U_dash.copy();
			// System.out.println(U);
			delta = 0.0;
			for (STATE_TYPE s : nonFinalstates) {
				Pair<ACTION_TYPE, Double> highestUtilityTransition = transitionModel
						.getTransitionWithMaximumExpectedUtility(s, U);
				double utility = rewardFunction.getRewardFor(s)
						+ (gamma * highestUtilityTransition.getSecond());
				U_dash.setUtility(s, utility);
				if ((Math.abs(U_dash.getUtility(s) - U.getUtility(s))) > delta) {
					delta = Math.abs(U_dash.getUtility(s) - U.getUtility(s));
				}

			}
		} while (delta < delta_max);
		return U;

	}

	public MDPUtilityFunction<STATE_TYPE> valueIterationForFixedIterations(
			int numberOfIterations, double gamma) {
		MDPUtilityFunction<STATE_TYPE> utilityFunction = initialUtilityFunction();

		for (int i = 0; i < numberOfIterations; i++) {
			Pair<MDPUtilityFunction<STATE_TYPE>, Double> result = valueIterateOnce(
					gamma, utilityFunction);
			utilityFunction = result.getFirst();
			// double maxUtilityGrowth = result.getSecond();
			// System.out.println("maxUtilityGrowth " + maxUtilityGrowth);
		}

		return utilityFunction;
	}

	public MDPUtilityFunction<STATE_TYPE> valueIterationTillMAximumUtilityGrowthFallsBelowErrorMargin(
			double gamma, double errorMargin) {
		int iterationCounter = 0;
		double maxUtilityGrowth = 0.0;
		MDPUtilityFunction<STATE_TYPE> utilityFunction = initialUtilityFunction();
		do {
			Pair<MDPUtilityFunction<STATE_TYPE>, Double> result = valueIterateOnce(
					gamma, utilityFunction);
			utilityFunction = result.getFirst();
			maxUtilityGrowth = result.getSecond();
			iterationCounter++;
			// System.out.println("Itration Number" +iterationCounter + " max
			// utility growth " + maxUtilityGrowth);

		} while (maxUtilityGrowth > errorMargin);

		return utilityFunction;
	}

	public Pair<MDPUtilityFunction<STATE_TYPE>, Double> valueIterateOnce(
			double gamma, MDPUtilityFunction<STATE_TYPE> presentUtilityFunction) {
		double maxUtilityGrowth = 0.0;
		MDPUtilityFunction<STATE_TYPE> newUtilityFunction = new MDPUtilityFunction<STATE_TYPE>();

		for (STATE_TYPE s : nonFinalstates) {
			// double utility = rewardFunction.getRewardFor(s)
			// + (gamma * highestUtilityTransition.getSecond());

			double utility = valueIterateOnceForGivenState(gamma,
					presentUtilityFunction, s);

			double differenceInUtility = Math.abs(utility
					- presentUtilityFunction.getUtility(s));
			if (differenceInUtility > maxUtilityGrowth) {
				maxUtilityGrowth = differenceInUtility;
			}
			newUtilityFunction.setUtility(s, utility);

			for (STATE_TYPE state : terminalStates) {
				newUtilityFunction.setUtility(state, presentUtilityFunction
						.getUtility(state));
			}
		}

		return new Pair<MDPUtilityFunction<STATE_TYPE>, Double>(
				newUtilityFunction, maxUtilityGrowth);

	}

	public MDPPolicy<STATE_TYPE, ACTION_TYPE> policyIteration(double gamma) {
		MDPUtilityFunction<STATE_TYPE> U = initialUtilityFunction();
		MDPPolicy<STATE_TYPE, ACTION_TYPE> pi = randomPolicy();
		boolean unchanged = false;
		do {
			unchanged = true;

			U = policyEvaluation(pi, U, gamma, 3);
			for (STATE_TYPE s : nonFinalstates) {
				Pair<ACTION_TYPE, Double> maxTransit = transitionModel
						.getTransitionWithMaximumExpectedUtility(s, U);
				Pair<ACTION_TYPE, Double> maxPolicyTransit = transitionModel
						.getTransitionWithMaximumExpectedUtilityUsingPolicy(pi,
								s, U);

				if (maxTransit.getSecond() > maxPolicyTransit.getSecond()) {
					pi.setAction(s, maxTransit.getFirst());
					unchanged = false;
				}
			}
		} while (unchanged == false);
		return pi;
	}

	public MDPUtilityFunction<STATE_TYPE> policyEvaluation(
			MDPPolicy<STATE_TYPE, ACTION_TYPE> pi,
			MDPUtilityFunction<STATE_TYPE> U, double gamma, int iterations) {
		MDPUtilityFunction<STATE_TYPE> U_dash = U.copy();
		for (int i = 0; i < iterations; i++) {

			U_dash = valueIterateOnceWith(gamma, pi, U_dash);
		}
		return U_dash;
	}

	public MDPPolicy<STATE_TYPE, ACTION_TYPE> randomPolicy() {
		MDPPolicy<STATE_TYPE, ACTION_TYPE> policy = new MDPPolicy<STATE_TYPE, ACTION_TYPE>();
		for (STATE_TYPE s : nonFinalstates) {
			policy.setAction(s, transitionModel.randomActionFor(s));
		}
		return policy;
	}

	public MDPUtilityFunction<STATE_TYPE> initialUtilityFunction() {

		return rewardFunction.asUtilityFunction();
	}

	public STATE_TYPE getInitialState() {
		return initialState;
	}

	public double getRewardFor(STATE_TYPE state) {
		return rewardFunction.getRewardFor(state);
	}

	public void setReward(STATE_TYPE state, double reward) {
		rewardFunction.setReward(state, reward);
	}

	public void setTransitionProbability(
			MDPTransition<STATE_TYPE, ACTION_TYPE> transition,
			double probability) {
		transitionModel.setTransitionProbability(transition.getInitialState(),
				transition.getAction(), transition.getDestinationState(),
				probability);
	}

	public double getTransitionProbability(
			MDPTransition<STATE_TYPE, ACTION_TYPE> transition) {
		return transitionModel.getTransitionProbability(transition
				.getInitialState(), transition.getAction(), transition
				.getDestinationState());
	}

	public MDPPerception<STATE_TYPE> execute(STATE_TYPE state,
			ACTION_TYPE action, Randomizer r) {
		return source.execute(state, action, r);
	}

	public boolean isTerminalState(STATE_TYPE state) {
		return terminalStates.contains(state);
	}

	public List<MDPTransition<STATE_TYPE, ACTION_TYPE>> getTransitionsWith(
			STATE_TYPE initialState, ACTION_TYPE action) {
		return transitionModel.getTransitionsWithStartingStateAndAction(
				initialState, action);
	}

	public List<ACTION_TYPE> getAllActions() {
		return source.getAllActions();
	}

	@Override
	public String toString() {
		return "initial State = " + initialState.toString()
				+ "\n rewardFunction = " + rewardFunction.toString()
				+ "\n transitionModel = " + transitionModel.toString()
				+ "\n states = " + nonFinalstates.toString();
	}

	//
	// PRIVATE METHODS
	// 

	private double valueIterateOnceForGivenState(double gamma,
			MDPUtilityFunction<STATE_TYPE> presentUtilityFunction,
			STATE_TYPE state) {
		Pair<ACTION_TYPE, Double> highestUtilityTransition = transitionModel
				.getTransitionWithMaximumExpectedUtility(state,
						presentUtilityFunction);
		double utility = rewardFunction.getRewardFor(state)
				+ (gamma * highestUtilityTransition.getSecond());

		return utility;
	}

	private MDPUtilityFunction<STATE_TYPE> valueIterateOnceWith(double gamma,
			MDPPolicy<STATE_TYPE, ACTION_TYPE> pi,
			MDPUtilityFunction<STATE_TYPE> U) {
		MDPUtilityFunction<STATE_TYPE> U_dash = U.copy();
		for (STATE_TYPE s : nonFinalstates) {

			Pair<ACTION_TYPE, Double> highestPolicyTransition = transitionModel
					.getTransitionWithMaximumExpectedUtilityUsingPolicy(pi, s,
							U);
			double utility = rewardFunction.getRewardFor(s)
					+ (gamma * highestPolicyTransition.getSecond());
			U_dash.setUtility(s, utility);

		}
		// System.out.println("ValueIterationOnce before " + U);
		// System.out.println("ValueIterationOnce after " + U_dash);
		return U_dash;
	}
}
