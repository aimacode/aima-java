package aima.core.api.search.nondeterministic;

/**
 * <pre>
 * <code>
 * function OR-SEARCH(state, problem, path) returns a conditional plan, or failure
 * if problem.GOAL-TEST(state) then return the empty plan
 * if state is on path then return failure
 * for each action in problem.ACTIONS(state) do
 *     plan <- AND-SEARCH(RESULTS(state, action), problem, [state | path])
 *     if plan != failure then return [action | plan]
 * return failure
 * </code>
 * </pre>
 * 
 * @author Anurag Rai
 */

import java.util.List;
import java.util.function.Function;

import aima.core.search.nondeterministic.Plan;

public interface OrSearch<A, S> extends Function<NondeterministicProblem<A, S>,Plan<A>> {
	
	default Plan<A> apply(S state, NondeterministicProblem<A, S> problem, List<S> path ) {
		// if problem.GOAL-TEST(state) then return the empty plan
		if ( problem.isGoalState(state) )
			return new Plan<A>();
		// if state is on path then return failure
		if (path.contains(state) ) {
			return null;
		}
		// for each action in problem.ACTIONS(state) do
		for ( A action : problem.actions(state) ) {
			// plan <- AND-SEARCH(RESULTS(state, action), problem, [state|path])
			path.add(0,state);
			Plan<A> plan = andSearch().apply( problem.results(state, action), problem, path );
			// if plan != failure then return [action|plan]
			if ( plan != null ) {
				return plan;
			}
		}		
		// return failure
		return null;
	}
	
	AndSearch<A, S> andSearch();
}
