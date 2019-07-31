package aima.core.search.csp.inference;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

/**
 * Implements forward checking. Constraints which are not binary are ignored here.
 * @author Ruediger Lunde
 */
public class ForwardCheckingStrategy<VAR extends Variable, VAL> implements InferenceStrategy<VAR, VAL> {

    /** The CSP is not changed at the beginning. */
    @Override
    public InferenceLog<VAR, VAL> apply(CSP csp) {
        return InferenceLog.emptyLog();
    }

    /**
     * Removes all values from the domains of the neighbor variables of <code>var</code> in the
     * constraint graph which are not consistent with the new value for <code>var</code>.
     * It is called after <code>assignment</code> has (recursively) been extended with a value
     * assignment for <code>var</code>.
     */
    @Override
    public InferenceLog<VAR, VAL> apply(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var) {
        DomainLog<VAR, VAL> log = new DomainLog<>();
        for (Constraint<VAR, VAL> constraint : csp.getConstraints(var)) {
            VAR neighbor = csp.getNeighbor(var, constraint);
            if (neighbor != null && !assignment.contains(neighbor)) {
                if (revise(neighbor, constraint, assignment, csp, log)) {
                    if (csp.getDomain(neighbor).isEmpty()) {
                        log.setEmptyDomainFound(true);
                        return log;
                    }
                }
            }
        }
        return log;
    }

    /**
     * Removes all values from the domain of <code>var</code> which are not consistent with
     * <code>constraint</code> and <code>assignment</code>. Modifies the domain log accordingly so
     * that all changes can be undone later on.
     */
    private boolean revise(VAR var, Constraint<VAR, VAL> constraint, Assignment<VAR, VAL> assignment,
                           CSP<VAR, VAL> csp, DomainLog<VAR, VAL> log) {

        boolean revised = false;
        for (VAL value : csp.getDomain(var)) {
            assignment.add(var, value);
            if (!constraint.isSatisfiedWith(assignment)) {
                log.storeDomainFor(var, csp.getDomain(var));
                csp.removeValueFromDomain(var, value);
                revised = true;
            }
            assignment.remove(var);
        }
        return revised;
    }
}
