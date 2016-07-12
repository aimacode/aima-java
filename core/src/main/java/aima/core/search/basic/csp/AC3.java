package aima.core.search.basic.csp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import aima.core.search.api.CSP;
import aima.core.search.api.Constraint;

/**
 * 
 * Artificial Intelligence A Modern Approach (4th Ed.): Figure ??, Page ??.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function AC-3(csp) returns false if an inconsistency is found and true otherwise
 *    inputs: csp, a binary CSP with components (X, D, C)
 *    local variables: queue, a queue of arcs, initially all the arcs in csp
 *    
 *    while queue is not empty do
 *       (X<sub>i</sub>, X<sub>j</sub>) = REMOVE-FIRST(queue)
 *       if REVISE(csp, X<sub>i</sub>, X<sub>j</sub>) then
 *          if size of D<sub>i</sub> = 0 then return false
 *          for each X<sub>k</sub> in X<sub>i</sub>.NEIGHBORS - {X<sub>j</sub>} do
 *             add (X<sub>k</sub>, X<sub>i</sub>) to queue
 *    return true
 * 
 * function REVISE(csp, X<sub>i</sub>, X<sub>j</sub>) returns true iff we revise the domain of X<sub>i</sub>
 *    revised &larr; false
 *    for each x in D<sub>i</sub> do
 *       if no value y in D<sub>j</sub> allows (x ,y) to satisfy the constraint between X<sub>i</sub> and X<sub>j</sub> then
 *          delete x from D<sub>i</sub>
 *          revised &larr; true
 *    return revised
 * </code>
 * </pre>
 * 
 * Figure ?? The arc-consistency algorithm AC-3. After applying AC-3, either
 * every arc is arc-consistent, or some variable has an empty domain, indicating
 * that the CSP cannot be solved. The name "AC-3" was used by the algorithm's
 * inventor (Mackworth, 1977) because it's the third version developed in the
 * paper.<br>
 * <br>
 * NOTE: this implementation when testing for arc-consistency can have the side
 * effect of reducing the domains associated with the CSP, whether or not the
 * CSP is found to be consistent or not.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class AC3 implements Predicate<CSP> {

	// function AC-3(csp) returns false if an inconsistency is found and true
	// otherwise
	// inputs: csp, a binary CSP with components (X, D, C)
	@Override
	public boolean test(CSP csp) {
		// local variables: queue, a queue of arcs, initially all the arcs in
		// csp
		Queue<Arc> queue = allArcs(csp);
		// while queue is not empty do
		while (!queue.isEmpty()) {
			// (X_i, X_j) = REMOVE-FIRST(queue)
			Arc X = queue.remove();
			// if REVISE(csp, X<sub>i</sub>, X<sub>j</sub>) then
			if (revise(csp, X)) {
				// if size of D<sub>i</sub> = 0 then return false
				if (csp.getDomains().get(X.i).size() == 0) {
					return false;
				}
				// for each X<sub>k</sub> in X<sub>i</sub>.NEIGHBORS -
				// {X<sub>j</sub>} do
				for (Arc X_k_i : neighborsMinusJ(X.i, X.j, csp)) {
					// add (X<sub>k</sub>, X<sub>i</sub>) to queue
					queue.add(X_k_i);
				}
			}
		}
		return true;
	}

	// function REVISE(csp, X_i, X_j) returns true iff we revise the domain of
	// X_i
	public boolean revise(CSP csp, Arc X) {
		boolean revised = false;
		// for each x in D<sub>i</sub> do
		for (Object x : csp.getDomains().get(X.i).getValues()) {
			// if no value y in D<sub>j</sub> allows (x, y) to satisfy the
			// constraint between X<sub>i</sub> and X<sub>j</sub> then
			if (!csp.getDomains().get(X.j).getValues().stream()
					.anyMatch(y -> X.constraint.getRelation().isMember(new Object[] { x, y }))) {
				csp.getDomains().get(X.i).delete(x);
				revised = true;
			}
		}
		return revised;
	}

	//
	// Supporting Code
	public static class Arc {
		public final Constraint constraint;
		public final int i, j;

		public Arc(Constraint constraint, int i, int j) {
			this.constraint = constraint;
			this.i = i; // Note: corresponds to constraint.scope[0]
			this.j = j; // Note: corresponds to constraint.scope[1]
		}
	}

	public Queue<Arc> allArcs(CSP csp) {
		Queue<Arc> allArcs = new LinkedList<>();

		for (Constraint c : csp.getConstraints()) {
			if (c.isBinary()) {
				allArcs.add(new Arc(c, csp.indexOf(c.getScope().get(0)), csp.indexOf(c.getScope().get(1))));
			}
		}

		return allArcs;
	}

	public List<Arc> neighborsMinusJ(int i, int j, CSP csp) {
		List<Arc> neighbors = new ArrayList<>();
		String varI = csp.getVariables().get(i);
		String varJ = csp.getVariables().get(j);
		for (Constraint c : csp.getConstraints()) {
			if (c.isBinary()) {
				if (c.getScope().contains(varI) && !c.getScope().contains(varJ)) {
					String scopeO = c.getScope().get(0);
					if (scopeO.equals(varI)) {
						neighbors.add(new Arc(c, i, csp.indexOf(c.getScope().get(1))));
					} else {
						neighbors.add(new Arc(c, csp.indexOf(scopeO), i));
					}
				}
			}
		}
		return neighbors;
	}
}