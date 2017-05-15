package aima.core.search.csp.inference;

import aima.core.search.csp.CSP;

/**
 * Singleton which represents an empty inference log.
 * @author Ruediger Lunde
 */
public class EmptyLog implements InferenceLog {
    private static final EmptyLog instance = new EmptyLog();
    public static EmptyLog instance() { return instance; }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean inconsistencyFound() {
        return false;
    }

    @Override
    public void undo(CSP csp) {
    }
}
