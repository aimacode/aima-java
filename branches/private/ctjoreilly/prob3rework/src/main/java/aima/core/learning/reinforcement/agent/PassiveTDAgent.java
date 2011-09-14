package aima.core.learning.reinforcement.agent;

import java.util.HashMap;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.learning.reinforcement.PerceptStateReward;
import aima.core.util.FrequencyCounter;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 837.<br>
 * <br>
 * 
 * <pre>
 * function PASSIVE-TD-AGENT(percept) returns an action
 *   inputs: percept, a percept indicating the current state s' and reward signal r'
 *   persistent: &pi;, a fixed policy
 *               U, a table of utilities, initially empty
 *               N<sub>s</sub>, a table of frequencies for states, initially zero
 *               s,a,r, the previous state, action, and reward, initially null
 *               
 *   if s' is new then U[s'] <- r'
 *   if s is not null then
 *        increment N<sub>s</sub>[s]
 *        U[s] <- U[s] + &alpha;(N<sub>s</sub>[s])(r + &gamma;U[s'] - U[s])
 *   if s'.TERMINAL? then s,a,r <- null else s,a,r <- s',&pi;[s'],r'
 *   return a
 * </pre>
 * 
 * Figure 21.4 A passive reinforcement learning agent that learns utility
 * estimates using temporal differences. The step-size function &alpha;(n) is
 * chosen to ensure convergence, as described in the text.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class PassiveTDAgent<S, A extends Action> extends
		ReinforcementAgent<S, A> {
	// persistent: &pi;, a fixed policy
	private Map<S, A> pi = new HashMap<S, A>();
	// U, a table of utilities, initially empty
	private Map<S, Double> U = new HashMap<S, Double>();
	// N<sub>s</sub>, a table of frequencies for states, initially zero
	private FrequencyCounter<S> Ns = new FrequencyCounter<S>();
	// s,a,r, the previous state, action, and reward, initially null
	private S s = null;
	private A a = null;
	private Double r = null;
	//
	private double alpha = 0.0;
	private double gamma = 0.0;

	/**
	 * Constructor.
	 * 
	 * @param fixedPolicy
	 *            &pi; a fixed policy.
	 * @param alpha
	 *            a fixed learning rate.
	 * @param gamma
	 *            discount to be used.
	 */
	public PassiveTDAgent(Map<S, A> fixedPolicy, double alpha, double gamma) {
		this.pi.putAll(fixedPolicy);
		this.alpha = alpha;
		this.gamma = gamma;
	}

	/**
	 * Passive reinforcement learning that learns utility estimates using
	 * temporal differences
	 * 
	 * @param percept
	 *            a percept indicating the current state s' and reward signal
	 *            r'.
	 * @return an action
	 */
	@Override
	public A execute(PerceptStateReward<S> percept) {
		// if s' is new then U[s'] <- r'
		S sDelta = percept.state();
		double rDelta = percept.reward();
		if (!U.containsKey(sDelta)) {
			U.put(sDelta, rDelta);
		}
		// if s is not null then
		if (null != s) {
			// increment N<sub>s</sub>[s]
			Ns.incrementFor(s);
			// U[s] <- U[s] + &alpha;(N<sub>s</sub>[s])(r + &gamma;U[s'] - U[s])
			double U_s = U.get(s);
			U.put(s, U_s + alpha(Ns, s) * (r + gamma * U.get(sDelta) - U_s));
		}
		// if s'.TERMINAL? then s,a,r <- null else s,a,r <- s',&pi;[s'],r'
		if (isTerminal(sDelta)) {
			s = null;
			a = null;
			r = null;
		} else {
			s = sDelta;
			a = pi.get(sDelta);
			r = rDelta;
		}

		// return a
		return a;
	}

	@Override
	public Map<S, Double> getUtility() {
		return new HashMap<S, Double>(U);
	}

	@Override
	public void reset() {
		U = new HashMap<S, Double>();
		Ns.clear();
		s = null;
		a = null;
		r = null;
	}

	//
	// PROTECTED METHODS
	//
	/**
	 * AIMA3e pg. 836 'if we change &alpha; from a fixed parameter to a function
	 * that decreases as the number of times a state has been visited increases,
	 * then U<sup>&pi;</sup>(s) itself will converge to the correct value.<br>
	 * <br>
	 * <b>Note:</b> override this method to obtain the desired behavior.
	 * 
	 * @param Ns
	 *            a frequency counter of observed states.
	 * @param s
	 *            the current state.
	 * @return the learning rate to use based on the frequency of the state
	 *         passed in.
	 */
	protected double alpha(FrequencyCounter<S> Ns, S s) {
		// Default implementation is just to return a fixed parameter value
		// irrespective of the # of times a state has been encountered
		return alpha;
	}

	//
	// PRIVATE METHODS
	//
	private boolean isTerminal(S s) {
		boolean terminal = false;
		Action a = pi.get(s);
		if (null == a || a.isNoOp()) {
			// No actions possible in state is considered terminal.
			terminal = true;
		}
		return terminal;
	}
}
