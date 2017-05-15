package aima.core.search.csp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aima.core.search.csp.inference.DomainLog;
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
 * @author Ruediger Lunde
 * 
 */
public class TreeCSPSolver<VAR extends Variable, VAL> extends SolutionStrategy<VAR, VAL> {

	@Override
	public Assignment<VAR, VAL> solve(CSP<VAR, VAL> csp) {

		Assignment<VAR, VAL> assignment = new Assignment<>();
		// Get the list of Variables from CSP to calculate the size
		List<VAR> l = csp.getVariables();
		// Calculate the size
		int n = l.size();
		// Select a random root from the List of Vaiables
		VAR root = Util.selectRandomlyFromList(l);
		// Sort the variables in topological order
		l = topologicalSort(csp, l, root);

		DomainLog log = new DomainLog();

		for (int i = n - 1; i >= 1; i--) {
			VAR var = l.get(i);
			// get constraints to find the parent
			for (Constraint<VAR, VAL> constraint : csp.getConstraints(var)) {
				if (constraint.getScope().size() == 2) {
					// if the neighbour is parent
					if (csp.getNeighbor(var, constraint) == l.get(parent[i])) {
						// make it Arc Consistent
						if (makeArcConsistent(l.get(parent[i]), var, constraint, csp, log)) {
							if (csp.getDomain(l.get(parent[i])).isEmpty()) {
								log.setEmptyDomainFound(true);
								return null;
							}
						}
					}
				}
			}
		}
		boolean assignment_consistent = false;
		for (int i = 0; i < n; i++) {
			VAR var = l.get(i);
			assignment_consistent = false;
			for (VAL value : csp.getDomain(var)) {
				assignment.add(var, value);
				if (assignment.isConsistent(csp.getConstraints(var))) {
					assignment_consistent = true;
					break;
				}
			}
			if (!assignment_consistent) {
				return null;
			}
		}
		return assignment;
	}

	//
	// Supporting Code
	protected int[] parent;

	// Since the graph is a tree, topologicalSort is:
	// Level order traversal of the tree OR BFS on tree OR Pre-oder
	protected List<VAR> topologicalSort(CSP<VAR, VAL> csp, List<VAR> l, VAR root) {
		// Track the parents
		parent = new int[l.size()];
		
		List<VAR> result = new ArrayList<>();
		Queue<VAR> q = new LinkedList<>(); // FIFO-Queue

		int i = 1;
		int parent_index = 0;
		int node_count = 0;
		q.add(root);

		while (!q.isEmpty()) {

			node_count = q.size(); // get number of nodes in the level

			while (node_count > 0) {

				VAR var = q.remove();
				result.add(var);
				// for each binary constraint of the Variable
				for (Constraint<VAR, VAL> constraint : csp.getConstraints(var)) {
					VAR neighbour = csp.getNeighbor(var, constraint);
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

	protected boolean makeArcConsistent(VAR xi, VAR xj, Constraint<VAR, VAL> constraint, CSP<VAR, VAL> csp,
			DomainLog<VAR, VAL> log) {
		boolean revised = false;
		Assignment<VAR, VAL> assignment = new Assignment<>();
		for (VAL iValue : csp.getDomain(xi)) {
			assignment.add(xi, iValue);
			boolean consistentExtensionFound = false;
			for (VAL jValue : csp.getDomain(xj)) {
				assignment.add(xj, jValue);
				if (constraint.isSatisfiedWith(assignment)) {
					consistentExtensionFound = true;
					break;
				}
			}
			if (!consistentExtensionFound) {
				log.storeDomainFor(xi, csp.getDomain(xi));
				csp.removeValueFromDomain(xi, iValue);
				revised = true;
			}
		}
		return revised;
	}
}
