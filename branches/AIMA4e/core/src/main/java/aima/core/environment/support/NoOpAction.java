package aima.core.environment.support;

import aima.core.api.agent.Action;

/**
 * @author Ciaran O'Reilly
 */
public class NoOpAction implements Action {

    @Override
    public boolean isNoOp() {
        return true;
    }

    @Override
    public String toString() {
        return "NoOp";
    }
}
