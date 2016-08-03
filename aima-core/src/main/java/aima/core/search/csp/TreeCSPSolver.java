package aima.core.search.csp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aima.core.util.Util;

/**
 * 
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.11, Page
 * 224.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function TREE-CSP-SOLVER(csp) returns a solution, or failure
 * 		inputs: csp, a CSP with components X, D, C
 * 		n &larr; number of variables in X
 * 		assignment &larr; an empty assignment
 * 		root  &larr; any variable in X
 * 		X &larr; TOPOLOGICALSORT(X, root )
 * 		for j = n down to 2 do
 * 			MAKE-ARC-CONSISTENT(PARENT(Xj),Xj )
 * 			if it cannot be made consistent then return failure
 * 		for i = 1 to n do
 * 			assignment[Xi] &larr; any consistent value from Di
 * 			if there is no consistent value then return failure
 * 		return assignment
 * </code>
 * 
 * <pre>
 * 
 * Figure 6.11 The TREE-CSP-SOLVER algorithm for solving tree-structured CSPs.
 * If the CSP has a solution, we will find it in linear time; if not, we will
 * detect a contradiction.
 * 
 * @author Anurag Rai
 * 
 */
public class TreeCSPSolver extends SolutionStrategy {

	@Override
	public Assignment solve(CSP csp) {

		Assignment assignment = new Assignment();
		// Get the list of Variables from CSP to calculate the size
		List<Variable> l = csp.getVariables();
		// Calculate the size
		int n = l.size();
		// Select a random root from the List of Vaiables
		Variable root = Util.selectRandomlyFromList(l);
		// Sort the variables in topological order
		l = topologicalSort(csp, l, root);

		DomainRestoreInfo info = new DomainRestoreInfo();

		for (int i = n - 1; i >= 1; i--) {
			Variable var = l.get(i);
			// get constraints to find the parent
			for (Constraint constraint : csp.getConstraints(var)) {
				if (constraint.getScope().size() == 2) {
					// if the neighbour is parent
					if (csp.getNeighbor(var, constraint) == l.get(parent[i])) {
						// make it Arc Consistent
						if (makeArcConsistent(l.get(parent[i]), var, constraint, csp, info)) {
							if (csp.getDomain(l.get(parent[i])).isEmpty()) {
								info.setEmptyDomainFound(true);
								assignment = null;
								return assignment;
							}
						}
					}
				}
			}
		}
		boolean assignment_consistent = false;
		for (int i = 0; i < n; i++) {
			Variable var = l.get(i);
			assignment_consistent = false;
			for (Object value : csp.getDomain(var)) {
				assignment.setAssignment(var, value);
				if (assignment.isConsistent(csp.getConstraints(var))) {
					assignment_consistent = true;
					break;
				}
			}
			if (!assignment_consistent) {
				assignment = null;
				return assignment;
			}
		}
		return assignment;
	}

	//
	// Supporting Code
	protected int[] parent;

	// Since the graph is a tree, topologicalSort is:
	// Level order traversal of the tree OR BFS on tree OR Pre-oder
	protected List<Variable> topologicalSort(CSP csp, List<Variable> l, Variable root) {
		// Track the parents
		parent = new int[l.size()];
		
		List<Variable> result = new ArrayList<>();
		Queue<Variable> q = new LinkedList<>(); // FIFO-Queue

		int i = 1;
		int parent_index = 0;
		int node_count = 0;
		q.add(root);

		while (!q.isEmpty()) {

			node_count = q.size(); // get number of nodes in the level

			while (node_count > 0) {

				Variable var = q.remove();
				result.add(var);
				// for each binary constraint of the Variable
				for (Constraint constraint : csp.getConstraints(var)) {
					Variable neighbour = csp.getNeighbor(var, constraint);
					// check if neighbour is root
					if (result.contains(neighbour))
						continue;
					parent[i] = parent_index;
					i++;
					q.add(neighbour);
				}
				node_count--;
				parent_index++;
			}
		}
		return result;
	}

	protected boolean makeArcConsistent(Variable xi, Variable xj, Constraint constraint, CSP csp,
			DomainRestoreInfo info) {
		boolean revised = false;
		Assignment assignment = new Assignment();
		for (Object iValue : csp.getDomain(xi)) {
			assignment.setAssignment(xi, iValue);
			boolean consistentExtensionFound = false;
			for (Object jValue : csp.getDomain(xj)) {
				assignment.setAssignment(xj, jValue);
				if (constraint.isSatisfiedWith(assignment)) {
					consistentExtensionFound = true;
					break;
				}
			}
			if (!consistentExtensionFound) {
				info.storeDomainFor(xi, csp.getDomain(xi));
				csp.removeValueFromDomain(xi, iValue);
				revised = true;
			}
		}
		return revised;
	}
}
