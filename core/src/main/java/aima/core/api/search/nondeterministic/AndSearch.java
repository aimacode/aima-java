package aima.core.api.search.nondeterministic;

/**
 * <pre>
 * <code>
 * function AND-SEARCH(states, problem, path) returns a conditional plan, or failure
 * for each s<sub>i</sub> in states do
 *    plan<sub>i</sub> <- OR-SEARCH(s<sub>i</sub>, problem, path)
 *    if plan<sub>i</sub> = failure then return failure
 * return [if s<sub>1</sub> then plan<sub>1</sub> else if s<sub>2</sub> then plan<sub>2</sub> else ... if s<sub>n-1</sub> then plan<sub>n-1</sub> else plan<sub>n</sub>]
 * </code>
 * </pre>
 * 
 * @author Anurag Rai
 */

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import aima.core.api.agent.Action;
import aima.core.search.nondeterministic.IfStateThenPlan;
import aima.core.search.nondeterministic.Plan;

public interface AndSearch<S> extends Function<Plan, List<Action>> {
	
	
	default Plan apply(Set<S> states, NondeterministicProblem<S> problem, List<S> path) {
		@SuppressWarnings("unchecked")
		S[] _states = (S[]) states.toArray();
		//List<List<Action>> plans = new ArrayList<>();
		Plan[] plans = new Plan[_states.length];
		// for each s_i in states do
		for (int i = 0; i < _states.length; i++) {
			// plan_i <- OR-SEARCH(s_i, problem, path)
			plans[i] = orSearch().apply(_states[i], problem, path);
			// if plan_i = failure then return failure
			if ( plans[i] == null) {
				return null;
			}
		}
		// return [if s_1 then plan_1 else ... if s_n-1 then plan_n-1 else
		// plan_n]
		Object[] steps = new Object[plans.length];
		if (plans.length > 0) {
			for (int i = 0; i < plans.length - 1; i++) {
				steps[i] = new IfStateThenPlan(_states[i], plans[i]);
			}
			steps[steps.length-1] = plans[plans.length - 1];
		}
		
		return new Plan(steps);
	}
	
	OrSearch<S> orSearch();
}
