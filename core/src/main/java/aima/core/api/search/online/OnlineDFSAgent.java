package aima.core.api.search.online;
/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ?.??, page ???.<br>
 * <br>
 * 
 * <pre>
 * function ONLINE-DFS-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               untried, a table that lists, for each state, the actions not yet tried
 *               unbacktracked, a table that lists, for each state, the backtracks not yet tried
 *               s, a, the previous state and action, initially null
 *    
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state (not in untried) then untried[s'] &lt;- ACTIONS(s')
 *   if s is not null then
 *       result[s, a] &lt;- s'
 *       add s to the front of the unbacktracked[s']
 *   if untried[s'] is empty then
 *       if unbacktracked[s'] is empty then return stop
 *       else a &lt;- an action b such that result[s', b] = POP(unbacktracked[s'])
 *   else a &lt;- POP(untried[s'])
 *   s &lt;- s'
 *   return a
 * </pre>
 * 
 * Figure ?.?? An online search agent that uses depth-first exploration. The
 * agent is applicable only in state spaces in which every action can be
 * "undone" by some other action.<br>
 * 
 * @author Anurag Rai
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import aima.core.api.agent.Agent;
import aima.core.search.online.OnlineSearchProblem;
import aima.core.util.datastructure.Pair;
import aima.core.util.datastructure.TwoKeyHashMap;


public interface OnlineDFSAgent<A, P, S> extends Agent<A, P> {
	
	@Override
	default A perceive(P percept) {
		S sDelta = getPerceptToStateFunction().apply(percept);
		S s = getPreviousState();
		A a = getPreviousAction();
		// if GOAL-TEST(s') then return stop
		if ( getProblem().isGoalState(sDelta) ) {
			a = (A) null;
		}
		else {
			// if s' is a new state (not in untried) then 
			// untried[s'] <- ACTIONS(s')
			if (!getUntried().containsKey(sDelta)) {
				getUntried().put(sDelta, new ArrayList<>(getProblem().getActionsFunction().apply(sDelta)));
			}
			// if s is not null then do
			if ( null != s) {
				// If already seen the result of [s, a]
				// then don't put it back on the unbacktracked
				// list otherwise it can keep oscillating
				// between the same states endlessly.
				if (!(sDelta.equals(getResult().get(s, a)))) {
					// result[s, a] <- s'
					getResult().put(s, a, sDelta);
		
					// Ensure the unbacktracked always has a list for s'
					if (!getUnbacktracked().containsKey(sDelta)) {
						getUnbacktracked().put(sDelta, new ArrayList<S>());
					}
		
					// add s to the front of the unbacktracked[s']
					getUnbacktracked().get(sDelta).add(0, s);
				}
			}
			
			// if untried[s'] is empty then
			if ( getUntried().get(sDelta).isEmpty() ) {
				// if unbacktracked[s'] is empty then return stop
				if (getUnbacktracked().get(sDelta).isEmpty()) {
					a = (A) null;
				} 
				// else a <- an action b such that result[s', b] = POP(unbacktracked[s'])
				else {
					S popped = getUnbacktracked().get(sDelta).remove(0);
					for (Pair<S, A> sa : getResult().keySet()) {
						if (sa.getFirst().equals(sDelta) && getResult().get(sa).equals(popped)) {
							a = sa.getSecond();
							break;
						}
					}
				}
			} 
			else {
				a = getUntried().get(sDelta).remove(0);
			}
		}
		// s <- s'
		setPreviousState(sDelta);
		// return a
		setPreviousAction(a);
		return a;
	}
	
	Function<P,S> getPerceptToStateFunction();
	OnlineSearchProblem<A, S> getProblem();
	TwoKeyHashMap<S, A, S> getResult();
	Map<S, List<A>> getUntried();
	Map<S, List<S>> getUnbacktracked();
	A getPreviousAction();
	S getPreviousState();
	void setPreviousState(S s);
	void setPreviousAction(A a);
}
