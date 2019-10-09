package aima.core.learning.reinforcement.agent;

import aima.core.agent.Action;
import aima.core.learning.reinforcement.PerceptStateReward;
import aima.core.probability.mdp.ActionsFunction;
import aima.core.probability.mdp.PolicyEvaluation;
import aima.core.probability.mdp.RewardFunction;
import aima.core.probability.mdp.TransitionProbabilityFunction;
import aima.core.probability.mdp.impl.MDP;
import aima.core.util.FrequencyCounter;
import aima.core.util.datastructure.Pair;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 834.<br>
 * <br>
 *
 * <pre>
 * function PASSIVE-ADP-AGENT(percept) returns an action
 *   inputs: percept, a percept indicating the current state s' and reward signal r'
 *   persistent: &pi;, a fixed policy
 *               mdp, an MDP with model P, rewards R, discount &gamma;
 *               U, a table of utilities, initially empty
 *               N<sub>sa</sub>, a table of frequencies for state-action pairs, initially zero
 *               N<sub>s'|sa</sub>, a table of outcome frequencies give state-action pairs, initially zero
 *               s, a, the previous state and action, initially null
 *
 *   if s' is new then U[s'] <- r'; R[s'] <- r'
 *   if s is not null then
 *        increment N<sub>sa</sub>[s,a] and N<sub>s'|sa</sub>[s',s,a]
 *        for each t such that N<sub>s'|sa</sub>[t,s,a] is nonzero do
 *            P(t|s,a) <-  N<sub>s'|sa</sub>[t,s,a] / N<sub>sa</sub>[s,a]
 *   U <- POLICY-EVALUATION(&pi;, U, mdp)
 *   if s'.TERMINAL? then s,a <- null else s,a <- s',&pi;[s']
 *   return a
 * </pre>
 *
 * Figure 21.2 A passive reinforcement learning agent based on adaptive dynamic
 * programming. The POLICY-EVALUATION function solves the fixed-policy Bellman
 * equations, as described on page 657.
 *
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 *
 */
public class PassiveADPAgent<S, A extends Action> extends ReinforcementAgent<S, A> {
	// persistent: &pi;, a fixed policy
	private Map<S, A> pi = new HashMap<>();
	// mdp, an MDP with model P, rewards R, discount &gamma;
	private MDP<S, A> mdp = null;
	private Map<Pair<S, Pair<S, A>>, Double> P = new HashMap<>();
	private Map<S, Double> R = new HashMap<>();
	private PolicyEvaluation<S, A> policyEvaluation = null;
	// U, a table of utilities, initially empty
	private Map<S, Double> U = new HashMap<>();
	// N<sub>sa</sub>, a table of frequencies for state-action pairs, initially
	// zero
	private FrequencyCounter<Pair<S, A>> Nsa = new FrequencyCounter<>();
	// N<sub>s'|sa</sub>, a table of outcome frequencies give state-action
	// pairs, initially zero
	private FrequencyCounter<Pair<S, Pair<S, A>>> NsDelta_sa = new FrequencyCounter<>();
	// s, a, the previous state and action, initially null
	private S s = null;
	private A a = null;

	/**
	 * Constructor.
	 *
	 * @param fixedPolicy
	 *            &pi; a fixed policy.
	 * @param states
	 *            the possible states in the world (i.e. fully observable).
	 * @param initialState
	 *            the initial state for the agent.
	 * @param actionsFunction
	 *            a function that lists the legal actions from a state.
	 * @param policyEvaluation
	 *            a function for evaluating a policy.
	 */
	public PassiveADPAgent(Map<S, A> fixedPolicy, Set<S> states,
			S initialState, ActionsFunction<S, A> actionsFunction,
			PolicyEvaluation<S, A> policyEvaluation) {
		this.pi.putAll(fixedPolicy);
		RewardFunction<S> rewardfn = (s) -> R.get(s);

		this.mdp = new MDP<>(states, initialState, actionsFunction,
				(sDelta, s, a) -> Optional.ofNullable(P.get(new Pair<>(sDelta, new Pair<>(s, a)))).orElse(0.0),
				rewardfn);
		this.policyEvaluation = policyEvaluation;
	}

	/**
	 * Passive reinforcement learning based on adaptive dynamic programming.
	 *
	 * @param percept
	 *            a percept indicating the current state s' and reward signal
	 *            r'.
	 * @return an action
	 */
	@Override
	public Optional<A> act(PerceptStateReward<S> percept) {
		// if s' is new then U[s'] <- r'; R[s'] <- r'
		S sDelta = percept.state();
		double rDelta = percept.reward();
		if (!U.containsKey(sDelta)) {
			U.put(sDelta, rDelta);
			R.put(sDelta, rDelta);
		}
		// if s is not null then
		if (s != null) {
			// increment N<sub>sa</sub>[s,a] and N<sub>s'|sa</sub>[s',s,a]
			Pair<S, A> sa = new Pair<>(s, a);
			Nsa.incrementFor(sa);
			NsDelta_sa.incrementFor(new Pair<>(sDelta, sa));
			// for each t such that N<sub>s'|sa</sub>[t,s,a] is nonzero do
			for (S t : mdp.states()) {
				Pair<S, Pair<S, A>> t_sa = new Pair<>(t, sa);
				if (NsDelta_sa.getCount(t_sa) != 0) {
					// P(t|s,a) <- N<sub>s'|sa</sub>[t,s,a] /
					// N<sub>sa</sub>[s,a]
					P.put(t_sa, NsDelta_sa.getCount(t_sa).doubleValue() / Nsa.getCount(sa).doubleValue());
				}
			}
		}
		// U <- POLICY-EVALUATION(&pi;, U, mdp)
		U = policyEvaluation.evaluate(pi, U, mdp);
		// if s'.TERMINAL? then s,a <- null else s,a <- s',&pi;[s']
		if (isTerminal(sDelta)) {
			s = null;
			a = null;

		} else {
			s = sDelta;
			a = pi.get(sDelta);
		}
		// return a
		return Optional.ofNullable(a);
	}

	@Override
	public Map<S, Double> getUtility() {
		return Collections.unmodifiableMap(U);
	}

	@Override
	public void reset() {
		P.clear();
		R.clear();
		U = new HashMap<>();
		Nsa.clear();
		NsDelta_sa.clear();
		s = null;
		a = null;
	}

	//
	// PRIVATE METHODS
	//
	private boolean isTerminal(S s) {
		// A state with no possible actions is considered terminal.
		return mdp.actions(s).isEmpty();
	}
}
