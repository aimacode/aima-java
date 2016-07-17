package aima.core.search.basic.csp;

import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.api.SearchForAssignmentFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Ed.): Figure ??, Page ??.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function BACKTRACKING-SEARCH(csp) returns a solution, or failure
 *    return BACKTRACK({ }, csp)
 * 
 * function BACKTRACK(assignment, csp) returns a solution, or failure
 *    if assignment is complete then return assignment
 *    var &larr; SELECT-UNASSIGNED-VARIABLE(csp)
 *    for each value in ORDER-DOMAIN-VALUES(var, assignment, csp) do
 *       if value is consistent with assignment then
 *          add {var = value} to assignment
 *          inferences &larr; INFERENCE(csp, var, value)
 *          if inferences &ne; failure then
 *             add inferences to assignment
 *             result &larr; BACKTRACK(assignment, csp)
 *             if result &ne; failure then
 *                return result
 *       remove {var = value} and inferences from assignment
 *    return failure
 * </code>
 * </pre>
 * 
 * Figure ?? A simple backtracking algorithm for constraint satisfaction
 * problems. The algorithm is modeled on the recursive depth-first search of
 * Chapter 3. By varying the functions SELECT-UNASSIGNED-VARIABLE and
 * ORDER-DOMAIN-VALUES, we can implement the general-purpose heuristic discussed
 * in the text. The function INFERENCE can optionally be used to impose arc-,
 * path-, or k-consistency, as desired. If a value choice leads to failure
 * (noticed either by INFERENCE or by BACKTRACK), then value assignments
 * (including those made by INFERENCE) are removed from the current assignment
 * and a new value is tried.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 *
 */
public class BacktrackingSearch implements SearchForAssignmentFunction {

	@Override
	public Assignment apply(CSP csp) {
		return null; // TODO
	}
}
