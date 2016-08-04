package aima.core.search.basic.csp;

import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.api.SearchForAssignmentFunction;
import aima.core.search.basic.support.BasicAssignment;

/**
 * 
 * Artificial Intelligence A Modern Approach (4th Ed.): Figure ??, Page ??.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function TREE-CSP-SOLVER(csp) returns a solution, or failure
 *    inputs: csp, a CSP with components X, D, C
 * 
 *    n &larr; number of variables in X
 *    assignment &larr; an empty assignment
 *    root  &larr; any variable in X
 *    X &larr; TOPOLOGICALSORT(X, root)
 *    for j = n down to 2 do
 *       MAKE-ARC-CONSISTENT(PARENT(X<sub>j</sub>), X<sub>j</sub>)
 *       if it cannot be made consistent then return failure
 *    for i = 1 to n do
 *       assignment[X<sub>i</sub>] &larr; any consistent value from D<sub>i</sub>
 *       if there is no consistent value then return failure
 *    return assignment
 * </code>
 * 
 * <pre>
 * 
 * Figure ?? The TREE-CSP-SOLVER algorithm for solving tree-structured CSPs. If
 * the CSP has a solution, we will find it in linear time; if not, we will
 * detect a contradiction.
 * 
 * @author Anurag Rai
 * 
 */
public class TreeCSPSolver implements SearchForAssignmentFunction {

	// function TREE-CSP-SOLVER(csp) returns a solution, or failure
	// inputs: csp, a CSP with components X, D, C
	@Override
	public Assignment apply(CSP csp) {
		// n <- number of variables in X
		int n = csp.getVariables().size();
		// assignment <- an empty assignment
		Assignment assignment = newEmptyAssignment();
		// root  <- any variable in X
		String root = anyVariable(csp);
		// X <- TOPOLOGICALSORT(X, root)
// TODO
		// for j = n down to 2 do
		for (int j = n-1; j >= 1; j--) {
			// MAKE-ARC-CONSISTENT(PARENT(X<sub>j</sub>), X<sub>j</sub>)
// TODO
			// if it cannot be made consistent then return failure
			if (csp.isInconsistent()) {
				return failure();
			}
		}
		// for i = 1 to n do
		for (int i = 0; i < n; i++) {
			// assignment[X<sub>i</sub>] <- any consistent value from D<sub>i</sub>
// TODO
			// if there is no consistent value then return failure
			if (!assignment.isConsistent(csp)) {
				return failure();
			}
		}
		
		return assignment;
	}
	
	//
	// Supporting Code
	public Assignment newEmptyAssignment() {
		return new BasicAssignment();
	}
	
	public String anyVariable(CSP csp) {
		return csp.getVariables().get(0);
	}
	
	public Assignment failure() {
		return null;
	}
}
