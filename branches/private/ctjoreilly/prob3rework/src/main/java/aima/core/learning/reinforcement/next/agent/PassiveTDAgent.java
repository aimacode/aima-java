package aima.core.learning.reinforcement.next.agent;

import aima.core.agent.Action;

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
public class PassiveTDAgent<S, A extends Action> {

}
