package aima.core.learning.reinforcement.agent;

import java.util.HashMap;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.learning.reinforcement.PerceptStateReward;
import aima.core.probability.mdp.ActionsFunction;
import aima.core.util.FrequencyCounter;
import aima.core.util.datastructure.Pair;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 844.<br>
 * <br>
 * 
 * <pre>
 * function Q-LEARNING-AGENT(percept) returns an action
 *   inputs: percept, a percept indicating the current state s' and reward signal r'
 *   persistent: Q, a table of action values indexed by state and action, initially zero
 *               N<sub>sa</sub>, a table of frequencies for state-action pairs, initially zero
 *               s,a,r, the previous state, action, and reward, initially null
 *               
 *   if TERMAINAL?(s) then Q[s,None] <- r'
 *   if s is not null then
 *       increment N<sub>sa</sub>[s,a]
 *       Q[s,a] <- Q[s,a] + &alpha;(N<sub>sa</sub>[s,a])(r + &gamma;max<sub>a'</sub>Q[s',a'] - Q[s,a])
 *   s,a,r <- s',argmax<sub>a'</sub>f(Q[s',a'],N<sub>sa</sub>[s',a']),r'
 *   return a
 * </pre>
 * 
 * Figure 21.8 An exploratory Q-learning agent. It is an active learner that
 * learns the value Q(s,a) of each action in each situation. It uses the same
 * exploration function f as the exploratory ADP agent, but avoids having to
 * learn the transition model because the Q-value of a state can be related
 * directly to those of its neighbors.<br>
 * <br>
 * <b>Note:</b> There appears to be two minor defects in the algorithm outlined
 * in the book:<br>
 * if TERMAINAL?(s) then Q[s,None] <- r'<br>
 * should be:<br>
 * if TERMAINAL?(s') then Q[s',None] <- r'<br>
 * so that the correct value for Q[s',a'] is used in the Q[s,a] update rule when
 * a terminal state is reached.<br>
 * <br>
 * s,a,r <- s',argmax<sub>a'</sub>f(Q[s',a'],N<sub>sa</sub>[s',a']),r'<br>
 * should be:
 * 
 * <pre>
 * if s'.TERMINAL? then s,a,r <- null else s,a,r <- s',argmax<sub>a'</sub>f(Q[s',a'],N<sub>sa</sub>[s',a']),r'
 * </pre>
 * 
 * otherwise at the beginning of a consecutive trial, s will be the prior
 * terminal state and is what will be updated in Q[s,a], which appears not to be
 * correct as you did not perform an action in the terminal state and the
 * initial state is not reachable from the prior terminal state. Comments
 * welcome.
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
public class QLearningAgent<S, A extends Action> extends
		ReinforcementAgent<S, A> {
	// persistent: Q, a table of action values indexed by state and action,
	// initially zero
	Map<Pair<S, A>, Double> Q = new HashMap<Pair<S, A>, Double>();
	// N<sub>sa</sub>, a table of frequencies for state-action pairs, initially
	// zero
	private FrequencyCounter<Pair<S, A>> Nsa = new FrequencyCounter<Pair<S, A>>();
	// s,a,r, the previous state, action, and reward, initially null
	private S s = null;
	private A a = null;
	private Double r = null;
	//
	private ActionsFunction<S, A> actionsFunction = null;
	private A noneAction = null;
	private double alpha = 0.0;
	private double gamma = 0.0;
	private int Ne = 0;
	private double Rplus = 0.0;

	/**
	 * Constructor.
	 * 
	 * @param actionsFunction
	 *            a function that lists the legal actions from a state.
	 * @param noneAction
	 *            an action representing None, i.e. a NoOp.
	 * @param alpha
	 *            a fixed learning rate.
	 * @param gamma
	 *            discount to be used.
	 * @param Ne
	 *            is fixed parameter for use in the method f(u, n).
	 * @param Rplus
	 *            R+ is an optimistic estimate of the best possible reward
	 *            obtainable in any state, which is used in the method f(u, n).
	 */
	public QLearningAgent(ActionsFunction<S, A> actionsFunction,
			A noneAction, double alpha,
			double gamma, int Ne, double Rplus) {
		this.actionsFunction = actionsFunction;
		this.noneAction = noneAction;
		this.alpha = alpha;
		this.gamma = gamma;
		this.Ne = Ne;
		this.Rplus = Rplus;
	}

	/**
	 * An exploratory Q-learning agent. It is an active learner that learns the
	 * value Q(s,a) of each action in each situation. It uses the same
	 * exploration function f as the exploratory ADP agent, but avoids having to
	 * learn the transition model because the Q-value of a state can be related
	 * directly to those of its neighbors.
	 * 
	 * @param percept
	 *            a percept indicating the current state s' and reward signal
	 *            r'.
	 * @return an action
	 */
	@Override
	public A execute(PerceptStateReward<S> percept) {

		S sPrime = percept.state();
		double rPrime = percept.reward();

		// if TERMAINAL?(s') then Q[s',None] <- r'
		if (isTerminal(sPrime)) {
			Q.put(new Pair<S, A>(sPrime, noneAction), rPrime);
		}

		// if s is not null then
		if (null != s) {
			// increment N<sub>sa</sub>[s,a]
			Pair<S, A> sa = new Pair<S, A>(s, a);
			Nsa.incrementFor(sa);
			// Q[s,a] <- Q[s,a] + &alpha;(N<sub>sa</sub>[s,a])(r +
			// &gamma;max<sub>a'</sub>Q[s',a'] - Q[s,a])
			Double Q_sa = Q.get(sa);
			if (null == Q_sa) {
				Q_sa = 0.0;
			}
			Q.put(sa, Q_sa + alpha(Nsa, s, a)
					* (r + gamma * maxAPrime(sPrime) - Q_sa));
		}
		// if s'.TERMINAL? then s,a,r <- null else
		// s,a,r <- s',argmax<sub>a'</sub>f(Q[s',a'],N<sub>sa</sub>[s',a']),r'
		if (isTerminal(sPrime)) {
			s = null;
			a = null;
			r = null;
		} else {
			s = sPrime;
			a = argmaxAPrime(sPrime);
			r = rPrime;
		}

		// return a
		return a;
	}

	@Override
	public void reset() {
		Q.clear();
		Nsa.clear();
		s = null;
		a = null;
		r = null;
	}

	@Override
	public Map<S, Double> getUtility() {
		// Q-values are directly related to utility values as follows
		// (AIMA3e pg. 843 - 21.6) :
		// U(s) = max<sub>a</sub>Q(s,a).
		Map<S, Double> U = new HashMap<S, Double>();
		for (Pair<S, A> sa : Q.keySet()) {
			Double q = Q.get(sa);
			Double u = U.get(sa.getFirst());
			if (null == u || u < q) {
				U.put(sa.getFirst(), q);
			}
		}

		return U;
	}

	//
	// PROTECTED METHODS
	//

	/**
	 * AIMA3e pg. 836 'if we change &alpha; from a fixed parameter to a function
	 * that decreases as the number of times a state action has been observed
	 * increases, then U<sup>&pi;</sup>(s) itself will converge to the correct
	 * value.<br>
	 * <br>
	 * <b>Note:</b> override this method to obtain the desired behavior.
	 * 
	 * @param Nsa
	 *            a frequency counter of observed state action pairs.
	 * @param s
	 *            the current state.
	 * @param a the current action.
	 * @return the learning rate to use based on the frequency of the state
	 *         passed in.
	 */
	protected double alpha(FrequencyCounter<Pair<S, A>> Nsa, S s, A a) {
		// Default implementation is just to return a fixed parameter value
		// irrespective of the # of times a state action has been encountered
		return alpha;
	}

	/**
	 * AIMA3e pg. 842 'f(u, n) is called the <b>exploration function</b>. It
	 * determines how greed (preferences for high values of u) is traded off
	 * against curiosity (preferences for actions that have not been tried often
	 * and have low n). The function f(u, n) should be increasing in u and
	 * decreasing in n.
	 * 
	 * 
	 * <b>Note:</b> Override this method to obtain desired behavior.
	 * 
	 * @param u
	 *            the currently estimated utility.
	 * @param n
	 *            the number of times this situation has been encountered.
	 * @return the exploration value.
	 */
	protected double f(Double u, int n) {
		// A Simple definition of f(u, n):
		if (null == u || n < Ne) {
			return Rplus;
		}
		return u;
	}

	//
	// PRIVATE METHODS
	//
	private boolean isTerminal(S s) {
		boolean terminal = false;
		if (null != s && actionsFunction.actions(s).size() == 0) {
			// No actions possible in state is considered terminal.
			terminal = true;
		}
		return terminal;
	}

	private double maxAPrime(S sPrime) {
		double max = Double.NEGATIVE_INFINITY;
		if (actionsFunction.actions(sPrime).size() == 0) {
			// a terminal state
			max = Q.get(new Pair<S, A>(sPrime, noneAction));
		} else {
			for (A aPrime : actionsFunction.actions(sPrime)) {
				Double Q_sPrimeAPrime = Q.get(new Pair<S, A>(sPrime, aPrime));
				if (null != Q_sPrimeAPrime && Q_sPrimeAPrime > max) {
					max = Q_sPrimeAPrime;
				}
			}
		}
		if (max == Double.NEGATIVE_INFINITY) {
			// Assign 0 as the mimics Q being initialized to 0 up front.
			max = 0.0;
		}
		return max;
	}

	// argmax<sub>a'</sub>f(Q[s',a'],N<sub>sa</sub>[s',a'])
	private A argmaxAPrime(S sPrime) {
		A a = null;
		double max = Double.NEGATIVE_INFINITY;
		for (A aPrime : actionsFunction.actions(sPrime)) {
			Pair<S, A> sPrimeAPrime = new Pair<S, A>(sPrime, aPrime);
			double explorationValue = f(Q.get(sPrimeAPrime), Nsa
					.getCount(sPrimeAPrime));
			if (explorationValue > max) {
				max = explorationValue;
				a = aPrime;
			}
		}
		return a;
	}
}
