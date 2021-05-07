package aima.core.search.csp.solver;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Variable;
import aima.core.search.csp.solver.inference.InferenceLog;
import aima.core.util.Tasks;

import java.util.Optional;

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
 * @param <VAR> Type which is used to represent variables
 * @param <VAL> Type which is used to represent the values in the domains
 *
 * @author Ruediger Lunde
 */
public abstract class AbstractBacktrackingSolver<VAR extends Variable, VAL> extends CspSolver<VAR, VAL> {

    /** Applies a recursive backtracking search to solve the CSP. */
    public Optional<Assignment<VAR, VAL>> solve(CSP<VAR, VAL> csp) {
        Assignment<VAR, VAL> result = backtrack(csp, new Assignment<>());
        return result != null ? Optional.of(result) : Optional.empty();
    }

    /**
     * Template method, which can be configured by overriding the three
     * primitive operations below.
     * @return An assignment (possibly incomplete if task was cancelled) or null if no solution was found.
     */
    private Assignment<VAR, VAL> backtrack(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment) {
        Assignment<VAR, VAL> result = null;
        if (assignment.isComplete(csp.getVariables()) || Tasks.currIsCancelled()) {
            result = assignment;
        } else {
            VAR var = selectUnassignedVariable(csp, assignment);
            for (VAL value : orderDomainValues(csp, assignment, var)) {
                assignment.add(var, value);
                fireStateChanged(csp, assignment, var);
                if (assignment.isConsistent(csp.getConstraints(var))) {
                    InferenceLog<VAR, VAL> log = inference(csp, assignment, var);
                    if (!log.isEmpty())
                        fireStateChanged(csp, null, null);
                    if (!log.inconsistencyFound()) {
                        result = backtrack(csp, assignment);
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
    protected abstract VAR selectUnassignedVariable(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment);

    /**
     * Primitive operation, ordering the domain values of the specified variable.
     */
    protected abstract Iterable<VAL> orderDomainValues(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var);

    /**
     * Primitive operation, which tries to optimize the CSP representation with respect to a new assignment.
     *
     * @param var The variable which just got a new value in the assignment.
     * @return An object which provides information about
     * (1) whether changes have been performed,
     * (2) possibly inferred empty domains, and
     * (3) how to restore the original CSP.
     */
    protected abstract InferenceLog<VAR, VAL> inference(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var);
}
