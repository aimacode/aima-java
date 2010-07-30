package aima.core.search.csp;

/**
 *  Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.5, Page 220.
 * 
 * function BACKTRACKING-SEARCH(csp) returns a solution, or failure
 *    return BACKTRACK({ }, csp)
 * <pre><code>
 * function BACKTRACK(assignment , csp) returns a solution, or failure
 *    if assignment is complete then return assignment
 *    var = SELECT-UNASSIGNED-VARIABLE(csp)
 *    for each value in ORDER-DOMAIN-VALUES(var , assignment , csp) do
 *       if value is consistent with assignment then
 *          add {var = value} to assignment
 *          inferences = INFERENCE(csp, var , value)
 *          if inferences 6= failure then
 *             add inferences to assignment
 *             result = BACKTRACK(assignment , csp)
 *             if result 6= failure then
 *                return result
 *          remove {var = value} and inferences from assignment
 *    return failure
 * </code></pre>
 * 
 * @author Ruediger Lunde
 */

public class BacktrackingStrategy implements SolutionStrategy {
	
	public Assignment solve(CSP csp) {
		return recursiveBackTrackingSearch(csp, new Assignment());
	}

	private Assignment recursiveBackTrackingSearch(CSP csp, Assignment assignment) {
		Assignment result = null;
		if (assignment.isComplete(csp.getVariables())) {
			result = assignment;
		} else {
			Variable var = selectFirstUnassignedVariable(csp, assignment);
			for (Object value : csp.getDomain(var)) {
				assignment.setAssignment(var, value);
				if (assignment.isConsistent(csp.getConstraints(var))) {
					result = recursiveBackTrackingSearch(csp, assignment);
					if (result != null)
						break;
				}
				assignment.removeAssignment(var);
			}
		}
		return result;
	}
	
	private Variable selectFirstUnassignedVariable(CSP csp, Assignment assignment) {
		for (Variable var : csp.getVariables()) {
			if (!(assignment.hasAssignmentFor(var)))
				return var;
		}
		return null;
	}
}
