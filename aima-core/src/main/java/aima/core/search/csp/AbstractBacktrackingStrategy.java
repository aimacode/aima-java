package aima.core.search.csp;

import aima.core.search.csp.inference.InferenceLog;
import aima.core.util.CancelableThread;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.5, Page 215.<br>
 * <br>
 * <p>
 * <pre>
 * <code>
 * function BACKTRACKING-SEARCH(csp) returns a solution, or failure
 *    return BACKTRACK({ }, csp)
 *
 * function BACKTRACK(assignment, csp) returns a solution, or failure
 *    if assignment is complete then return assignment
 *    var = SELECT-UNASSIGNED-VARIABLE(csp)
 *    for each value in ORDER-DOMAIN-VALUES(var, assignment, csp) do
 *       if value is consistent with assignment then
 *          add {var = value} to assignment
 *          inferences = INFERENCE(csp, var, value)
 *          if inferences != failure then
 *             add inferences to assignment
 *             result = BACKTRACK(assignment, csp)
 *             if result != failure then
 *                return result
 *          remove {var = value} and inferences from assignment
 *    return failure
 * </code>
 * </pre>
 * <p>
 * Figure 6.5 A simple backtracking algorithm for constraint satisfaction
 * problems. The algorithm is modeled on the recursive depth-first search of
 * Chapter 3. By varying the functions SELECT-UNASSIGNED-VARIABLE and
 * ORDER-DOMAIN-VALUES, we can implement the general-purpose heuristic discussed
 * in the text. The function INFERENCE can optionally be used to impose arc-,
 * path-, or k-consistency, as desired. If a value choice leads to failure
 * (noticed wither by INFERENCE or by BACKTRACK), then value assignments
 * (including those made by INFERENCE) are removed from the current assignment
 * and a new value is tried.
 *
 * @author Ruediger Lunde
 */
public abstract class AbstractBacktrackingStrategy extends SolutionStrategy {

    /** Applies a recursive backtracking search to solve the CSP. */
    public Assignment solve(CSP csp) {
        return recursiveBackTrackingSearch(csp, new Assignment());
    }

    /**
     * Template method, which can be configured by overriding the three
     * primitive operations below.
     */
    private Assignment recursiveBackTrackingSearch(CSP csp,
                                                   Assignment assignment) {
        Assignment result = null;
        if (assignment.isComplete(csp.getVariables()) || CancelableThread.currIsCanceled()) {
            result = assignment;
        } else {
            Variable var = selectUnassignedVariable(assignment, csp);
            for (Object value : orderDomainValues(var, assignment, csp)) {
                assignment.add(var, value);
                fireStateChanged(assignment, csp);
                if (assignment.isConsistent(csp.getConstraints(var))) {
                    InferenceLog log = inference(var, assignment, csp);
                    if (!log.isEmpty())
                        fireStateChanged(csp);
                    if (!log.inconsistencyFound()) {
                        result = recursiveBackTrackingSearch(csp, assignment);
                        if (result != null)
                            break;
                    }
                    log.undo(csp);
                }
                assignment.remove(var);
            }
        }
        return result;
    }

    /**
     * Primitive operation, selecting a not yet assigned variable.
     */
    protected abstract Variable selectUnassignedVariable(Assignment assignment, CSP csp);

    /**
     * Primitive operation, ordering the domain values of the specified variable.
     */
    protected abstract Iterable<Object> orderDomainValues(Variable var, Assignment assignment, CSP csp);

    /**
     * Primitive operation, which tries to optimize the CSP representation with respect to a new assignment.
     */
    protected abstract InferenceLog inference(Variable var, Assignment assignment, CSP csp);
}
