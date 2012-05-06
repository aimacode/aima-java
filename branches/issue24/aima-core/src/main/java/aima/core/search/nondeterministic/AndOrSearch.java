package aima.core.search.nondeterministic;

import aima.core.agent.Action;
import aima.core.search.framework.Metrics;
import java.util.Set;

/**
 * Implements an AND-OR search tree with conditional plans according to the
 * algorithm explained on pages 135-136 of AIMAv3. Unfortunately, this class
 * cannot implement the interface Search (core.search.framework.Search) because
 * Search.search() returns a list of Actions to perform, whereas a
 * nondeterministic search must return a Plan.
 *
 * <pre><code>
 * function AND-OR-GRAPH-SEARCH(problem) returns a conditional plan, or failure
 *  OR-SEARCH(problem.INITIAL-STATE, problem, [])
 *
 * function OR-SEARCH(state, problem, path) returns a conditional plan, or failure
 *  if problem.GOAL-TEST(state) then return the empty plan
 *  if state is on path then return failure
 *  for each action in problem.ACTIONS(state) do
 *      plan = AND-SEARCH(RESULTS(state, action), problem, [state|path])
 *      if plan != failure then return [action|plan]
 *  return failure
 *
 * function AND-SEARCH(states, problem, path) returns a conditional plan, or failure
 *  for each s_i in states do
 *      plan_i = OR-SEARCH(s_i, problem, path)
 *      if plan_i == failure then return failure
 *  return [if s_1 then plan_1 else ... if s_n-1 then plan_n-1 else plan_n]
 * </code></pre>
 *
 * @author Andrew Brown
 */
public class AndOrSearch {

    protected int expandedNodes;

    /**
     * Searches through state space and returns a conditional plan for the given
     * problem. The conditional plan is a list of either an action or an if-then
     * construct (consisting of a list of states and consequent actions). The
     * final product, when printed, resembles the contingency plan on page 134.
     *
     * This function is equivalent to the following on page 136:
     *
     * <pre><code>
     * function AND-OR-GRAPH-SEARCH(problem) returns a conditional plan, or failure
     *  OR-SEARCH(problem.INITIAL-STATE, problem, [])
     * </code></pre>
     *
     * @param problem
     * @return
     * @throws Exception
     */
    public Plan search(NondeterministicProblem problem) throws Exception {
        this.expandedNodes = 0;
        // OR-SEARCH(problem.INITIAL-STATE, problem, [])
        return this.orSearch(problem.getInitialState(), problem, new Path());
    }

    /**
     * Returns a conditional plan or null on failure; this function is
     * equivalent to the following on page 136:
     *
     * <pre><code>
     * function OR-SEARCH(state, problem, path) returns a conditional plan, or failure
     *  if problem.GOAL-TEST(state) then return the empty plan
     *  if state is on path then return failure
     *  for each action in problem.ACTIONS(state) do
     *      plan = AND-SEARCH(RESULTS(state, action), problem, [state|path])
     *      if plan != failure then return [action|plan]
     *  return failure
     * </code></pre>
     *
     * @param state
     * @param problem
     * @param path
     * @return
     */
    public Plan orSearch(Object state, NondeterministicProblem problem, Path path) {
        // do metrics
        this.expandedNodes++;
        // if problem.GOAL-TEST(state) then return the empty plan
        if (problem.isGoalState(state)) {
            return new Plan();
        }
        // if state is on path then return failure
        if (path.contains(state)) {
            return null;
        }
        // for each action in problem.ACTIONS(state) do
        for (Action action : problem.getActionsFunction().actions(state)) {
            // plan = AND-SEARCH(RESULTS(state, action), problem, [state|path])
            Plan plan = this.andSearch(problem.getResultsFunction().results(state, action), problem, path.prepend(state));
            // if plan != failure then return [action|plan]
            if (plan != null) {
                return plan.prepend(action);
            }
        }
        // return failure
        return null;
    }

    /**
     * Returns a conditional plan or null on failure; this function is
     * equivalent to the following on page 136:
     *
     * <pre><code>
     * function AND-SEARCH(states, problem, path) returns a conditional plan, or failure
     *  for each s_i in states do
     *      plan_i = OR-SEARCH(s_i, problem, path)
     *      if plan_i == failure then return failure
     *  return [if s_1 then plan_1 else ... if s_n-1 then plan_n-1 else plan_n]
     * </code></pre>
     *
     * @param states
     * @param problem
     * @param path
     * @return
     */
    public Plan andSearch(Set<Object> states, NondeterministicProblem problem, Path path) {
        // do metrics, setup
        this.expandedNodes++;
        Object[] _states = states.toArray();
        Plan[] plans = new Plan[_states.length];
        // for each s_i in states do
        for (int i = 0; i < _states.length; i++) {
            // plan_i = OR-SEARCH(s_i, problem, path)
            plans[i] = this.orSearch(_states[i], problem, path);
            // if plan_i == failure then return failure
            if (plans[i] == null) {
                return null;
            }
        }
        //return [if s_1 then plan_1 else ... if s_n-1 then plan_n-1 else plan_n]
        Plan plan = new Plan();
        if (plans.length > 0) {
            for (int i = 0; i < plans.length - 1; i++) {
                plan.append(new IfThen(_states[i], plans[i]));
            }
            plan.append(plans[plans.length - 1]);
        }
        return plan;
    }

    /**
     * Returns all the metrics of the node expander.
     *
     * @return all the metrics of the node expander.
     */
    public Metrics getMetrics() {
        Metrics result = new Metrics();
        result.set("expandedNodes", this.expandedNodes);
        return result;
    }
}
