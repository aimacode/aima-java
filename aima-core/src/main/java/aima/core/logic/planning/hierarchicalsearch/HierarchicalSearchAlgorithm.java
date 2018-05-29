package aima.core.logic.planning.hierarchicalsearch;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;
import aima.core.logic.planning.State;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 11.5, page
 * 409.<br>
 * <br>
 *
 * <pre>
 * function HIERARCHICAL-SEARCH(problem, hierarchy) returns a solution, or failure
 *  frontier ← a FIFO queue with [Act] as the only element
 *  loop do
 *    if EMPTY?(frontier) then return failure
 *    plan ← POP(frontier) / chooses the shallowest plan in frontier /
 *    hla ← the first HLA in plan,or null if none
 *    prefix,suffix ← the action subsequences before and after hla in plan
 *    outcome ← RESULT(problem.INITIAL-STATE,prefix)
 *    if hla is null then / so plan is primitive and outcome is its result /
 *      if outcome satisfies problem.GOAL then return plan
 *    else for each sequence in REFINEMENTS(hla,outcome,hierarchy)do
 *      frontier ← INSERT(APPEND(prefix,sequence,suffix),frontier)
 * </pre>
 * <p>
 * Figure 9.3 A breadth-first implementation of hierarchical forward planning
 * search. The initial plan supplied to the algorithm is [Act]. The REFINEMENTS
 * function returns a set of action sequences, one for each refinement of the
 * HLA whose preconditions are satisfied by the specified state, outcome.
 *
 * @author samagra
 */
public class HierarchicalSearchAlgorithm {

    /**
     * function HIERARCHICAL-SEARCH(problem, hierarchy) returns a solution, or failure
     *
     * @param problem The planning problem.
     * @return A list of actions representing the plan.
     */
    public List<ActionSchema> heirarchicalSearch(Problem problem) {
        // frontier ← a FIFO queue with [Act] as the only element
        Queue<List<ActionSchema>> frontier = new LinkedList<>();
        frontier.add(new ArrayList<>(Collections.singletonList(PlanningProblemFactory.getHlaAct(problem))));
        // loop do
        while (true) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty())
                return null;
            // plan ← POP(frontier) /* chooses the shallowest plan in frontier */
            List<ActionSchema> plan = frontier.poll();
            // hla ← the first HLA in plan, or null if none
            int i = 0;
            ActionSchema hla;
            while (i < plan.size() && !(plan.get(i) instanceof HighLevelAction))
                i++;
            if (i < plan.size())
                hla = plan.get(i);
            else
                hla = null;
            // prefix,suffix ← the action subsequences before and after hla in plan
            List<ActionSchema> prefix = new ArrayList<>();
            List<ActionSchema> suffix = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                prefix.add(plan.get(j));
            }
            for (int j = i + 1; j < plan.size(); j++) {
                suffix.add(plan.get(j));
            }
            // outcome ← RESULT(problem.INITIAL-STATE, prefix)
            State outcome = problem.getInitialState().result(prefix);
            // if hla is null then /* so plan is primitive and outcome is its result */
            if (hla == null) {
                // if outcome satisfies problem.GOAL then return plan
                if (outcome.getFluents().containsAll(problem.getGoalState().getFluents()))
                    return plan;
            } else {
                List<ActionSchema> tempInsertionList = new ArrayList<>();
                // else for each sequence in REFINEMENTS(hla, outcome, hierarchy) do
                for (List<ActionSchema> sequence :
                        refinements(hla, outcome)) {
                    // frontier ← INSERT(APPEND(prefix, sequence, suffix), frontier)
                    tempInsertionList.clear();
                    tempInsertionList.addAll(prefix);
                    tempInsertionList.addAll(sequence);
                    tempInsertionList.addAll(suffix);
                    ((LinkedList<List<ActionSchema>>) frontier).addLast(new ArrayList<>(tempInsertionList));
                }
            }
        }
    }

    /**
     * The REFINEMENTS function returns a set of action sequences,
     * one for each refinement of the HLA whose preconditions are
     * satisfied by the specified state, outcome.
     *
     * @param hla     The hla to which the refinements are to be applied.
     * @param outcome The state in which the refinements are to be applied.
     * @return List of all refinements of the current hla in a given outcome state.
     */
    public List<List<ActionSchema>> refinements(ActionSchema hla, State outcome) {
        List<List<ActionSchema>> result = new ArrayList<>();
        for (List<ActionSchema> refinement :
                ((HighLevelAction) hla).getRefinements()) {
            if (refinement.size() > 0) {
                if (outcome.isApplicable(refinement.get(0)))
                    result.add(refinement);
            } else
                result.add(refinement);
        }
        return result;
    }
}
