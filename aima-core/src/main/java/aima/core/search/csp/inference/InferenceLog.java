package aima.core.search.csp.inference;

import aima.core.search.csp.CSP;

/**
 * Provides information about (1) whether changes have been performed, (2) possibly inferred empty domains , and
 * (3) how to restore the domains.
 *
 * @author Ruediger Lunde
 */
public interface InferenceLog {
    boolean isEmpty();
    boolean inconsistencyFound();
    void undo(CSP csp);
}
