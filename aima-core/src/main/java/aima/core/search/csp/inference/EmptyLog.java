package aima.core.search.csp.inference;

import aima.core.search.csp.CSP;
import aima.core.search.csp.Variable;

/**
 * Represents an empty inference log.
 * @author Ruediger Lunde
 */
public class EmptyLog<VAR extends Variable, VAL> implements InferenceLog<VAR, VAL> {

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean inconsistencyFound() {
        return false;
    }

    @Override
    public void undo(CSP<VAR, VAL> csp) {
    }
}
