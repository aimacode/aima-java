package aima.core.learning.reinforcement.next;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 834.<br>
 * <br>
 * 
 * <pre>
 * function PASSIVE-ADP-AGENT(percept) returns an action
 *   inputs: percept, a percept indicating the current state s' and reward signal r'
 *   persistent: &pi;, a fixed policy
 *               mdp, an MDP with model P, rewards R, discount &gamma;
 *               U, a table of utilities, initally empty
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
 * 
 */
public class PassiveADPAgent<S, A> {

}
