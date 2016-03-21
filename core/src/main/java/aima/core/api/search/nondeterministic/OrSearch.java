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
 * @author Anurag Rai
 */

import java.util.List;
import java.util.function.Function;

import aima.core.api.agent.Action;
import aima.core.search.nondeterministic.Plan;

public interface OrSearch<S> extends Function<NondeterministicProblem<S>,Plan> {
	
	default Plan apply(S state, NondeterministicProblem<S> problem, List<S> path ) {
		// if problem.GOAL-TEST(state) then return the empty plan
		if ( problem.isGoalState(state) )
			return new Plan();
		// if state is on path then return failure
		if (path.contains(state) ) {
			return null;
		}
		// for each action in problem.ACTIONS(state) do
		for ( Action action : problem.actions(state) ) {
			// plan <- AND-SEARCH(RESULTS(state, action), problem, [state|path])
			path.add(0,state);
			Plan plan = andSearch().apply( problem.results(state, action), problem, path );
			// if plan != failure then return [action|plan]
			if ( plan != null ) {
				return plan;
			}
		}		
		// return failure
		return null;
	}
	
	AndSearch<S> andSearch();
}
