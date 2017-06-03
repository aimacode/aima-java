package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for CSP solver implementations. Solving a CSP means finding an
 * assignment, which is consistent and complete with respect to a CSP. This
 * abstract class provides the central interface method and additionally an
 * implementation of an observer mechanism.
 *
 * @param <VAR> Type which is used to represent variables
 * @param <VAL> Type which is used to represent the values in the domains
 *
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public abstract class CspSolver<VAR extends Variable, VAL> {

    private List<CspListener<VAR, VAL>> listeners = new ArrayList<>();

    /**
     * Computes a solution to the specified CSP, which specifies values for all
     * variables of the given CSP such that all constraints are satisfied.
     *
     * @param csp a CSP to be solved.
     * @return the computed solution or null if no solution was found.
     */
    public abstract Assignment<VAR, VAL> solve(CSP<VAR, VAL> csp);

    /**
     * Adds a CSP listener to the solution strategy.
     *
     * @param listener a listener which follows the progress of the solution strategy
     *                 step-by-step.
     */
    public void addCspListener(CspListener<VAR, VAL> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a CSP listener from the solution strategy.
     *
     * @param listener the listener to remove
     */
    public void removeCspListener(CspListener<VAR, VAL> listener) {
        listeners.remove(listener);
    }


    /** Informs all registered listeners about a state change. */
    protected void fireStateChanged(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR variable) {
        for (CspListener<VAR, VAL> listener : listeners)
            listener.stateChanged(csp, assignment, variable);
    }
}
