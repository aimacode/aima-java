package aima.core.search.csp;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.5, Page 220.
 * 
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
 * 
 * @author Ruediger Lunde
 */

public class BacktrackingStrategy extends SolutionStrategy {

	public Assignment solve(CSP csp) {
		return recursiveBackTrackingSearch(csp, new Assignment());
	}

	/**
	 * Template method, which can be configured by overriding the three primitive
	 * operations below.
	 */
	private Assignment recursiveBackTrackingSearch(CSP csp,
			Assignment assignment) {
		Assignment result = null;
		if (assignment.isComplete(csp.getVariables())) {
			result = assignment;
		} else {
			Variable var = selectUnassignedVariable(assignment, csp);
			for (Object value : orderDomainValues(var, assignment, csp)) {
				assignment.setAssignment(var, value);
				fireStateChanged(assignment);
				if (assignment.isConsistent(csp.getConstraints(var))) {
					CSP savedCSP = csp;
					csp = inference(var, assignment, csp);
					if (csp != savedCSP)
						fireStateChanged(csp);
					if (csp != null) { // null denotes failure
						result = recursiveBackTrackingSearch(csp, assignment);
						if (result != null)
							break;
					}
					csp = savedCSP;
				}
				assignment.removeAssignment(var);
			}
		}
		return result;
	}

	/**
	 * Primitive operation, selecting a not yet assigned variable. This default
	 * implementation just selects the first in the ordered list of variables
	 * provided by the CSP.
	 */
	protected Variable selectUnassignedVariable(Assignment assignment, CSP csp) {
		for (Variable var : csp.getVariables()) {
			if (!(assignment.hasAssignmentFor(var)))
				return var;
		}
		return null;
	}

	/**
	 * Primitive operation, ordering the domain values of the specified
	 * variable. This default implementation just takes the default order
	 * provided by the CSP.
	 */
	protected Iterable<?> orderDomainValues(Variable var,
			Assignment assignment, CSP csp) {
		return csp.getDomain(var);
	}
	
	/**
	 * Primitive operation, which tries to prune out values from the
	 * CSP which are not possible anymore when extending the given assignment
	 * to a solution. This default implementation just returns the original CSP.
	 * @return A reduced copy of the original CSP or null denoting failure
	 *         (assignment cannot be extended to a solution).
	 */
	protected CSP inference(Variable var,
			Assignment assignment, CSP csp) {
		return csp;
	}
}
