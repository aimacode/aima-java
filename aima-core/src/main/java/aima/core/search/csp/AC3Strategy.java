package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.util.datastructure.FIFOQueue;

/**
 * 
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.3, Page 213.
 * 
 * <pre>
 * <code>
 * function AC-3(csp) returns false if an inconsistency is found and true otherwise
 *    inputs: csp, a binary CSP with components (X, D, C)
 *    local variables: queue, a queue of arcs, initially all the arcs in csp
 *    while queue is not empty do
 *       (Xi, Xj) = REMOVE-FIRST(queue)
 *       if REVISE(csp, Xi, Xj) then
 *          if size of Di = 0 then return false
 *             for each Xk in Xi.NEIGHBORS - {Xj} do
 *                add (Xk, Xi) to queue
 *    return true
 * 
 * function REVISE(csp, Xi, Xj) returns true iff we revise the domain of Xi
 *    revised = false
 *    for each x in Di do
 *       if no value y in Dj allows (x ,y) to satisfy the constraint between Xi and Xj then
 *          delete x from Di
 *          revised = true
 *    return revised
 * </code>
 * </pre>
 * 
 * @author Ruediger Lunde
 */

public class AC3Strategy {

	public CSP reduceDomains(Variable var, Object value, CSP csp) {
		Domain domain = csp.getDomain(var);
		if (domain.contains(value)) {
			if (domain.size() > 1) {
				FIFOQueue<Variable> queue = new FIFOQueue<Variable>();
				List<Object> newDomain = new ArrayList<Object>(1);
				newDomain.add(value);
				queue.add(var);
				csp = csp.copyForPropagation();
				csp.setDomain(var, newDomain);
				csp = reduceDomains(queue, csp);
			}
		} else {
			csp = null;
		}
		return csp;
	}

	public CSP reduceDomains(CSP csp) {
		FIFOQueue<Variable> queue = new FIFOQueue<Variable>();
		for (Variable var : csp.getVariables())
			queue.add(var);
		return reduceDomains(queue, csp.copyForPropagation());
	}

	protected CSP reduceDomains(FIFOQueue<Variable> queue, CSP csp) {
		
		while (!queue.isEmpty()) {
			Variable var = queue.pop();
			for (Constraint constraint : csp.getConstraints(var)) {
				if (constraint.getScope().size() == 2) {
					Variable neighbor = csp.getNeighbor(var, constraint);
					if (revise(neighbor, var, constraint, csp)) {
						if (csp.getDomain(neighbor).isEmpty())
							return null;
						queue.push(neighbor);
					}
				}
			}
		}
		return csp;
	}

	private boolean revise(Variable xi, Variable xj,
			Constraint constraint, CSP csp) {
		boolean revised = false;
		Assignment assignment = new Assignment();
		for (Object vValue : csp.getDomain(xi)) {
			assignment.setAssignment(xi, vValue);
			boolean vValueOK = false;
			for (Object nValue : csp.getDomain(xj)) {
				assignment.setAssignment(xj, nValue);
				if (constraint.isSatisfiedWith(assignment)) {
					vValueOK = true;
					break;
				}
			}
			if (!vValueOK) {
				csp.removeValueFromDomain(xi, vValue);
				revised = true;
			}
		}
		return revised;
	}
}
