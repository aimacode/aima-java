package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

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

public class BacktrackingStrategy implements SolutionStrategy {

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
			Variable var = selectUnassignedVariable(csp, assignment);
			for (Object value : orderDomainValues(var, assignment, csp)) {
				assignment.setAssignment(var, value);
				if (assignment.isConsistent(csp.getConstraints(var))) {
					List<VarValuePair> inferences = inference(var, assignment, csp);
					if (inferences != null) { // null denotes failure
						for(VarValuePair vvp : inferences)
							assignment.setAssignment(vvp.var, vvp.value);
					
						result = recursiveBackTrackingSearch(csp, assignment);
						if (result != null)
							break;
						
						for(VarValuePair vvp : inferences)
							assignment.removeAssignment(vvp.var);
					}
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
	protected Variable selectUnassignedVariable(CSP csp, Assignment assignment) {
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
	protected List<?> orderDomainValues(Variable var,
			Assignment assignment, CSP csp) {
		return csp.getDomain(var);
	}
	
	/**
	 * Primitive operation, which checking whether a solution is still possible
	 * and provides necessary extensions for the assignment. This default
	 * implementation just returns an empty extension list.
	 * @return List of variable value pairs or null denoting failure
	 *         (no solution possible).
	 */
	protected List<VarValuePair> inference(Variable var,
			Assignment assignment, CSP csp) {
		return new ArrayList<VarValuePair>(0);
	}
	
	
	// //////////////////////////////////////////////////////////////
	// inner classes
	
	protected static class VarValuePair {
		Variable var;
		Object value;
		protected VarValuePair(Variable var, Object value) {
			this.var = var;
			this.value = value;
		}
	}
}
