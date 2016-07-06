package aima.core.agent.basic;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import aima.core.agent.api.Agent;
import aima.core.search.api.OnlineSearchProblem;
import aima.core.util.datastructure.TwoKeyLookup;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ???.
 * <br>
 * <br>
 * 
 * <pre>
 * function LRTA*-AGENT(s&prime;) returns an action
 *   inputs: s&prime;, a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               H, a table of cost estimates indexed by state, initially empty
 *               s, a, the previous state and action, initially null
 *           
 *   if GOAL-TEST(s&prime;) then return stop
 *   if s&prime; is a new state (not in H) then H[s&prime;] &larr; h(s&prime;)
 *   if s is not null
 *     result[s, a] &larr; s&prime;
 *     H[s] &larr;        min LRTA*-COST(s, b, result[s, b], H)
 *             b &isin; ACTIONS(s)
 *   a &larr; an action b in ACTIONS(s&prime;) that minimizes LRTA*-COST(s&prime;, b, result[s&prime;, b], H)
 *   s &larr; s&prime;
 *   return a
 *   
 * function LRTA*-COST(s, a, s&prime;, H) returns a cost estimate
 *   if s&prime; is undefined then return h(s)
 *   else return c(s, a, s&prime;) + H[s&prime;]
 * </pre>
 * 
 * Figure ?.?? LRTA*-AGENT selects an action according to the value of
 * neighboring states, which are updated as the agent moves about the state
 * space.<br>
 * <br>
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
 * @author Mike Stampone
 */
public class LRTAStarAgent<A, P, S> implements Agent<A, P> {
	// persistent: result, a table, indexed by state and action, initially empty
	protected final TwoKeyLookup<S, A, S> result = new TwoKeyLookup<S, A, S>();
	// H, a table of cost estimates indexed by state, initiallly empy
	protected final Map<S, Double> H = new HashMap<S, Double>();
	// s, a, the previous state and action, initially null
	protected S s = null;
	protected A a = null;

	// function LRTA*-AGENT(s&prime;) returns an action
	@Override
	public A perceive(P percept) {
		// inputs: s', a percept that identifies the current state
		S sPrime = identifyStateFor(percept);
		// if GOAL-TEST(s') then return stop
		if (isGoalState(sPrime)) {
			return stop();
		}
		// if s' is a new state (not in H) then H[s'] <- h(s')
		H.computeIfAbsent(sPrime, state -> h.applyAsDouble(state));
		// if s is not null
		if (s != null) {
			// result[s, a] <- s'
			result.put(s, a, sPrime);
			// H[s] <- min LRTA*-COST(s, b, result[s, b], H)
			// b (element of) ACTIONS(s)
			H.put(s, actions(s).stream().mapToDouble(b -> lrtaCost(s, b, result.get(s, b), H)).min().getAsDouble());
		}
		// a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b,
		// result[s', b], H)
		a = actions(sPrime).stream().min(Comparator.comparingDouble(b -> lrtaCost(sPrime, b, result.get(sPrime, b), H)))
				.get();
		// s <- s'
		s = sPrime;
		return a;
	}

	// function LRTA*-COST(s, a, s', H) returns a cost estimate
	public double lrtaCost(S s, A a, S sPrime, Map<S, Double> H) {
		// if s' is undefined then return h(s)
		if (null == sPrime) {
			return h.applyAsDouble(s);
		}
		// else return c(s, a, s') + H[s']
		return onlineProblem.stepCost(s, a, sPrime) + H.get(sPrime);
	}

	//
	// Supporting Code
	protected OnlineSearchProblem<A, S> onlineProblem;
	protected ToDoubleFunction<S> h;
	protected Function<P, S> identifyStateFunction;
	protected A stopAction;

	public LRTAStarAgent(OnlineSearchProblem<A, S> onlineProblem, ToDoubleFunction<S> h,
			Function<P, S> identifyStateFunction) {
		this(onlineProblem, h, identifyStateFunction, null);
	}

	public LRTAStarAgent(OnlineSearchProblem<A, S> onlineProblem, ToDoubleFunction<S> h,
			Function<P, S> identifyStateFunction, A stopAction) {
		this.identifyStateFunction = identifyStateFunction;
		this.h = h;
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

	public List<A> actions(S state) {
		return onlineProblem.actions(state);
	}
}