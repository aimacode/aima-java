package aima.core.search.basic.csp;

import java.util.List;

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
 *    var &larr; SELECT-UNASSIGNED-VARIABLE(assignment, csp)
 *    for each value in ORDER-DOMAIN-VALUES(var, assignment, csp) do
 *       if value is consistent with assignment then
 *          add {var = value} to assignment
 *          inferences &larr; INFERENCE(csp, var, value)
 *          if inferences &ne; failure then
 *             add inferences to assignment
 *             result &larr; BACKTRACK(assignment, csp)
 *             if result &ne; failure then
 *                return result
 *             remove inferences from assignment
 *          remove {var = value} from assignment
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

	// function BACKTRACKING-SEARCH(csp) returns a solution, or failure
	@Override
	public Assignment apply(CSP csp) {
		// return BACKTRACK({ }, csp)
		return backtrack(newAssignment(), csp);
	}

	// function BACKTRACK(assignment, csp) returns a solution, or failure
	public Assignment backtrack(Assignment assignment, CSP csp) {
		// if assignment is complete then return assignment
		if (assignment.isComplete(csp)) {
			return assignment;
		}
		// var <- SELECT-UNASSIGNED-VARIABLE(csp)
		String var = selectUnassignedVariable(assignment, csp);
		// for each value in ORDER-DOMAIN-VALUES(var, assignment, csp) do
		for (Object value : orderDomainValues(var, assignment, csp)) {
			// if value is consistent with assignment then
			if (assignment.isConsistent(var, value, csp)) {
				// add {var = value} to assignment
				assignment.add(var, value);
				// inferences <- INFERENCE(csp, var, value)
				Assignment inferences = inference(csp, var, value);
				// if inferences != failure then
				if (inferences != failure()) {
					// add inferences to assignment
					assignment.add(inferences);
					// result <- BACKTRACK(assignment, csp)
					Assignment result = backtrack(assignment, csp);
					// if result != failure then
					if (result != failure()) {
						// return result
						return result;
					}
					// remove inferences from assignment
					assignment.remove(inferences, csp);
				}
				// remove {var = value} from assignment
				assignment.remove(var, value);
			}
		}
		// return failure
		return failure();
	}

	//
	// Supporting Code
	public Assignment newAssignment() {
		return null; // TODO
	}

	public Assignment failure() {
		return null;
	}

	public String selectUnassignedVariable(Assignment assignment, CSP csp) {
		// Default implementation returns the first unassigned variable as
		// specified on the CSP
		return csp.getVariables().stream().filter(var -> !assignment.getAssignments().keySet().contains(var))
				.findFirst().get();
	}

	public List<Object> orderDomainValues(String var, Assignment assignment, CSP csp) {
		// Default implementation just returns the order of values as specified
		// on the CSP
		return csp.getDomains().get(csp.indexOf(var)).getValues();
	}

	public Assignment inference(CSP csp, String var, Object value) {
		// Default is no inference.
		return newAssignment();
	}
}