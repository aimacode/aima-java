package aima.core.learning.reinforcement.next.agent;

import aima.core.agent.Action;

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
 * directly to those of its neighbors.
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
public class QLearningAgent<S, A extends Action> {

}
