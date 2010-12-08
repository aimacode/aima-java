package aima.core.learning.reinforcement;

import java.util.Hashtable;
import java.util.List;

import aima.core.probability.decision.MDP;
import aima.core.probability.decision.MDPPerception;
import aima.core.probability.decision.MDPPolicy;
import aima.core.probability.decision.MDPTransition;
import aima.core.probability.decision.MDPUtilityFunction;
import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class PassiveADPAgent<STATE_TYPE, ACTION_TYPE> extends
		MDPAgent<STATE_TYPE, ACTION_TYPE> {
	private MDPPolicy<STATE_TYPE, ACTION_TYPE> policy;

	private MDPUtilityFunction<STATE_TYPE> utilityFunction;

	private Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double> nsa;

	private Hashtable<MDPTransition<STATE_TYPE, ACTION_TYPE>, Double> nsasdash;

	public PassiveADPAgent(MDP<STATE_TYPE, ACTION_TYPE> mdp,
			MDPPolicy<STATE_TYPE, ACTION_TYPE> policy) {
		super(mdp.emptyMdp());
		this.policy = policy;
		this.utilityFunction = new MDPUtilityFunction<STATE_TYPE>();
		this.nsa = new Hashtable<Pair<STATE_TYPE, ACTION_TYPE>, Double>();
		this.nsasdash = new Hashtable<MDPTransition<STATE_TYPE, ACTION_TYPE>, Double>();

	}

	@Override
	public ACTION_TYPE decideAction(MDPPerception<STATE_TYPE> perception) {

		if (!(utilityFunction.hasUtilityFor(perception.getState()))) { // if
			// perceptionState
			// is
			// new
			utilityFunction.setUtility(perception.getState(), perception
					.getReward());
			mdp.setReward(perception.getState(), perception.getReward());
		}
		if (!(previousState == null)) {
			Double oldValue1 = nsa.get(new Pair<STATE_TYPE, ACTION_TYPE>(
					previousState, previousAction));
			if (oldValue1 == null) {
				nsa.put(new Pair<STATE_TYPE, ACTION_TYPE>(previousState,
						previousAction), 1.0);
			} else {
				nsa.put(new Pair<STATE_TYPE, ACTION_TYPE>(previousState,
						previousAction), oldValue1 + 1);
			}
			Double oldValue2 = nsasdash
					.get(new MDPTransition<STATE_TYPE, ACTION_TYPE>(
							previousState, previousAction, currentState));
			if (oldValue2 == null) {
				nsasdash.put(new MDPTransition<STATE_TYPE, ACTION_TYPE>(
						previousState, previousAction, currentState), 1.0);

			} else {
				nsasdash.put(new MDPTransition<STATE_TYPE, ACTION_TYPE>(
						previousState, previousAction, currentState),
						oldValue2 + 1);
			}
			for (MDPTransition<STATE_TYPE, ACTION_TYPE> transition : nsasdash
					.keySet()) {
				if (nsasdash.get(transition) != 0.0) {
					double newValue = nsasdash.get(transition)
							/ nsa.get(new Pair<STATE_TYPE, ACTION_TYPE>(
									transition.getInitialState(), transition
											.getAction()));
					mdp.setTransitionProbability(transition, newValue);
				}
			}
			List<MDPTransition<STATE_TYPE, ACTION_TYPE>> validTransitions = mdp
					.getTransitionsWith(previousState, policy
							.getAction(previousState));
			utilityFunction = valueDetermination(validTransitions, 1);
		}

		if (mdp.isTerminalState(currentState)) {
			previousState = null;
			previousAction = null;
		} else {
			previousState = currentState;
			previousAction = policy.getAction(currentState);
		}
		return previousAction;
	}

	public MDPUtilityFunction<STATE_TYPE> getUtilityFunction() {
		return utilityFunction;
	}

	//
	// PRIVATE METHODS
	//
	private MDPUtilityFunction<STATE_TYPE> valueDetermination(
			List<MDPTransition<STATE_TYPE, ACTION_TYPE>> validTransitions,
			double gamma) {
		MDPUtilityFunction<STATE_TYPE> uf = utilityFunction.copy();
		double additional = 0.0;
		if (validTransitions.size() > 0) {
			STATE_TYPE initState = validTransitions.get(0).getInitialState();
			double reward = mdp.getRewardFor(initState);
			for (MDPTransition<STATE_TYPE, ACTION_TYPE> transition : validTransitions) {
				additional += mdp.getTransitionProbability(transition)
						* utilityFunction.getUtility(transition
								.getDestinationState());
			}
			uf.setUtility(initState, reward + (gamma * additional));
		}

		return uf;
	}
}
