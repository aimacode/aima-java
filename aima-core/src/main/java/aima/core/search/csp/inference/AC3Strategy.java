package aima.core.search.csp.inference;

import java.util.Queue;

import aima.core.search.csp.*;
import aima.core.search.framework.QueueFactory;

/**
 * 
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.3, Page 209.<br>
 * <br>
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
 * Figure 6.3 The arc-consistency algorithm AC-3. After applying AC-3, either
 * every arc is arc-consistent, or some variable has an empty domain, indicating
 * that the CSP cannot be solved. The name "AC-3" was used by the algorithm's
 * inventor (Mackworth, 1977) because it's the third version developed in the
 * paper.
 * 
 * @author Ruediger Lunde
 */
public class AC3Strategy implements InferenceStrategy {

	/**
	 * Makes a CSP consisting of binary constraints arc-consistent.
	 * 
	 * @return An object which indicates success/failure and contains data to
	 *         undo the operation.
	 */
	public InferenceLog apply(CSP csp) {
		Queue<Variable> queue = QueueFactory.createLifoQueue();
		queue.addAll(csp.getVariables());
		DomainLog log = new DomainLog();
		reduceDomains(queue, csp, log);
		return log.compactify();
	}

	/**
	 * Reduces the domain of the specified variable to the specified value and
	 * reestablishes arc-consistency. It is assumed that the provided CSP is
	 * arc-consistent before the call.
	 * 
	 * @return An object which indicates success/failure and contains data to
	 *         undo the operation.
	 */
	public InferenceLog apply(Variable var, Assignment assignment, CSP csp) {
		Domain domain = csp.getDomain(var);
		Object value = assignment.getValue(var);
		assert domain.contains(value);
		DomainLog log = new DomainLog();
		if (domain.size() > 1) {
			Queue<Variable> queue = QueueFactory.createLifoQueue();
			queue.add(var);
			log.storeDomainFor(var, domain);
			csp.setDomain(var, new Domain(value));
			reduceDomains(queue, csp, log);
		}
		return log.compactify();
	}

	private void reduceDomains(Queue<Variable> queue, CSP csp, DomainLog info) {
		while (!queue.isEmpty()) {
			Variable var = queue.remove();
			for (Constraint constraint : csp.getConstraints(var)) {
				if (constraint.getScope().size() == 2) {
					Variable neighbor = csp.getNeighbor(var, constraint);
					if (revise(neighbor, var, constraint, csp, info)) {
						if (csp.getDomain(neighbor).isEmpty()) {
							info.setEmptyDomainFound(true);
							return;
						}
						queue.add(neighbor);
					}
				}
			}
		}
	}

	private boolean revise(Variable xi, Variable xj, Constraint constraint,
			CSP csp, DomainLog info) {
		boolean revised = false;
		Assignment assignment = new Assignment();
		for (Object iValue : csp.getDomain(xi)) {
			assignment.add(xi, iValue);
			boolean consistentExtensionFound = false;
			for (Object jValue : csp.getDomain(xj)) {
				assignment.add(xj, jValue);
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
