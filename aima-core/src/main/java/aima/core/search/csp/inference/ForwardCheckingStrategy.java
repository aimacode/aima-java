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
public class ForwardCheckingStrategy<VAR extends Variable, VAL> implements InferenceStrategy<VAR, VAL> {

    /** The CSP is not changed at the beginning. */
    @Override
    public InferenceLog apply(CSP csp) {
        return new EmptyLog<>();
    }

    /**
     * Is called after the assignment has (recursively) been extended by a value assignment
     * for <code>var</code>.
     */
    @Override
    public InferenceLog<VAR, VAL> apply(VAR var, Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
        DomainLog<VAR, VAL> log = new DomainLog<>();
        for (Constraint<VAR, VAL> constraint : csp.getConstraints(var)) {
            List<VAR> scope = constraint.getScope();
            if (scope.size() == 2) {
                for (VAR neighbor : constraint.getScope()) {
                    if (!assignment.contains(neighbor)) {
                        if (revise(neighbor, constraint, assignment, csp, log)) {
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

    private boolean revise(VAR var, Constraint<VAR, VAL> constraint,
                           Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp, DomainLog<VAR, VAL> log) {

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
