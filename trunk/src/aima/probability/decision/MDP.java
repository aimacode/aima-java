package aima.probability.decision;

import java.util.List;

import aima.util.Pair;

public class MDP<STATE_TYPE, ACTION_TYPE> {
	private STATE_TYPE initialState;

	private MDPTransitionModel<STATE_TYPE, ACTION_TYPE> transitionModel;

	private MDPRewardFunction<STATE_TYPE> rewardFunction;

	private List<STATE_TYPE> states;

	public MDP(STATE_TYPE initialState,
			MDPTransitionModel<STATE_TYPE, ACTION_TYPE> transitionModel,
			MDPRewardFunction<STATE_TYPE> rewardFunction,
			List<STATE_TYPE> states) {
		this.initialState = initialState;
		this.transitionModel = transitionModel;
		this.rewardFunction = rewardFunction;
		this.states = states;
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
			for (STATE_TYPE s : states) {
				Pair<ACTION_TYPE, Double> highestUtilityTransition = transitionModel
						.maxTransition(s, U);
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
			MDPUtilityFunction<STATE_TYPE> cachedUtilityFunction = utilityFunction
					.copy();
			
			Pair<MDPUtilityFunction<STATE_TYPE>, Double> result =  valueIterateOnce(gamma, utilityFunction);
			utilityFunction = result.getFirst();
			double maxUtilityGrowth = result.getSecond();
			//System.out.println("maxUtilityGrowth " + maxUtilityGrowth);
					
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
			//System.out.println("Itration Number" +iterationCounter + " max utility growth " + maxUtilityGrowth);

		}while (maxUtilityGrowth > errorMargin);
		
		return utilityFunction;
	}

	public Pair<MDPUtilityFunction<STATE_TYPE>, Double> valueIterateOnce(
			double gamma, MDPUtilityFunction<STATE_TYPE> presentUtilityFunction) {
		double maxUtilityGrowth = 0.0;
		MDPUtilityFunction<STATE_TYPE> newUtilityFunction = new MDPUtilityFunction<STATE_TYPE>();

		for (STATE_TYPE s : states) {
			Pair<ACTION_TYPE, Double> highestUtilityTransition = transitionModel
					.maxTransition(s, presentUtilityFunction);
			double utility = rewardFunction.getRewardFor(s)
					+ (gamma * highestUtilityTransition.getSecond());
			double differenceInUtility = Math.abs(utility - presentUtilityFunction.getUtility(s));
			if (differenceInUtility > maxUtilityGrowth){
				maxUtilityGrowth = differenceInUtility;
			}
			newUtilityFunction.setUtility(s, utility);

		}

		return new Pair<MDPUtilityFunction<STATE_TYPE>, Double>(
				newUtilityFunction, maxUtilityGrowth);

	}

	private MDPUtilityFunction<STATE_TYPE> initialUtilityFunction() {

		return rewardFunction.asUtilityFunction();
	}

	public String toString() {
		return "initial State = " + initialState.toString()
				+ "\n rewardFunction = " + rewardFunction.toString()
				+ "\n transitionModel = " + transitionModel.toString()
				+ "\n states = " + states.toString();
	}

}
