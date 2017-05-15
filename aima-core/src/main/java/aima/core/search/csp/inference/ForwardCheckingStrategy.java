package aima.core.search.csp.inference;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

import java.util.List;

/**
 * Implements forward checking. Constraints which are not binary are ignored here.
 * @author Ruediger Lunde
 */
public class ForwardCheckingStrategy implements InferenceStrategy {

    /** The CSP is not changed at the beginning. */
    @Override
    public InferenceLog apply(CSP csp) {
        return EmptyLog.instance();
    }

    /**
     * Is called after the assignment has (recursively) been extended by a value assignment
     * for <code>var</code>.
     */
    @Override
    public InferenceLog apply(Variable var, Assignment assignment, CSP csp) {
        DomainLog log = new DomainLog();
        for (Constraint constraint : csp.getConstraints(var)) {
            List<Variable> scope = constraint.getScope();
            if (scope.size() == 2) {
                for (Variable neighbor : constraint.getScope()) {
                    if (!assignment.contains(neighbor)) {
                        if (revise(neighbor, constraint, assignment, csp,
                                log)) {
                            if (csp.getDomain(neighbor).isEmpty()) {
                                log.setEmptyDomainFound(true);
                                return log;
                            }
                        }
                    }
                }
            }
        }
        return log;
    }

    private boolean revise(Variable var, Constraint constraint,
                           Assignment assignment, CSP csp, DomainLog info) {

        boolean revised = false;
        for (Object value : csp.getDomain(var)) {
            assignment.add(var, value);
            if (!constraint.isSatisfiedWith(assignment)) {
                info.storeDomainFor(var, csp.getDomain(var));
                csp.removeValueFromDomain(var, value);
                revised = true;
            }
            assignment.remove(var);
        }
        return revised;
    }
}
