package aima.core.agent.basic;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

import aima.core.agent.api.Agent;
import aima.core.search.api.OnlineSearchProblem;
import aima.core.util.datastructure.TwoKeyHashMap;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ?.??, page
 * ???.<br>
 * <br>
 * 
 * <pre>
 * function ONLINE-DFS-AGENT(s&prime;) returns an action
 *   inputs: s&prime;, a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               untried, a table that lists, for each state, the actions not yet tried
 *               unbacktracked, a table that lists, for each state, the backtracks not yet tried
 *               s, a, the previous state and action, initially null
 *    
 *   if GOAL-TEST(s&prime;) then return stop
 *   if s&prime; is a new state (not in untried) then untried[s&prime;] &larr; ACTIONS(s&prime;)
 *   if s is not null then
 *       result[s, a] &larr; s&prime;
 *       add s to the front of the unbacktracked[s&prime;]
 *   if untried[s&prime;] is empty then
 *       if unbacktracked[s&prime;] is empty then return stop
 *       else a &larr; an action b such that result[s&prime;, b] = POP(unbacktracked[s&prime;])
 *   else a &larr; POP(untried[s&prime;])
 *   s &larr; s&prime;
 *   return a
 * </pre>
 * 
 * Figure ?.?? An online search agent that uses depth-first exploration. The
 * agent is applicable only in state spaces in which every action can be
 * "undone" by some other action.<br>
 * 
 * @param <A>
 *            the type of actions the agent can take.
 * @param <P>
 *            the specific type of perceptual information the agent can perceive
 *            through its sensors.
 * @param <S>
 *            the type of the state space
 * 
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 * 
 */
public class OnlineDFSAgent<A, P, S> implements Agent<A, P> {
	// persistent: result, a table, indexed by state and action, initially empty
	private final TwoKeyHashMap<S, A, S> result = new TwoKeyHashMap<S, A, S>();
	// untried, a table that lists, for each state, the actions not yet tried
	private final Map<S, Deque<A>> untried = new HashMap<S, Deque<A>>();
	// unbacktracked, a table that lists, for each state, the backtracks not yet
	// tried
	private final Map<S, Deque<S>> unbacktracked = new HashMap<S, Deque<S>>();
	// s, a, the previous state and action, initially null
	private S s = null;
	private A a = null;

	// function ONLINE-DFS-AGENT(s&prime;) returns an action
	@Override
	public A perceive(P percept) {
		// s', a percept that identifies the current state
		S sPrime = identifyStateFor(percept);

		if (isGoalState(sPrime)) {
			return stop();
		}
		// if s' is a new state (not in untried) then untried[s'] <- ACTIONS(s')
		if (!untried.containsKey(sPrime)) {
			untried.put(sPrime, actions(sPrime));
		}
		// if s is not null then do
		if (s != null) {
			// result[s, a] <- s'
			result.put(s, a, sPrime);
			// add s to the front of the unbacktracked[s']
			unbacktracked.computeIfAbsent(sPrime, key -> new LinkedList<>()).push(s);
		}
		// if untried[s'] is empty then
		if (untried.get(sPrime).isEmpty()) {
			// if unbacktracked[s'] is empty then return stop
			if (unbacktracked.get(sPrime).isEmpty()) {
				return stop();
			}
			// else a <- an action b such that result[s',b] =
			// POP(unbacktracked[s'])
			S popped = unbacktracked.get(sPrime).pop();
			a = result.entrySet().stream()
					.filter(entry -> entry.getKey().getFirst().equals(sPrime) && entry.getValue().equals(popped))
					.findFirst().get().getKey().getSecond();
		} else {
			// else a <- POP(untried[s'])
			a = untried.get(sPrime).pop();
		}
		// s <- s'
		s = sPrime;
		return a;
	}

	//
	// Supporting Code
	private Function<P, S> identifyStateFunction;
	private OnlineSearchProblem<A, S> onlineProblem;
	private A stopAction;

	public OnlineDFSAgent(Function<P, S> identifyStateFunction, OnlineSearchProblem<A, S> onlineProblem, A stopAction) {
		this.identifyStateFunction = identifyStateFunction;
		this.onlineProblem = onlineProblem;
		this.stopAction = stopAction;
	}

	public S identifyStateFor(P percept) {
		return identifyStateFunction.apply(percept);
	}

	public boolean isGoalState(S state) {
		return onlineProblem.isGoalState(state);
	}

	public A stop() {
		return stopAction;
	}

	public Deque<A> actions(S state) {
		return new LinkedList<>(onlineProblem.actions(state));
	}
}